
export function schemas () {
  return 'http://localhost:8080/schemas'
}

export function nodeNames (schema) {
  return `http://localhost:8080/schemas/${schema}/nodes`
}

export function basicGraph (schema, node, parentHops, cousinHops, childHops) {
  return `http://localhost:8080/schemas/${schema}/graph/basic?node=${node}&parentHops=${parentHops}&cousinHops=${cousinHops}&childHops=${childHops}`
}

export function pathGraph (schema, fromNode, toNode) {
  return `http://localhost:8080/schemas/${schema}/graph/paths?fromNode=${fromNode}&toNode=${toNode}`
}

export function nodeProperty (schema, node) {
  return `http://localhost:8080/schemas/${schema}/nodes/${node}`
}

export function edgeProperty (schema, fromNode, toNode) {
  return `http://localhost:8080/schemas/${schema}/edges?fromNode=${fromNode}&toNode=${toNode}`
}
