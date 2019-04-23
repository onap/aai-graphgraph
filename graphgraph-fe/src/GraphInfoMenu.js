import React from 'react'
import './GraphInfoMenu.css'
import PathBreadCrumb from './PathBreadCrumb'
import _ from 'underscore'
import ReactBasicTable from 'react-basic-table'

var getRows = function (nodeProps) {
  return _.map(nodeProps, path => {
    return (
      [
        <span>{path.propertyName}</span>,
        <span>{path.propertyValue}</span>
      ]
    )
  })
}

class GraphInfoMenu extends React.Component {
  constructor (props) {
    super(props)
    this.basicTable = React.createRef()
  }

  // ReactBasicTable does not reset pagination after
  // changing data, we need to do it manually
  componentDidUpdate (prevProps) {
    this.basicTable.current.setPage(1)
  }

  render () {
    var paths = this.props.paths
    var callback = this.props.pathCallback
    var showPaths = _.isArray(paths) && !_.isEmpty(paths)
    var breadcrumbs = _.map(paths, (path, i) => <PathBreadCrumb key={i} index={i} pathCallback={callback} path={path}/>)
    return (
      <div className="node-property-list">
        <div className="fixed-height-container" style={{ display: showPaths ? 'block' : 'none' }}>
          <p className='path-heading'>Paths</p>
          {breadcrumbs}
        </div>
        <div className="kv-table datatable">
          <ReactBasicTable ref={this.basicTable} pageSize={3} rows={getRows(this.props.nodeProperties)} columns={['Property Name', 'Value']} />
        </div>
      </div>
    )
  }
}

export default GraphInfoMenu
