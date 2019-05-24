
export function schemas () {
  return 'http://localhost:8080/schemas'
}

export function nodeNames (schema, edgeFilter) {
  return `http://localhost:8080/schemas/${schema}/nodes?edgeFilter=${edgeFilter}`
}

export function basicGraph (schema, node, parentHops, cousinHops, childHops, edgeFilter) {
  return `http://localhost:8080/schemas/${schema}/graph/basic?node=${node}&parentHops=${parentHops}&cousinHops=${cousinHops}&childHops=${childHops}&edgeFilter=${edgeFilter}`
}

export function pathGraph (schema, fromNode, toNode, edgeFilter) {
  return `http://localhost:8080/schemas/${schema}/graph/paths?fromNode=${fromNode}&toNode=${toNode}&edgeFilter=${edgeFilter}`
}

export function nodeProperty (schema, node) {
  return `http://localhost:8080/schemas/${schema}/nodes/${node}`
}

export function edgeProperty (schema, fromNode, toNode) {
  return `http://localhost:8080/schemas/${schema}/edges?fromNode=${fromNode}&toNode=${toNode}`
}
