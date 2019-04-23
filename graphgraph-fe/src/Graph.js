import React from 'react'
import * as d3 from 'd3'
import './Graph.css'
import _ from 'underscore'
import * as constants from './constants'

const CLICKED_COLOR = 'blue'
const PATH_COLOR = 'red'
const EDGE_MOUSE_OVER_COLOR = 'yellow'
const EDGE_NORMAL_COLOR = '#ccc'
const EDGE_LABEL_COLOR = 'black'
const NODE_NORMAL_COLOR = '#E3E3E3'
const NODE_LABEL_COLOR = 'black'
const NODE_MOUSE_OVER_COLOR = '#F6F6F6'
const NODE_BORDER_COLOR = 'lightgray'

// variable holds state in order to determine if graph should be redrawn completely
// it breaks the react concept, so a better approach is needed
var graphFingerprint = ''

var htmlfyProperties = function (properties) {
  return "<div class='d3-tip'>" + (_.reduce(properties, (html, e) => { return html + "<span style='color: lightgray'>" + e.propertyName + ':</span> <span>' + e.propertyValue + '</span><br/>' }, '')) + '</div>'
}

var mouseOverEdge = function (edge, div) {
  div.transition()
    .duration(20)
    .style('opacity', 0.9)
  div.html(htmlfyProperties(edge.tooltipProperties))
    .style('left', `${d3.event.pageX}px`)
    .style('top', `${d3.event.pageY - 28}px`)
}

var mouseOutEdge = function (edge, div) {
  div.transition()
    .duration(6000)
    .style('opacity', 0)
}

var addEdgePaths = function (links, g) {
  d3.select('body').append('div')
    .attr('class', 'tooltip')
    .style('opacity', 0)
  g.selectAll('.edgepath')
    .data(links)
    .enter()
    .append('path')
    .attr('d', d => `M ${d.source.x} ${d.source.y} L ${d.target.x} ${d.target.y}`)
    .attr('class', 'edgepath')
    .attr('fill-opacity', 0)
    .attr('stroke-opacity', 0)
    .attr('fill', EDGE_LABEL_COLOR)
    .attr('id', (d, i) => `edgepath${i}`)
}

var chooseColor = function (state) {
  if (state === constants.CLICKED) {
    return CLICKED_COLOR
  }

  if (state === constants.PATH) {
    return PATH_COLOR
  }

  return NODE_NORMAL_COLOR
}

var redrawNodeColors = function (nodeStates, selectedEdge) {
  d3.selectAll('svg').selectAll('g').selectAll('circle').style('fill', n => chooseColor(nodeStates[n.id]))
  d3.selectAll('svg').selectAll('g').selectAll('line').style('stroke', EDGE_NORMAL_COLOR).attr('oldStroke', EDGE_NORMAL_COLOR)
  d3.selectAll('svg').selectAll('g').selectAll('line').filter(edge => edge.source.id === selectedEdge.source && edge.target.id === selectedEdge.target).attr('oldStroke', CLICKED_COLOR).style('stroke', CLICKED_COLOR)
}

var addEdgeLabels = function (links, g, div) {
  var edgelabels = g.selectAll('.edgelabel')
    .data(links)
    .enter()
    .append('text')
    .attr('class', 'edgelabel')
    .attr('text-anchor', 'middle')
    .attr('dx', 200)
    .attr('dy', 0)
    .attr('font-size', '22px')
    .attr('id', (d, i) => `edgelabel${i}`)

  edgelabels.append('textPath')
    .attr('xlink:href', (d, i) => `#edgepath${i}`)
    .text((d, i) => d.type)
}

var addNodeLabels = function (nodes, g) {
  g.selectAll('.nodelabel')
    .data(nodes)
    .enter()
    .append('text')
    .attr('x', d => d.x - 14)
    .attr('y', d => d.y - 17)
    .attr('class', 'nodelabel')
    .attr('fill', NODE_LABEL_COLOR)
    .attr('font-size', '32px')
    .text(d => d.id)
    .on('mouseenter', onNodeLabelMouseOver)
    .on('mouseout', onNodeLabelMouseOut)
}

