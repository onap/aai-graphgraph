package org.onap.aai.graphgraph.dto;

import java.util.List;

public class Graph {
    private List<NodeName> nodeNames;
    private List<Edge> edges;
    private List<List<NodeName>> paths;
    private List <Property> startNodeProperties;

    public Graph(List<NodeName> nodeNames, List<Edge> edges, List<List<NodeName>> pathsList, List <Property> startNodeProperties) {
        this.nodeNames = nodeNames;
        this.edges = edges;
        this.paths = pathsList;
        this.startNodeProperties = startNodeProperties;
    }

    public List<Property> getStartNodeProperties() {
        return startNodeProperties;
    }

    public void setStartNodeProperties(List<Property> startNodeProperties) {
        this.startNodeProperties = startNodeProperties;
    }

    public List<List<NodeName>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<NodeName>> paths) {
        this.paths = paths;
    }

    public List<NodeName> getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(List<NodeName> nodeNames) {
        this.nodeNames = nodeNames;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
