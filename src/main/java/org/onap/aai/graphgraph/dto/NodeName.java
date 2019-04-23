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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NodeName nodeName = (NodeName) o;

        return id.equals(nodeName.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
