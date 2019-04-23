package org.onap.aai.graphgraph.dto;

public class Property implements Comparable<Property>{
    private String propertyName;
    private String propertyValue;

    public Property(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Property property = (Property) o;

        if (!propertyName.equals(property.propertyName)) {
            return false;
        }
        return propertyValue.equals(property.propertyValue);

    }

    @Override
    public int hashCode() {
        int result = propertyName.hashCode();
        result = 31 * result + propertyValue.hashCode();
        return result;
    }

    @Override
    public int compareTo(Property o) {
        if (o.getPropertyName().equals(getPropertyName()) && o.getPropertyValue() != null
            && getPropertyValue() != null) {
            return getPropertyValue().compareTo(o.getPropertyValue());
        }

        return propertyName.compareTo(o.getPropertyName());
    }
}
