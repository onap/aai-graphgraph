package org.onap.aai.graphgraph.dto;

public class NodeName {
    private String id;

    public NodeName(String name) {
        this.id = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
