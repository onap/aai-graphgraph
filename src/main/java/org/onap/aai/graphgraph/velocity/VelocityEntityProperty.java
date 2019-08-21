package org.onap.aai.graphgraph.velocity;

import org.onap.aai.graphgraph.dto.Property;

public class VelocityEntityProperty extends Property {

  private final VelocityEntity entity;

  public VelocityEntityProperty(String propertyName, String propertyValue, VelocityEntity entity) {
    super(propertyName, propertyValue);
    this.entity = entity;
  }

  public String getEntityId() {
    return entity.getId();
  }

  public String getEntityName() {
    return entity.getName();
  }

  public boolean hasEntity(){
    return entity != null;
  }

  @Override
  public String toString() {
    return "VelocityEntityProperty{" +
        " name=" + getPropertyName() +
        " type=" + getPropertyValue() +
        '}';
  }
}
