/*
  ============LICENSE_START=======================================================
  org.onap.aai
  ================================================================================
  Copyright © 2019-2020 Orange Intellectual Property. All rights reserved.
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ============LICENSE_END=========================================================
 */
package org.onap.aai.graphgraph.velocity;

import org.eclipse.jetty.util.StringUtil;
import org.onap.aai.graphgraph.dto.Property;

import java.util.UUID;

public class VelocityEntityProperty extends Property {

    private final VelocityEntity entity;
    private final String propertyId;
    private final String description;

    public VelocityEntityProperty(String propertyName, String propertyValue, String propertyDescription, VelocityEntity entity) {
        super(propertyName, propertyValue);
        this.description = propertyDescription;
        this.entity = entity;
        propertyId = VelocityId.getRandomId();
    }

    public String getEntityId() {
        return entity.getId();
    }

    public String getEntityName() {
        return entity.getName();
    }

    public boolean hasDescription() {
        return !StringUtil.isBlank(description);
    }

    public boolean hasEntity() {
        return entity != null;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "VelocityEntityProperty{" + " name=" + getPropertyName() + " description=" + getDescription() + " type=" + getPropertyValue() + '}';
    }

}