var addLinks = function (links, g, div, edgePropsLoader) {
  let ss = _.filter(links, l => l.source.id === l.target.id)

  let selfLinks = _.isUndefined(ss) ? [] : ss

  g.selectAll('ellipse')
    .data(selfLinks)
    .enter().append('ellipse')
    .attr('fill-opacity', 0)
    .attr('rx', d => 100)
    .attr('ry', d => 16)
    .attr('cx', d => d.target.x + 80)
    .attr('cy', d => d.target.y)
    .style('stroke', NODE_BORDER_COLOR)
    .attr('stroke-width', 5)
    .on('click', edge => edgePropsLoader(edge.source.id, edge.target.id))
    .on('mouseenter', function (edge) {
      mouseOverEdge(edge, div)
      d3.select(this)
        .transition()
        .attr('oldStroke', EDGE_NORMAL_COLOR)
        .duration(10)
        .style('stroke', EDGE_MOUSE_OVER_COLOR)
    })
    .on('mouseleave', function (edge) {
      mouseOutEdge(edge, div)
      var strokeColor = d3.select(this).attr('oldStroke')
      d3.select(this)
        .transition()
        .duration(300)
        .style('stroke', strokeColor)
    })

  g.selectAll('.edgelooplabel')
    .data(selfLinks)
    .enter()
    .append('text')
    .attr('x', d => d.source.x + 35)
    .attr('y', d => d.source.y + 50)
    .attr('class', 'edgelooplabel')
    .attr('fill', NODE_LABEL_COLOR)
    .attr('font-size', '22px')
    .text(d => d.type)

  g.selectAll('line')
    .data(links)
    .enter().append('line')
    .attr('stroke-width', 5)
    .attr('x1', d => d.source.x)
    .attr('y1', d => d.source.y)
    .attr('x2', d => d.target.x)
    .attr('y2', d => d.target.y)
    .attr('id', (d, i) => `edge${i}`)
    .attr('marker-end', 'url(#arrowhead)')
    .style('stroke', EDGE_NORMAL_COLOR)
    .on('click', edge => edgePropsLoader(edge.source.id, edge.target.id))
    .on('mouseenter', function (edge) {
      mouseOverEdge(edge, div)
      d3.select(this)
        .transition()
        .attr('oldStroke', EDGE_NORMAL_COLOR)
        .duration(10)
        .style('stroke', EDGE_MOUSE_OVER_COLOR)
    })
    .on('mouseleave', function (edge) {
      mouseOutEdge(edge, div)
      var strokeColor = d3.select(this).attr('oldStroke')
      d3.select(this)
        .transition()
        .duration(300)
        .style('stroke', strokeColor)
    })
}

var addMarkers = function (g, svg) {
  g.append('defs').append('marker')
    .attr('id', 'arrowhead')
    .attr('viewBox', '-0 -5 10 10')
    .attr('refX', '20')
    .attr('refY', '0')
    .attr('orient', 'auto')
    .attr('markerWidth', '4')
    .attr('markerHeight', '4')
    .attr('xoverflow', 'visible')
    .append('svg:path')
    .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
    .attr('fill', EDGE_NORMAL_COLOR)
    .attr('stroke', EDGE_NORMAL_COLOR)

  var zoomHandler = d3.zoom()
    .on('zoom', _ => g.attr('transform', d3.event.transform))

  zoomHandler(svg)
  zoomHandler.translateTo(svg, -7000, -4000)
  zoomHandler.scaleTo(svg, 0.08)
}

