const host = window.location.hostname;
const port = window.location.port;
const protocol = window.location.protocol;

export function schemas () {
  return `${protocol}//${host}:${port}/schemas`
}

export function validateSchema (schema) {
  return `${protocol}//${host}:${port}/schemas/${schema}/validation`
}

export function nodeNames (schema, edgeFilter) {
  return `${protocol}//${host}:${port}/schemas/${schema}/nodes?edgeFilter=${edgeFilter}`
}

export function basicGraph (schema, node, parentHops, cousinHops, childHops, edgeFilter) {
  return `${protocol}//${host}:${port}/schemas/${schema}/graph/basic?node=${node}&parentHops=${parentHops}&cousinHops=${cousinHops}&childHops=${childHops}&edgeFilter=${edgeFilter}`
}

export function pathGraph (schema, fromNode, toNode, edgeFilter) {
  return `${protocol}//${host}:${port}/schemas/${schema}/graph/paths?fromNode=${fromNode}&toNode=${toNode}&edgeFilter=${edgeFilter}`
}

export function nodeProperty (schema, node) {
  return `${protocol}//${host}:${port}/schemas/${schema}/nodes/${node}`
}

export function edgeProperty (schema, fromNode, toNode) {
  return `${protocol}//${host}:${port}/schemas/${schema}/edges?fromNode=${fromNode}&toNode=${toNode}`
}
