package org.onap.aai.graphgraph.reader;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.onap.aai.graphgraph.App;
import org.onap.aai.graphgraph.dto.Edge;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.Property;
import org.onap.aai.introspection.Introspector;

public class BasicSchemaReader implements SchemaReader {

  private static final String PARENT = "parent";
  private static final String CHILD = "child";
  private static final String EDGERULES = "edgerules";

  private Map<String, Introspector> allEntities;
  Graph<String, MetadataEdge> graph = new DefaultDirectedGraph<>(MetadataEdge.class);
  private void init() {
    if (allEntities != null) {
      return;
    }

    allEntities = App.loader.getAllObjects();
    graph = createGraph();


  }

  private Graph<String, MetadataEdge> createGraph() {
    Graph<String, MetadataEdge> graph = new DefaultDirectedGraph<>(MetadataEdge.class);
    for (Entry<String, Introspector> currentParent : allEntities.entrySet()) {
      graph.addVertex(currentParent.getKey());
      currentParent.getValue().getProperties().stream()
          .filter(v -> allEntities.containsKey(v))
          .filter(v -> !currentParent.getKey().equals(v))
          .forEach(v -> {
            graph.addVertex(v);
            addParentChildEdge(currentParent.getKey(), v, graph);
          });
    }
    return graph;
  }

  private void addParentChildEdge(String parent, String child, Graph<String, MetadataEdge> graph) {
    graph.addEdge(parent, child, new MetadataEdge(PARENT, parent, child));
    graph.addEdge(child, parent, new MetadataEdge(CHILD, child, parent));
  }

  @Override
  public String getSchemaName() {
    return "v12";
  }

  @Override
  public List<NodeName> getAllVertexNames() {
    init();

   return allEntities.keySet().stream().sorted()
        .map(NodeName::new).collect(
            Collectors.toList());
  }

  //TODO not working, for example for "nodes" node
  @Override
  public List<Property> getVertexProperties(String nodeName) {
    init();

    if (!allEntities.containsKey(nodeName)) {
      return Collections.emptyList();
    }

    List<Property> neighbours = graph.incomingEdgesOf(nodeName).stream()
        .map(e -> e.getSource())
        .map(t -> new Property("(neighbour node)", t))
        .collect(Collectors.toList());

    neighbours.addAll(graph.outgoingEdgesOf(nodeName).stream()
        .map(e -> e.getTarget())
        .map(t -> new Property("(neighbour node)", t))
        .collect(Collectors.toList()));

    List<Property> properties = allEntities.get(nodeName).getProperties().stream()
        .filter(p -> !allEntities.containsKey(p))
        .map(p -> new Property(p, ""))
        .collect(Collectors.toList());

    properties.addAll(neighbours);
    return properties;
  }

  @Override
  public List<Property> getEdgeProperties(String fromNode, String toNode) {
    init();

    return Collections.EMPTY_LIST;
  }

  @Override
  public org.onap.aai.graphgraph.dto.Graph getGraph(String initialNode, int parentHops, int cousinHops, int childHops) {
    init();
    Set<Edge> edges = computeAllEdges("action"); /*TODO check action*/

    if (!"all".equals(initialNode)) {
      Set<String> subGraphVertices = computeNodes(initialNode, parentHops, CHILD);
      subGraphVertices.addAll(computeNodes(initialNode, childHops, PARENT));
      edges = edges.stream().filter(e -> subGraphVertices.contains(e.getSource()) || subGraphVertices.contains(e.getTarget())).collect(
          Collectors.toSet());
    }

    return new org.onap.aai.graphgraph.dto.Graph(new LinkedList<>(computeNodeNames(edges)), new LinkedList<>(edges), Collections.emptyList(), getVertexProperties(initialNode) );
  }

  private Set<NodeName> computeNodeNames(Set<Edge> edges) {
    return edges.stream().flatMap(e -> e.getNodeNames().stream()).collect(
        Collectors.toSet());
  }

  private Set<Edge> computeAllEdges(String initial) {
    Set<Edge> result = new HashSet<>();
    List<String> toQuery = new LinkedList<>();
    toQuery.add(initial);
    final List<String> toVisit = new LinkedList<>();
    Set<String> visited = new HashSet<>();

    while (!toQuery.isEmpty()){
      for (String v : toQuery) {
        visited.add(v);

        graph.edgesOf(v).forEach(edge -> {
          String neighbour = edge.getTarget();
          toVisit.add(neighbour);
          switch (edge.getType()) {
            case CHILD:
              result.add(new Edge(neighbour, edge.getSource(), Edge.TYPE_PARENT,
                  createTooltip(neighbour, edge.getSource(), Edge.TYPE_PARENT)));
              break;
            /*case PARENT:
              result.add(new Edge(edge.getSource(), neighbour, Edge.TYPE_PARENT,
                  createTooltip(edge.getSource(), neighbour, Edge.TYPE_PARENT)));*/
          }
        });
      }
        toQuery.clear();
        toQuery.addAll(
            toVisit.stream().filter(s -> !visited.contains(s)).collect(Collectors.toList()));
      }

      return result;

  }

  private Set<String> computeNodes(String vertex, int hops, String relationshipName) {
    List<String> toQuery = new LinkedList<>();
    toQuery.add(vertex);

    Set<String> visited = new HashSet<>();
    int i = 0;

    final List<String> toVisit = new LinkedList<>();
    while(!toQuery.isEmpty() && hops > i){
      i++;
      toVisit.clear();

      for (String v : toQuery) {
        visited.add(v);
        graph.edgesOf(v).stream().filter(e -> e.getType().equals(relationshipName)).map(
            MetadataEdge::getTarget).forEach(toVisit::add);
      }

      toQuery.clear();
      toQuery.addAll(toVisit.stream().filter(v -> !visited.contains(v)).collect(Collectors.toList()));
    }

    return visited;
  }


  @Override
  public org.onap.aai.graphgraph.dto.Graph getGraph(String fromNode, String toNode) {
    init();
    Graph<String, MetadataEdge> tempGraph = createGraph();
    List<List<NodeName>> paths = new LinkedList<>();
    FloydWarshallShortestPaths <String, MetadataEdge> shortestPaths;

    while(true){
      shortestPaths = new FloydWarshallShortestPaths<>(tempGraph);
      GraphPath<String, MetadataEdge> p = shortestPaths.getPath(fromNode, toNode);
      if(p == null || p.getEdgeList() == null || p.getEdgeList().isEmpty())
        break;
      paths.add(p.getEdgeList().stream().map(e -> new NodeName(e.getSource())).collect(Collectors.toList()));
      tempGraph.removeEdge(p.getEdgeList().get(p.getLength() - 1));
    }



    return null;
  }

  private List<Property> createTooltip(String target, String v, String type)  {
    return Arrays.asList(new Property("From", target), new Property("To", v),
        new Property("Type", type));
  }
  }