var drawGraph = function (nodes, links, g, simulation, svg, addNodes, edgePropsLoader) {
  for (var i = 0, n = Math.ceil(Math.log(simulation.alphaMin()) / Math.log(1 - simulation.alphaDecay())); i < n; ++i) {
    simulation.tick()
  }

  var div = d3.select('body').append('div')
    .attr('class', 'tooltip')
    .style('opacity', 0)

  addLinks(links, g, div, edgePropsLoader)
  addNodes(nodes, g)
  addNodeLabels(nodes, g)
  addEdgePaths(links, g)
  addEdgeLabels(links, g, div)
  addMarkers(g, svg)
}

var prepareLinks = function (nodes, links) {
  var result = []
  links.forEach(e => {
    var sourceNode = nodes.filter(n => n.id === e.source)[0]
    var targetNode = nodes.filter(n => n.id === e.target)[0]

    result.push({
      source: sourceNode,
      target: targetNode,
      type: e.type,
      tooltipProperties: e.tooltipProperties
    })
  })

  return result
}

var createSimulation = function (nodes, links) {
  return d3.forceSimulation(nodes)
    .force('charge', d3.forceManyBody().strength(-201))
    .force('link', d3.forceLink(links).distance(1200).strength(1).iterations(400))
    .force('collision', d3.forceCollide().radius(d => 310))
    .force('x', d3.forceX())
    .force('y', d3.forceY())
    .stop()
}

var createSvg = function () {
  var svg = d3.select('#graph').append('svg').attr('height', '100%').attr('width', '100%')
  var g = svg.append('g')

  return { 'g': g, 'svg': svg }
}

var onNodeLabelMouseOut = function () {
  d3.select(this)
    .transition()
    .duration(600)
    .attr('font-size', '32px')
}

var onNodeLabelMouseOver = function () {
  d3.select(this)
    .transition()
    .duration(200)
    .attr('font-size', '232px')
}

var onNodeMouseOver = function () {
  var oldFill = d3.select(this).style('fill')
  d3.select(this)
    .transition()
    .duration(200)
    .attr('r', 31)
    .attr('oldFill', oldFill)
    .style('fill', NODE_MOUSE_OVER_COLOR)
}

var onNodeMouseOut = function (nodeStates) {
  var oldFill = d3.select(this).attr('oldFill')
  d3.select(this)
    .transition()
    .duration(300)
    .attr('r', 23)
    .style('fill', oldFill)
}

class Graph extends React.Component {
  onNodeClick (x) {
    // on mouse out the node will change color read from 'oldFill' attribute
    d3.selectAll('svg').selectAll('g').selectAll('circle').filter(c => c.id === x.id).attr('oldFill', CLICKED_COLOR)
    this.props.nodePropsLoader(x.id)
  }

  addNodes (nodes, g) {
    g.selectAll('circle')
      .data(nodes)
      .enter().append('circle')
      .attr('cx', d => d.x)
      .attr('cy', d => d.y)
      .style('fill', n => {
        return chooseColor(this.props.nodeStates[n.id])
      })
      .style('stroke', NODE_BORDER_COLOR)
      .attr('stroke-width', 3)
      .attr('r', 23)
      .on('click', this.onNodeClick)
      .on('mouseover', onNodeMouseOver)
      .on('mouseout', onNodeMouseOut)
  }

  reCreateGraph () {
    d3.select('#graph').selectAll('*').remove()

    var nodes = this.props.nodes
    var links = prepareLinks(this.props.nodes, this.props.edges)
    var o = createSvg()
    var simulation = createSimulation(nodes, links)

    drawGraph(nodes, links, o.g, simulation, o.svg, this.addNodes, this.props.edgePropsLoader)
  }

  constructor (props, context) {
    super(props, context)

    this.reCreateGraph = this.reCreateGraph.bind(this)
    this.addNodes = this.addNodes.bind(this)
    this.onNodeClick = this.onNodeClick.bind(this)
  }

  render () {
    if (this.props.graphFingerprint !== graphFingerprint) {
      this.reCreateGraph()
      graphFingerprint = this.props.graphFingerprint
    } else {
      redrawNodeColors(this.props.nodeStates, this.props.selectedEdge)
    }

    return (<div id="graph"/>)
  }
}

export default Graph
