import React from 'react'
import './App.css'
import Graph from './Graph'
import GraphSettingsMenu from './GraphSettingsMenu'
import GraphInfoMenu from './GraphInfoMenu'
import _ from 'underscore'
import { nodeProperty, edgeProperty } from './requests'
import * as constants from './constants'

var emptyState = {
  selectedSchema: '', // currently selected schema
  graph: {
    nodeNames: [], // names of nodes
    edges: [], // edges (each edge has source, target, type and a list of key value properties for tooltip)
    paths: [] // all paths between start node and end node
  },
  displayedProperties: [], // properties of currently thing (edge or node)
  nodeStates: {}, // possible states CLICKED - the currently selected node PATH - currently displayed path
  pathIndex: 0, // array index to paths i.e. which path from paths is currently displayed
  selectedEdge: { source: 'none', target: 'none' } // defines currently selected edge like like a js object - {source: "pserver", target: "vserver"}
}

class App extends React.Component {
  constructor (props, context) {
    super(props, context)
    this.graphdata = this.graphdata.bind(this)
    this.changePaths = this.changePaths.bind(this)
    this.loadNodeProperties = this.loadNodeProperties.bind(this)
    this.loadEdgeProperties = this.loadEdgeProperties.bind(this)
    this.computeNodeStatesFromPath = this.computeNodeStatesFromPath.bind(this)
    this.computeNodeStates = this.computeNodeStates.bind(this)

    this.state = emptyState
  }

  loadNodeProperties (nodeName) {
    var s = this.state
    fetch(nodeProperty(s.selectedSchema, nodeName))
      .then(response => response.json())
      .then(p => {
        s['displayedProperties'] = p
        s['nodeStates'] = this.computeNodeStates(s['pathIndex'])
        // select node
        s['nodeStates'][nodeName] = constants.CLICKED
        // unselect edge
        s['selectedEdge']['source'] = ''
        s['selectedEdge']['target'] = ''
        this.setState(s)
      })
  }

  loadEdgeProperties (source, target) {
    var s = this.state
    fetch(edgeProperty(s.selectedSchema, source, target))
      .then(response => response.json())
      .then(p => {
        s['displayedProperties'] = p
        // select edge
        s['selectedEdge']['source'] = source
        s['selectedEdge']['target'] = target
        // unselect node
        s['nodeStates'] = this.computeNodeStates(s['pathIndex'])
        this.setState(s)
      })
  }

  graphdata (data, selectedGraphSchema, graphFingerprint) {
    var s = this.state
    s['selectedSchema'] = selectedGraphSchema
    s['graphFingerprint'] = graphFingerprint
    s['graph'] = data
    s['displayedProperties'] = data.startNodeProperties
    if (_.isArray(data.paths) && !_.isEmpty(data.paths)) {
      s['paths'] = data.paths
      s['nodeStates'] = this.computeNodeStatesFromPath(data.paths[0])
      s['pathIndex'] = 0
    } else {
      s['paths'] = []
      s['nodeStates'] = {}
    }
    this.setState(s)
    return data
  }

  computeNodeStatesFromPath (path) {
    return _.reduce(path, (acc, node) => {
      acc[node.id] = constants.PATH
      return acc
    }, {})
  }

  computeNodeStates (pathIndex) {
    return this.computeNodeStatesFromPath(this.state.paths[pathIndex])
  }

  changePaths (pathIndex, selectedNode) {
    var s = this.state
    s['pathIndex'] = pathIndex
    this.setState(s)
    this.loadNodeProperties(selectedNode)
  }

  render () {
    return (
      <div className="App">
        <GraphSettingsMenu graphData={this.graphdata}/>
        <div className='graph-area'>
          <Graph graphFingerprint={this.state.graphFingerprint} edgePropsLoader={this.loadEdgeProperties} selectedEdge={this.state.selectedEdge} nodes={this.state.graph.nodeNames} edges={this.state.graph.edges} nodeStates={this.state.nodeStates} nodePropsLoader={this.loadNodeProperties}/>
        </div>

        <GraphInfoMenu pathCallback={this.changePaths} paths={ this.state.graph.paths } nodeProperties={ this.state.displayedProperties} />
      </div>
    )
  }
}

export default App
