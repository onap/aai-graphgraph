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
import './app.css';
import Graph from './graph.js';
import GraphSettingsMenu from './graph_settings_menu.js';
import GraphInfoMenu from './graph_info_menu.js';
import _ from 'underscore';
import { nodeProperty, edgeProperty } from './requests.js';
import * as constants from './constants.js';

var emptyState = {
    // currently selected schema
    selectedSchema: '',
    graph: {
        // names of nodes
        nodeNames: [],
        // edges (each edge has source, target, type and a list of key value properties for tooltip)
        edges: [],
        // all paths between start node and end node
        paths: []
    },
    // properties of currently thing (edge or node)
    displayedProperties: [],
    // possible states:
    // CLICKED - the currently selected node
    // PATH - currently displayed path
    nodeStates: {},
    // array index to paths i.e. which path from paths is currently displayed
    pathIndex: 0,
    // defines currently selected edge like a js object - {source: "pserver", target: "vserver"}
    selectedEdge: { source: 'none', target: 'none' }
};

class App extends React.Component {
    constructor (props, context) {
        super(props, context);
        this.graphdata = this.graphdata.bind(this);
        this.changePaths = this.changePaths.bind(this);
        this.loadNodeProperties = this.loadNodeProperties.bind(this);
        this.loadEdgeProperties = this.loadEdgeProperties.bind(this);
        this.computeNodeStatesFromPath = this.computeNodeStatesFromPath.bind(this);
        this.computeNodeStates = this.computeNodeStates.bind(this);
        this.state = emptyState;
    }

    loadNodeProperties (nodeName) {
        var s = this.state;
        fetch(nodeProperty(s.selectedSchema, nodeName))
            .then(response => response.json())
            .then(p => {
                s['displayedProperties'] = p;
                s['nodeStates'] = this.computeNodeStates(s['pathIndex']);
                // select node
                s['nodeStates'][nodeName] = constants.CLICKED;
                // unselect edge
                s['selectedEdge']['source'] = '';
                s['selectedEdge']['target'] = '';
                this.setState(s);
            });
    }

    loadEdgeProperties (source, target) {
        var s = this.state;
        fetch(edgeProperty(s.selectedSchema, source, target))
            .then(response => response.json())
            .then(p => {
                s['displayedProperties'] = p;
                // select edge
                s['selectedEdge']['source'] = source;
                s['selectedEdge']['target'] = target;
                // unselect node
                s['nodeStates'] = this.computeNodeStates(s['pathIndex']);
                this.setState(s);
            });
    }

    graphdata (data, selectedGraphSchema, graphFingerprint) {
        var s = this.state;
        s['selectedSchema'] = selectedGraphSchema;
        s['graphFingerprint'] = graphFingerprint;
        s['graph'] = data;
        // TODO this should be handled more gracefully ...
        if (_.isEmpty(data.edges)) {
            alert('The graph has no edges, nothing to display');
        }
        s['displayedProperties'] = data.startNodeProperties;
        if (_.isArray(data.paths) && !_.isEmpty(data.paths)) {
            s['paths'] = data.paths;
            s['nodeStates'] = this.computeNodeStatesFromPath(data.paths[0]);
            s['pathIndex'] = 0;
        } else {
            s['paths'] = [];
            s['nodeStates'] = {};
        }
        this.setState(s);
        return data;
    }

    computeNodeStatesFromPath (path) {
        return _.reduce(path, (acc, node) => {
            acc[node.id] = constants.PATH;
            return acc;
        }, {});
    }

    computeNodeStates (pathIndex) {
        return this.computeNodeStatesFromPath(this.state.paths[pathIndex]);
    }

    changePaths (pathIndex, selectedNode) {
        var s = this.state;
        s['pathIndex'] = pathIndex;
        this.setState(s);
        this.loadNodeProperties(selectedNode);
    }

    render () {
        let n = _.invert(this.state.nodeStates)[constants.CLICKED];

        let selectedNode = _.isUndefined(n) ? '' : n;
        return (
                <div className="App">
                <GraphSettingsMenu graphData={this.graphdata} nodePropsLoader={this.loadNodeProperties} selectedNode={selectedNode}/>
                <div className='graph-area'>
                <Graph graphFingerprint={this.state.graphFingerprint} edgePropsLoader={this.loadEdgeProperties} selectedEdge={this.state.selectedEdge} nodes={this.state.graph.nodeNames} edges={this.state.graph.edges} nodeStates={this.state.nodeStates} nodePropsLoader={this.loadNodeProperties}/>
                </div>
                <GraphInfoMenu pathCallback={this.changePaths} paths={ this.state.graph.paths } nodeProperties={ this.state.displayedProperties}/>
                </div>
        );
    }
}

export default App;
