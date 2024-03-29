/*
  ============LICENSE_START=======================================================
  org.onap.aai
  ================================================================================
  Copyright © 2019-2020 Orange Intellectual Property. All rights reserved.
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ============LICENSE_END=========================================================
 */
package org.onap.aai.graphgraph.reader;

import com.google.common.collect.Multimap;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.onap.aai.edges.EdgeIngestor;
import org.onap.aai.edges.EdgeRule;
import org.onap.aai.edges.exceptions.EdgeRuleNotFoundException;
import org.onap.aai.graphgraph.MoxyLoaderRepository;
import org.onap.aai.graphgraph.dto.Edge;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.NodeProperty;
import org.onap.aai.graphgraph.dto.Property;
import org.onap.aai.introspection.Introspector;
import org.onap.aai.schema.enums.PropertyMetadata;
import org.onap.aai.setup.SchemaVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicSchemaReader implements SchemaReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicSchemaReader.class);

    private Map<String, Introspector> allEntities;
    private Graph<String, MetadataEdge> graph = new DefaultDirectedGraph<>(MetadataEdge.class);
    private EdgeIngestor edgeIngestor;
    private final String version;
    private final List<String> schemaErrors = new LinkedList<>();
    private final MoxyLoaderRepository moxyLoaderRepository;

    public BasicSchemaReader(String version, MoxyLoaderRepository moxyLoaderRepository, EdgeIngestor edgeIngestor) {
        this.version = version;
        this.moxyLoaderRepository = moxyLoaderRepository;
        this.edgeIngestor = edgeIngestor;
    }

    public List<String> getSchemaErrors() {
        return schemaErrors;
    }

    private void initAllEntitiesAndCreateGraph() {
        if (allEntities != null) {
            return;
        }

        try {
            allEntities = moxyLoaderRepository.getMoxyLoaders().get(getSchemaName()).getAllObjects();
            graph = createGraph(true, true);

        } catch (Exception e) {
            LOGGER.error("Failed creation of BasicSchemaReader, version: " + getSchemaName(), e);
        }
    }

    private Graph<String, MetadataEdge> createGraph(boolean withParentChild, boolean withEdgeRules) {
        Graph<String, MetadataEdge> directedGraph = new DefaultDirectedGraph<>(MetadataEdge.class);
        for (Entry<String, Introspector> currentParent : allEntities.entrySet()) {
            directedGraph.addVertex(currentParent.getKey());
            currentParent.getValue().getProperties().stream()
                    .filter(v -> allEntities.containsKey(v))
                    .filter(v -> !currentParent.getKey().equals(v))
                    .forEach(v -> {
                        directedGraph.addVertex(v);
                        if (withParentChild) {
                            addParentChildEdge(currentParent.getKey(), v, directedGraph);
                        }
                    });
        }

        if (!withEdgeRules) {
            return directedGraph;
        }

        Multimap<String, EdgeRule> allRules = null;
        try {
            allRules = edgeIngestor.getAllRules(new SchemaVersion(getSchemaName()));

        } catch (EdgeRuleNotFoundException e) {
            LOGGER.error("Edge rule not found", e);
        }

        Objects.requireNonNull(allRules).asMap().values().stream()
                .flatMap(Collection::stream)
                .forEach(e -> {
                    switch (e.getDirection()) {
                        case OUT:
                            addEdgerule(e.getFrom(), e.getTo(), e.getLabel(), directedGraph);
                            break;
                        case IN:
                            addEdgerule(e.getTo(), e.getFrom(), e.getLabel(), directedGraph);
                            break;
                        case BOTH:
                            addEdgerule(e.getFrom(), e.getTo(), e.getLabel(), directedGraph);
                            addEdgerule(e.getTo(), e.getFrom(), e.getLabel(), directedGraph);
                            break;
                    }
                });

        return directedGraph;
    }

    private void addEdgerule(String parent, String child, String label, Graph<String, MetadataEdge> graph) {
        //shortening labels, long edge names are unreadable in the UI
        if (label.contains(".")) {
            String[] split = label.split("\\.");
            label = split[split.length - 1];
        }
        checkVertexExist(graph, parent);
        checkVertexExist(graph, child);

        graph.addEdge(child, parent,
                new MetadataEdge(EdgeType.EDGE_RULE.getTypeName(), child, parent, label));
    }

    private void checkVertexExist(Graph<String, MetadataEdge> graph, String vertex) {
        if (!graph.vertexSet().contains(vertex)) {
            graph.addVertex(vertex);
            schemaErrors.add(String.format("Schema is inconsistent, missing node %s", vertex));
        }
    }

    private void addParentChildEdge(String parent, String child, Graph<String, MetadataEdge> graph) {
        graph.addEdge(parent, child,
                new MetadataEdge(EdgeType.PARENT.getTypeName(), parent, child, EdgeType.PARENT.getTypeName()));
        graph.addEdge(child, parent,
                new MetadataEdge(EdgeType.CHILD.getTypeName(), child, parent, EdgeType.CHILD.getTypeName()));
    }

    @Override
    public String getSchemaName() {
        return version;
    }

    @Override
    public List<NodeName> getAllVertexNames(String edgeFilter) {
        initAllEntitiesAndCreateGraph();

        return createGraph(
                isParentChildFilter(edgeFilter),
                isEdgeRulesFilter(edgeFilter)).edgeSet().stream()
                .flatMap(e -> Stream.of(e.getSource(), e.getTarget()))
                .sorted().distinct()
                .map(NodeName::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<NodeProperty> getVertexProperties(String nodeName) {
        initAllEntitiesAndCreateGraph();
        if (!allEntities.containsKey(nodeName)) {
            return Collections.emptyList();
        }
        Introspector introspector = allEntities.get(nodeName);
        List<String> properties = introspector.getProperties().stream().sorted().collect(Collectors.toList());

        return properties.stream()
                .map(p -> new NodeProperty(
                        p,
                        introspector.getPropertyMetadata(p).getOrDefault(
                                PropertyMetadata.DESCRIPTION, "no description available"),
                        introspector.getType(p),
                        introspector.getAllKeys().contains(p),
                        introspector.getIndexedProperties().contains(p),
                        introspector.getRequiredProperties().contains(p)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> getEdgeProperties(String fromNode, String toNode, String type) {
        initAllEntitiesAndCreateGraph();
        if (type.equals(EdgeType.EDGE_RULE.getTypeName())) {
            try {
                List<EdgeRule> rules = edgeIngestor.getAllRules(new SchemaVersion(getSchemaName()))
                        .asMap().values().stream()
                        .flatMap(Collection::stream)
                        .filter(identifyEdgeRule(fromNode, toNode))
                        .collect(Collectors.toList());

                Optional<List<Property>> properties = rules.stream().map(this::edgeRuleProperties).findFirst();
                return properties.orElse(Collections.emptyList());

            } catch (EdgeRuleNotFoundException e) {
                LOGGER.error("Edge rule not found", e);
            }
        }
        return Collections.emptyList();
    }

    private Predicate<EdgeRule> identifyEdgeRule(String fromNode, String toNode) {
        return e -> {
            switch (e.getDirection()) {
                case OUT:
                    return e.getFrom().equals(fromNode) && e.getTo().equals(toNode);
                case IN:
                    return e.getFrom().equals(toNode) && e.getTo().equals(fromNode);
                case BOTH:
                    return e.getFrom().equals(toNode) && e.getTo().equals(fromNode)
                            || e.getFrom().equals(fromNode) && e.getTo().equals(toNode);
                default:
                    return false;
            }
        };
    }

    private List<Property> edgeRuleProperties(EdgeRule r) {
        List<Property> ps = new LinkedList<>();
        ps.add(new Property("Multiplicity", r.getMultiplicityRule().name()));
        ps.add(new Property("Is default edge", String.valueOf(r.isDefault())));
        ps.add(new Property("Description", r.getDescription()));
        ps.add(new Property("Is private edge", String.valueOf(r.isPrivateEdge())));
        ps.add(new Property("Contains", r.getContains()));
        ps.add(new Property("Prevent delete", r.getPreventDelete()));
        ps.add(new Property("Label", r.getLabel()));
        ps.add(new Property("Delete other v", r.getDeleteOtherV()));
        return ps;
    }

    @Override
    public org.onap.aai.graphgraph.dto.Graph getGraph(
            String initialNode, int parentHops, int cousinHops, int childHops, String edgeFilter) {
        initAllEntitiesAndCreateGraph();

        Optional<String> anyVertex = graph.vertexSet().stream().findFirst();
        if (!anyVertex.isPresent()) {
            return org.onap.aai.graphgraph.dto.Graph.emptyGraph();
        }
        Set<Edge> edges = computeAllEdges(
                anyVertex.get(), isParentChildFilter(edgeFilter), isEdgeRulesFilter(edgeFilter));

        if (!"all".equals(initialNode)) {
            Set<String> subGraphVertices = computeNodes(initialNode, parentHops, EdgeType.CHILD.getTypeName());
            subGraphVertices.addAll(computeNodes(initialNode, childHops, EdgeType.PARENT.getTypeName()));
            subGraphVertices.addAll(computeNodes(initialNode, cousinHops, EdgeType.EDGE_RULE.getTypeName()));
            edges = filterEdges(edges, subGraphVertices);
        }

        return new org.onap.aai.graphgraph.dto.Graph(new LinkedList<>(computeNodeNames(edges)),
                new LinkedList<>(edges), Collections.emptyList(), getVertexProperties(initialNode));
    }

    private boolean isParentChildFilter(String edgeFilter) {
        return "Parents".equals(edgeFilter);
    }

    private boolean isEdgeRulesFilter(String edgeFilter) {
        return "Edgerules".equals(edgeFilter);
    }

    private Set<Edge> filterEdgesStrict(Set<Edge> edges, Set<String> subGraphVertices) {
        return edges.stream()
                .filter(e -> subGraphVertices.contains(e.getSource()) && subGraphVertices.contains(e.getTarget()))
                .collect(Collectors.toSet());
    }

    private Set<Edge> filterEdges(Set<Edge> edges, Set<String> subGraphVertices) {
        return edges.stream()
                .filter(e -> subGraphVertices.contains(e.getSource()) || subGraphVertices.contains(e.getTarget()))
                .filter(e -> !e.getType().equals(EdgeType.EDGE_RULE.getTypeName()))
                .collect(Collectors.toSet());
    }

    private Set<NodeName> computeNodeNames(Set<Edge> edges) {
        return edges.stream()
                .flatMap(e -> e.getNodeNames().stream())
                .collect(Collectors.toSet());
    }

    private Set<Edge> computeAllEdges(String initial, boolean parentChild, boolean edgeRules) {
        Set<Edge> result = new HashSet<>();
        List<String> toQuery = new LinkedList<>();
        toQuery.add(initial);
        final List<String> toVisit = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        while (!toQuery.isEmpty()) {
            for (String v : toQuery) {
                visited.add(v);

                graph.edgesOf(v).forEach(edge -> {
                    String neighbour = edge.getTarget();
                    toVisit.add(neighbour);

                    if (EdgeType.CHILD.isType(edge.getType()) && parentChild) {
                        result.add(new Edge(
                                neighbour, edge.getSource(), EdgeType.PARENT.getTypeName(), createTooltip(
                                neighbour, edge.getSource(), EdgeType.PARENT.getTypeName(), edge.getLabel())));
                    }

                    if (EdgeType.EDGE_RULE.isType(edge.getType()) && edgeRules) {
                        result.add(new Edge(
                                edge.getSource(), neighbour, edge.getLabel(), createTooltip(
                                neighbour, edge.getSource(), EdgeType.EDGE_RULE.getTypeName(), edge.getLabel())));
                    }
                });
            }
            toQuery.clear();
            toQuery.addAll(toVisit.stream().filter(s -> !visited.contains(s)).collect(Collectors.toList()));
        }

        return result;

    }

    private Set<String> computeNodes(String vertex, int hops, String relationshipName) {
        List<String> toQuery = new LinkedList<>();
        toQuery.add(vertex);

        Set<String> visited = new HashSet<>();
        int i = 0;

        final List<String> toVisit = new LinkedList<>();
        while (!toQuery.isEmpty() && hops > i) {
            i++;
            toVisit.clear();

            for (String v : toQuery) {
                visited.add(v);
                graph.edgesOf(v).stream()
                        .filter(e -> e.getType().equals(relationshipName))
                        .map(MetadataEdge::getTarget)
                        .forEach(toVisit::add);
            }

            toQuery.clear();
            toQuery.addAll(toVisit.stream().filter(v -> !visited.contains(v)).collect(Collectors.toList()));
        }

        return visited;
    }

    @Override
    public org.onap.aai.graphgraph.dto.Graph getGraph(String fromNode, String toNode, String edgeFilter) {
        initAllEntitiesAndCreateGraph();
        Graph<String, MetadataEdge> tempGraph = createGraph(
                isParentChildFilter(edgeFilter), isEdgeRulesFilter(edgeFilter));
        List<List<NodeName>> paths = new LinkedList<>();
        FloydWarshallShortestPaths<String, MetadataEdge> shortestPaths;

        while (true) {
            shortestPaths = new FloydWarshallShortestPaths<>(tempGraph);
            GraphPath<String, MetadataEdge> p = shortestPaths.getPath(fromNode, toNode);
            if (p == null || p.getEdgeList() == null || p.getEdgeList().isEmpty()) {
                break;
            }
            String previous = fromNode;
            List<NodeName> path = new LinkedList<>();
            for (MetadataEdge e : p.getEdgeList()) {
                if (e.getTarget().equals(previous)) {
                    previous = e.getSource();
                    path.add(new NodeName(e.getTarget()));
                } else {
                    previous = e.getTarget();
                    path.add(new NodeName(e.getSource()));
                }
            }
            path.add(new NodeName(previous));
            paths.add(path);
            tempGraph.removeEdge(p.getEdgeList().get(p.getLength() - 1)); //remove last edge from path
        }

        Set<Edge> edges = computeAllEdges(fromNode, isParentChildFilter(edgeFilter), isEdgeRulesFilter(edgeFilter));
        edges = filterEdgesStrict(
                edges,
                paths.stream()
                        .flatMap(Collection::stream)
                        .map(NodeName::getId)
                        .collect(Collectors.toSet()));
        return new org.onap.aai.graphgraph.dto.Graph(
                new LinkedList<>(computeNodeNames(edges)), new LinkedList<>(edges),
                paths, getVertexProperties(fromNode));
    }

    private List<Property> createTooltip(String target, String v, String type, String label) {
        List<Property> properties = new LinkedList<>();
        properties.add(new Property("From", target));
        properties.add(new Property("To", v));
        properties.add(new Property("Type", type));
        properties.add(new Property("Relationship", label));
        properties.addAll(getEdgeProperties(
                v, target,
                EdgeType.EDGE_RULE.isType(type) ? EdgeType.EDGE_RULE.getTypeName() : EdgeType.PARENT.getTypeName()));
        return properties;
    }
}
