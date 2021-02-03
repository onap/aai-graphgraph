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
import _ from 'underscore';
import { DropdownButton, MenuItem, Label } from 'react-bootstrap';
import './graph_settings.css';
import Popup from './popup_settings.js';
import ValidationModal from './validation_modal.js';
import DownloadExport from './download_export.js';
import { validateSchema, pathGraph, basicGraph, schemas, nodeNames } from './requests.js';

var emptyState = {
    schemaProblems: [],
    nodeNames: [],
    fromNode: '',
    graph: {
        nodeNames: [],
        edges: []
    },
    showHops: false,
    enableDestinationNode: false,
    toNode: '',
    edgeFilter: 'Edgerules',
    hops: {
        parents: 1,
        cousin: 1,
        child: 1
    },
    selectedSchema: ''
};

class GraphSettings extends React.Component {
    constructor (props, context) {
        super(props, context);
        this.onChangeStartNode = this.onChangeStartNode.bind(this);
        this.onSelectNode = this.onSelectNode.bind(this);
        this.selectSchema = this.selectSchema.bind(this);
        this.onChangeToNode = this.onChangeToNode.bind(this);
        this.loadInitialGraph = this.loadInitialGraph.bind(this);
        this.updateHops = this.updateHops.bind(this);
        this.changeEdgeFilter = this.changeEdgeFilter.bind(this);
        this.graphFingerprint = this.graphFingerprint.bind(this);
        this.state = emptyState;
    }

    /* this serves as a config 'fingerprint' to know if the d3 visualisation
       should be redrawn from scratch or just updated */
    graphFingerprint (schema, from, to, parents, cousin, child, edgeFilter) {
        return `${schema}:${from}:${to}:${parents}:${cousin}:${child}:${edgeFilter}`;
    }

    loadInitialGraph (startNode, endNode, parentHops, cousinHops, childHops, edgeFilter) {
        if (this.state.selectedSchema === '' || startNode === 'none') {
            var s = this.state;
            s['edgeFilter'] = edgeFilter;
            this.setState(s);
            return;
        }
        if (startNode === 'all') {
            endNode = 'none';
        }

        let requestUri = endNode === 'none'
            ? basicGraph(
                this.state.selectedSchema, startNode, parentHops, cousinHops, childHops, edgeFilter)
            : pathGraph(
                this.state.selectedSchema, startNode, endNode, edgeFilter);

        fetch(requestUri)
            .then(response => response.json())
            .then(g => {
                let schema = this.state.selectedSchema;
                let f = this.graphFingerprint(
                    schema, startNode, endNode, parentHops, cousinHops, childHops, edgeFilter);
                this.props.graphData(g, this.state.selectedSchema, f);
                return g;
            })
            .then(g => {
                var s = this.state;
                s['hops']['parents'] = parentHops;
                s['hops']['cousin'] = cousinHops;
                s['hops']['child'] = childHops;
                s['fromNode'] = startNode;
                s['toNode'] = endNode;
                s['graph'] = g;
                s['edgeFilter'] = edgeFilter;
                s['showHops'] = endNode === 'none' && startNode !== 'none' && startNode !== 'all';
                s['enableDestinationNode'] = startNode !== 'none' && startNode !== 'all';
                this.setState(s);

                if (startNode !== 'all') {
                    this.onSelectNode(startNode);
                }
            });
    }

    selectSchema (schema) {
        var s = this.state;
        s['selectedSchema'] = schema;
        fetch(nodeNames(schema, s['edgeFilter']))
            .then(response => response.json())
            .then(nodeNames => {
                s['fromNode'] = s['toNode'] = 'none';
                s['nodeNames'] = nodeNames;
                this.setState(s);
            });
        fetch(validateSchema(schema))
            .then(response => response.json())
            .then(p => {
                s['schemaProblems'] = p.problems;
                this.setState(s);
            });
    }

    changeEdgeFilter (edgeFilter) {
        fetch(nodeNames(this.state.selectedSchema, edgeFilter))
            .then(response => response.json())
            .then(nodeNames => {
                let s = this.state;
                s['edgeFilter'] = edgeFilter;
                s['fromNode'] = s['toNode'] = 'none';
                s['nodeNames'] = nodeNames;
                this.setState(s);
            });
        this.loadInitialGraph(
            this.state.fromNode,
            this.state.toNode,
            this.state.hops.parents,
            this.state.hops.cousin,
            this.state.hops.child,
            edgeFilter
        );
    }

