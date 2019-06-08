import React from 'react'
import './GraphInfoMenu.css'
import PathBreadCrumb from './PathBreadCrumb'
import _ from 'underscore'
import ReactTable from "react-table";
import "react-table/react-table.css";

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
        <div className="kv-table datatable">
          <ReactTable pageSizeOptions={[4, 25]} data={this.props.nodeProperties} 
		columns={[
            {
              Header: "Attribute",
              accessor: "propertyName",
              minWidth: 40
            },
            {
              Header: "Type",
              accessor: "type",
              minWidth: 70
            },
            {
              Header: "Description",
              accessor: "description",
              minWidth: 260
            },
            {
              id: "Key",
              Header: "Key",
              accessor: p => p.key ? "yes" : "",
              minWidth: 20
            },
            {
              id: "Index",
              Header: "Index",
              accessor: p => p.index ? "yes" : "",
              minWidth: 20
            },
            {
              id: "Required",
              Header: "Required",
              accessor: p => p.required ? "yes" : "",
              minWidth: 20
            }
          ]}
          defaultPageSize={4}
          className="-striped -highlight"
        />
        </div>
      </div>
    )
  }
}

export default GraphInfoMenu
