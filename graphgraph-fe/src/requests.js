const host = window.location.hostname;
const port = 8453;

export function schemas () {
  return `http://${host}:${port}/schemas`
}

export function validateSchema (schema) {
  return `http://${host}:${port}/schemas/${schema}/validation`
}

export function nodeNames (schema, edgeFilter) {
  return `http://${host}:${port}/schemas/${schema}/nodes?edgeFilter=${edgeFilter}`
}

export function basicGraph (schema, node, parentHops, cousinHops, childHops, edgeFilter) {
  return `http://${host}:${port}/schemas/${schema}/graph/basic?node=${node}&parentHops=${parentHops}&cousinHops=${cousinHops}&childHops=${childHops}&edgeFilter=${edgeFilter}`
}

export function pathGraph (schema, fromNode, toNode, edgeFilter) {
  return `http://${host}:${port}/schemas/${schema}/graph/paths?fromNode=${fromNode}&toNode=${toNode}&edgeFilter=${edgeFilter}`
}

export function nodeProperty (schema, node) {
  return `http://${host}:${port}/schemas/${schema}/nodes/${node}`
}

export function edgeProperty (schema, fromNode, toNode) {
  return `http://${host}:${port}/schemas/${schema}/edges?fromNode=${fromNode}&toNode=${toNode}`
}
