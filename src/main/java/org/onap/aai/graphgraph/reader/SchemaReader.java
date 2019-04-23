package org.onap.aai.graphgraph.reader;

import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.Property;

import java.util.List;

public interface SchemaReader {
    String getSchemaName();
    List<NodeName> getAllVertexNames();
    List<Property> getVertexProperties(String nodeName);
    List<Property> getEdgeProperties(String fromNode, String toNode);
    Graph getGraph(String initialNode, int parentHops, int cousinHops, int childHops);
    Graph getGraph(String fromNode, String toNode);
}
