package org.onap.aai.graphgraph.reader;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
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
  Graph graph = new TinkerGraph();
  private void init() {
    if (allEntities != null) {
      return;
    }

    allEntities = App.loader.getAllObjects();

    for (Entry<String, Introspector> currentParent : allEntities.entrySet()) {
      addVertex(currentParent.getKey());
      currentParent.getValue().getProperties().stream().filter(v -> allEntities.containsKey(v)).forEach(v -> {
        addVertex(v);
        addParentChildEdge(currentParent.getKey(), v);
      });
    }
  }

  private void addParentChildEdge(String parent, String child) {

    String edgeName = parent + child + PARENT;
    graph.addEdge(edgeName, graph.getVertex(parent), graph.getVertex(child), PARENT);
    graph.addEdge(child + parent + CHILD, graph.getVertex(child), graph.getVertex(parent), CHILD);
  }

  private void addVertex(String vertexId) {
    if (graph.getVertex(vertexId) == null) {
      graph.addVertex(vertexId);
    }
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

  @Override
  public List<Property> getVertexProperties(String nodeName) {
    init();

    if (!allEntities.containsKey(nodeName)) {
      return Collections.EMPTY_LIST;
    }

    return allEntities.get(nodeName).getProperties().stream()
        .filter(p -> !allEntities.containsKey(p))
        .map(p -> new Property(p, ""))
        .collect(Collectors.toList());
  }

  @Override
  public List<Property> getEdgeProperties(String fromNode, String toNode) {
    init();

    return Collections.EMPTY_LIST;
  }

  @Override
  public org.onap.aai.graphgraph.dto.Graph getGraph(String initialNode, int parentHops, int cousinHops, int childHops) {
    init();
    Set<Edge> edges = computeAllEdges(graph.getVertices().iterator().next()); /*TODO check*/

    if (!"all".equals(initialNode)) {
      Set<String> subGraphVertices = computeNodes(graph.getVertex(initialNode), parentHops, CHILD);
      subGraphVertices.addAll(computeNodes(graph.getVertex(initialNode), childHops, PARENT));
      edges = edges.stream().filter(e -> subGraphVertices.contains(e.getSource()) || subGraphVertices.contains(e.getTarget())).collect(
          Collectors.toSet());
    }

    return new org.onap.aai.graphgraph.dto.Graph(new LinkedList<>(computeNodeNames(edges)), new LinkedList<>(edges), Collections.emptyList(), getVertexProperties(initialNode) );
  }

  private Set<NodeName> computeNodeNames(Set<Edge> edges) {
    return edges.stream().flatMap(e -> e.getNodeNames().stream()).collect(
        Collectors.toSet());
  }

  private Set<Edge> computeAllEdges(Vertex initial) {
    Set<Edge> result = new HashSet<>();
    List<Vertex> toQuery = new LinkedList<>();
    toQuery.add(initial);
    final List<Vertex> toVisit = new LinkedList<>();
    Set<String> visited = new HashSet<>();

    while(!toQuery.isEmpty()) {
      for (Vertex v : toQuery) {
        visited.add(getId(v));

        v.getVertices(Direction.OUT, CHILD).forEach(neighbour -> {
          toVisit.add(neighbour);
          result.add(processParentChild().apply(v, neighbour));
        });

        v.getVertices(Direction.OUT, PARENT).forEach(neighbour -> {
          toVisit.add(neighbour);
          result.add(processChildParent().apply(v, neighbour));
        });

      }

      toQuery.clear();
      toQuery.addAll(toVisit.stream().filter(v -> !visited.contains(getId(v))).collect(Collectors.toList()));

    }

    return result;
  }

  private Set<String> computeNodes(Vertex vertex, int hops, String relationshipName) {
    List<Vertex> toQuery = new LinkedList<>();
    toQuery.add(vertex);

    Set<String> visited = new HashSet<>();
    int i = 0;

    final List<Vertex> toVisit = new LinkedList<>();
    while(!toQuery.isEmpty() && hops > i){
      i++;
      toVisit.clear();

      for (Vertex v : toQuery) {
        visited.add(getId(v));
        v.getVertices(Direction.OUT, relationshipName).forEach(toVisit::add);
      }

      toQuery.clear();
      toQuery.addAll(toVisit.stream().filter(v -> !visited.contains(getId(v))).collect(Collectors.toList()));
    }

    return visited;
  }


  /*
  private Set<Edge> computeEdges2(Vertex vertex, int hops, BiFunction<Vertex, Vertex, Edge> createEdge, String relationshipName) {
    Set<Edge> result = new HashSet<>();
    List<Vertex> toQuery = new LinkedList<>();
    toQuery.add(vertex);

    Set<String> visited = new HashSet<>();
    int i = 0;

    final List<Vertex> toVisit = new LinkedList<>();
    while(!toQuery.isEmpty() && hops > i){
      i++;
      toVisit.clear();

      for (Vertex v : toQuery) {
        visited.add(getId(v));
        v.getVertices(Direction.OUT, relationshipName).forEach(neighbour -> {
          toVisit.add(neighbour);
          result.add(createEdge.apply(v, neighbour));
        });
       }

      toQuery.clear();
      toQuery.addAll(toVisit.stream().filter(v -> !visited.contains(getId(v))).collect(Collectors.toList()));
    }

    return result;
  }
*/

  private String getId(Vertex v) {
    return (String) v.getId();
  }


  private BiFunction<Vertex, Vertex, Edge> processChildParent() {
    return (parent, child) -> {
      String parentId = getId(parent);
      String childId = getId(child);
      return new Edge(parentId, childId, Edge.TYPE_PARENT, createTooltip(parentId, childId, Edge.TYPE_PARENT));
    };
  }

  private List<Property> createTooltip(String parentId, String childId, String type) {
    return Arrays.asList(new Property("From", parentId), new Property("To", childId),
        new Property("Type", type));
  }

  private BiFunction<Vertex, Vertex, Edge> processParentChild() {
    return (child, parent) -> {
      String parentId = getId(parent);
      String childId = getId(child);
      return new Edge(parentId, childId, Edge.TYPE_PARENT, createTooltip(parentId, childId, Edge.TYPE_PARENT));
    };
  }
  @Override
  public org.onap.aai.graphgraph.dto.Graph getGraph(String fromNode, String toNode) {
    init();

    return null;
  }
}
