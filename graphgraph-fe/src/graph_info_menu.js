/*
 *  ============LICENSE_START=======================================================
 * org.onap.aai
 *  ================================================================================
 *  Copyright © 2019-2020 Orange Intellectual Property. All rights reserved.
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

import React from 'react';
import './graph_info_menu.css';
import PathBreadcrumb from './path_breadcrumb.js';
import _ from 'underscore';
import ReactTable from "react-table";
import "react-table/react-table.css";

class GraphInfoMenu extends React.Component {
    render () {
        var paths = this.props.paths;
        var callback = this.props.pathCallback;
        var showPaths = _.isArray(paths) && !_.isEmpty(paths);
        var breadcrumbs = _.map(
            paths, (path, i) => <PathBreadcrumb key={i} index={i} pathCallback={callback} path={path}/>);
        return (
                <div className="node-property-list">
                <div className="fixed-height-container" style={{ display: showPaths ? 'block' : 'none' }}><p className='path-heading'>Paths</p>{breadcrumbs}</div>
                <div className="kv-table datatable">
                <ReactTable pageSizeOptions={[4, 25]} data={this.props.nodeProperties} columns={[
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
                ]} defaultPageSize={4} className="-striped -highlight"
                />
                </div>
                </div>
        );
    }
}

export default GraphInfoMenu;
