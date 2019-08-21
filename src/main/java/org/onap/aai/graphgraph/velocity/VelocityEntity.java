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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class VelocityEntity extends VelocityId {
  private String name;
  private List<VelocityAssociation> neighbours = new LinkedList<>();
  private Set<VelocityEntityProperty> properties;

  public Set<VelocityEntityProperty> getProperties() {
    return properties;
  }

  public void setProperties(Set<VelocityEntityProperty> properties) {
    this.properties = properties;
  }

  public VelocityEntity(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<VelocityAssociation> getNeighbours() {
    return neighbours;
  }

  public void setNeighbours(List<VelocityAssociation> neighbours) {
    this.neighbours = neighbours;
  }

  public void addNeighbours(VelocityAssociation neighbour) {
    neighbours.add(neighbour);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VelocityEntity that = (VelocityEntity) o;

    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "VelocityEntity{" +
        "name='" + name + '\'' +
        '}';
  }
}