    updateHops (parentHops, cousinHops, childHops) {
        this.loadInitialGraph(
            this.state.fromNode,
            this.state.toNode,
            parentHops,
            cousinHops,
            childHops,
            this.state.edgeFilter
        );
    }

    onChangeToNode (eventKey) {
        this.loadInitialGraph(
            this.state.fromNode,
            eventKey,
            this.state.hops.parents,
            this.state.hops.cousin,
            this.state.hops.child,
            this.state.edgeFilter
        );
    }

    onSelectNode (eventKey) {
        this.props.nodePropsLoader(eventKey);
    }

    onChangeStartNode (eventKey) {
        this.loadInitialGraph(
            eventKey,
            this.state.toNode,
            this.state.hops.parents,
            this.state.hops.cousin,
            this.state.hops.child,
            this.state.edgeFilter
        );
    }

    componentDidMount () {
        fetch(schemas())
            .then(response => response.json())
            .then(schemas => {
                let s = this.state;
                s['schemas'] = schemas;
                this.setState(s);
            });
    }

    render () {
        var schemas = _.map(this.state.schemas, (x, k) => <MenuItem key={k} eventKey={x}>{x}</MenuItem>);

        var items = _.map(this.state.nodeNames, (x, k) => <MenuItem key={k} eventKey={x.id}>{x.id}</MenuItem>);
        let sortedNames = _.sortBy(this.state.graph.nodeNames, 'id');
        var currentNodeNames = _.map(sortedNames, (x, k) => <MenuItem key={k} eventKey={x.id}>{x.id}</MenuItem>);

        var fromItems = items.slice();
        fromItems.unshift(<MenuItem key='divider' divider/>);
        fromItems.unshift(<MenuItem key='all' eventKey='all'>all</MenuItem>);

        items.unshift(<MenuItem key='anotherdivider' divider/>);
        items.unshift(<MenuItem key='none' eventKey='none'>none</MenuItem>);

        let edgeFilterItems = [
                <MenuItem key='Edgerules' eventKey='Edgerules'>Edgerules</MenuItem>,
                <MenuItem key='Parents' eventKey='Parents'>Parent-child (OXM structure)</MenuItem>,
        ];
        return (
            <div>
                <div className="graph-menu">
                    <div className="startendnode-dropdown">
                        <div>
                            <Label>Schemas</Label>
                            <DropdownButton className="schemas-dropdown" onSelect={this.selectSchema} id="schemas"
                                            title={this.state.selectedSchema}>{schemas}</DropdownButton>
                        </div>
                        <div className="source-dropdown-div">
                            <Label>Source Node</Label>
                            <DropdownButton className="node-dropdown" onSelect={this.onChangeStartNode} id="namesFrom"
                                            title={this.state.fromNode}>{fromItems}</DropdownButton>
                        </div>
                        <div>
                            <Label>Destination Node</Label>
                            <DropdownButton disabled={!this.state.enableDestinationNode} className="node-dropdown"
                                            onSelect={this.onChangeToNode} id="namesTo"
                                            title={this.state.toNode}>{items}</DropdownButton>
                        </div>
                        <div className="source-dropdown-div">
                            <Label>Edge filter</Label>
                            <DropdownButton className="node-dropdown" onSelect={this.changeEdgeFilter} id="filterEdge"
                                            title={this.state.edgeFilter}>{edgeFilterItems}</DropdownButton>
                        </div>
                        <div>
                            <Label>Selected Node</Label>
                            <DropdownButton className="node-dropdown" onSelect={this.onSelectNode} id="selectedNode"
                                            title={this.props.selectedNode}>{currentNodeNames}</DropdownButton>
                        </div>
                        <Popup isDisabled={!this.state.showHops} edgeFilter={this.state.edgeFilter}
                               parentHops={this.state.hops.parents} childHops={this.state.hops.child}
                               cousinHops={this.state.hops.cousin} updateHops={this.updateHops}/>
                        <div className="modal-button">
                            <ValidationModal schemaProblems={this.state.schemaProblems}/>
                        </div>
                        <div className="modal-button">
                            <DownloadExport schemaVersion={this.state.selectedSchema}/>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default GraphSettings;
