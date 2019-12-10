package org.onap.aai.graphgraph.velocity;

import java.util.UUID;
import org.onap.aai.graphgraph.dto.Property;

public class VelocityEntityProperty extends Property {

  private final VelocityEntity entity;
  private final String propertyId;
  public VelocityEntityProperty(String propertyName, String propertyValue, VelocityEntity entity) {
    super(propertyName, propertyValue);
    this.entity = entity;
    propertyId = entity != null ? entity.getRandomId() : UUID.randomUUID().toString();
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

  public String getPropertyId() {
    return propertyId;
  }

  @Override
  public String toString() {
    return "VelocityEntityProperty{" +
        " name=" + getPropertyName() +
        " type=" + getPropertyValue() +
        '}';
  }
}
