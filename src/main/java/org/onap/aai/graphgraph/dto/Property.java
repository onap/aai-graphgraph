/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
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
