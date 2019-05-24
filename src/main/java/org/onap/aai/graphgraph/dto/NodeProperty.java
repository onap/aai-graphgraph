package org.onap.aai.graphgraph.dto;

public class NodeProperty extends Property {
    private String description;
    private String type;
    private boolean key;
    private boolean index;
    private boolean required;

    public NodeProperty(String propertyName,
        String description, String type, boolean key, boolean index, boolean required) {
        super(propertyName, "");
        this.description = description;
        this.type = type;
        this.key = key;
        this.index = index;
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean getIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NodeProperty property = (NodeProperty) o;

        return getPropertyName().equals(property.getPropertyName());
    }

    @Override
    public int hashCode() {
        return  getPropertyName().hashCode();
    }

    @Override
    public int compareTo(Property o) {
        return o.getPropertyName().compareTo(getPropertyName());
    }
}
