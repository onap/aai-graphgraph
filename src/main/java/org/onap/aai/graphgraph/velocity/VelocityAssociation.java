/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2019 Orange Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */
package org.onap.aai.graphgraph.velocity;

public class VelocityAssociation extends VelocityId {

  private final String name;
  private final VelocityEntity fromEntity;
  private final VelocityEntity toEntity;
  private final String fromId = getRandomId();
  private final String toId = getRandomId();
  private final String multiplicity;
  private final boolean isComposition;

  public VelocityAssociation(VelocityEntity fromEntity,
      VelocityEntity toEntity, String name, String multiplicity, boolean isComposition) {
    this.fromEntity = fromEntity;
    this.toEntity = toEntity;
    this.name = name;
    this.multiplicity = multiplicity;
    this.isComposition = isComposition;
  }

  public String getFromEntityName(){
    return fromEntity.getName();
  }

  public String getToEntityName(){
    return toEntity.getName();
  }

  public String getFromEntityId(){
    return fromEntity.getId();
  }

  public String getToEntityId(){
    return toEntity.getId();
  }

  public String getFromId() {
    return fromId;
  }

  public String getToId() {
    return toId;
  }

  public String getName() {
    return name;
  }

  public String getMultiplicity() {
    return multiplicity.toUpperCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VelocityAssociation that = (VelocityAssociation) o;

    if (!name.equals(that.name)) {
      return false;
    }
    if (!fromEntity.equals(that.fromEntity)) {
      return false;
    }
    return toEntity.equals(that.toEntity);
  }

  public boolean getIsComposition() {
    return isComposition;
  }

  @Override
  public String toString() {
    return "VelocityAssociation{" +
        "name='" + name + '\'' +
        ", fromEntity=" + fromEntity +
        ", toEntity=" + toEntity +
        '}';
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + fromEntity.hashCode();
    result = 31 * result + toEntity.hashCode();
    return result;


  }
}
