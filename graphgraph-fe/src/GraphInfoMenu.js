import React from 'react'
import './GraphInfoMenu.css'
import PathBreadCrumb from './PathBreadCrumb'
import _ from 'underscore'
import ReactBasicTable from 'react-basic-table'

var generatePropertyTable = function (nodeProps) {
  var columns = ['Property Name', 'Value']
  var rows = _.map(nodeProps, path => {
    return (
      [
        <span>{path.propertyName}</span>,
        <span>{path.propertyValue}</span>
      ]
    )
  })

  return (
    <div className="datatable">
      <ReactBasicTable pageSize={3} rows={rows} columns={columns} />
    </div>
  )
}

class GraphInfoMenu extends React.Component {
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
        <div className="kv-table">
          {generatePropertyTable(this.props.nodeProperties)}
        </div>
      </div>
    )
  }
}

export default GraphInfoMenu
