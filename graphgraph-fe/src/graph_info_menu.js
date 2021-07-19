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
import { useTable, usePagination } from 'react-table'

function Table({ columns, data }) {

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
        canPreviousPage,
        canNextPage,
        pageOptions,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: { pageIndex, pageSize },
    } = useTable(
        {
            columns,
            data,
            initialState: { pageIndex: 0, pageSize: 5 },
        },
        usePagination
    )

    return (
        <>
            <table {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row, i) => {
                    prepareRow(row)
                    return (
                        <tr {...row.getRowProps()}>
                            {row.cells.map(cell => {
                                return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            })}
                        </tr>
                    )
                })}
                </tbody>
            </table>

            <div className="pagination">
                <div className="pagination-controls">
                <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
                    {'<<'}
                </button>{' '}
                <button onClick={() => previousPage()} disabled={!canPreviousPage}>
                    {'<'}
                </button>{' '}
                <button onClick={() => nextPage()} disabled={!canNextPage}>
                    {'>'}
                </button>{' '}
                <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
                    {'>>'}
                </button>{' '}
                <span>
                    Page{' '}
                    <strong>
                        {pageIndex + 1} of {pageOptions.length}
                    </strong>{' '}
                </span>
                <span>
                    | Go to page:{' '}
                    <input
                        type="number"
                        defaultValue={pageIndex + 1}
                        onChange={e => {
                            const page = e.target.value ? Number(e.target.value) - 1 : 0
                            gotoPage(page)
                        }}
                        style={{ width: '100px' }}
                    />
                </span>{' '}
                <select
                    value={pageSize}
                    onChange={e => {
                        setPageSize(Number(e.target.value))
                    }}
                >
                    {[5, 25, 50].map(pageSize => (
                        <option key={pageSize} value={pageSize}>
                            Show {pageSize}
                        </option>
                    ))}
                </select>
                </div>
            </div>
        </>
    )
}

const GraphInfoMenu = (props) => {

        const columns = React.useMemo(
            () => [
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
            ],
            []
        );

        var paths = props.paths;
        var callback = props.pathCallback;
        var showPaths = _.isArray(paths) && !_.isEmpty(paths);
        var breadcrumbs = _.map(
            paths, (path, i) => <PathBreadcrumb key={i} index={i} pathCallback={callback} path={path}/>);

        return (
                <div className="node-property-list">
                <div className="fixed-height-container" style={{ display: showPaths ? 'block' : 'none' }}><p className='path-heading'>Paths</p>{breadcrumbs}</div>
                <div className="kv-table datatable">

                <div className={'prop-table-wrapper'}>
                    <Table columns={columns} data={props.nodeProperties} />
                </div>

                </div>
                </div>
        );

}

export default GraphInfoMenu;
