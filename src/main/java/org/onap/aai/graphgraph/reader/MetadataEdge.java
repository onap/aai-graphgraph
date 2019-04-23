package org.onap.aai.graphgraph.reader;

import org.jgrapht.graph.DefaultEdge;

public class MetadataEdge extends DefaultEdge {

  private final String type;
  private final String target;
  private final String source;

  public MetadataEdge(String type, String source, String target) {
    this.source = source;
    this.target = target;
    this.type = type;
  }

  @Override
  public String getTarget() {
    return target;
  }

  @Override
  public String getSource() {
    return source;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "MetadataEdge{" +
        "type='" + type + '\'' +
        ", target='" + target + '\'' +
        ", source='" + source + '\'' +
        '}';
  }
}
