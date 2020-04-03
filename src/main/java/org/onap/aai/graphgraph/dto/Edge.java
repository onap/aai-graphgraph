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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Edge {

    private String source;
    private String target;
    private String type;
    private List<Property> tooltipProperties;

    public Edge(String source, String target, String type, List<Property> tooltipProperties) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.tooltipProperties = tooltipProperties;
    }

    public List<Property> getTooltipProperties() {
        return tooltipProperties;
    }

    public void setTooltipProperties(List<Property> tooltipProperties) {
        this.tooltipProperties = tooltipProperties;
    }

    public String getSource() {
        return source;
    }

    public List<NodeName> getNodeNames() {
        return Arrays.asList(new NodeName(source), new NodeName(target));
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edge edge = (Edge) o;

        if (!source.equals(edge.source)) {
            return false;
        }
        if (!target.equals(edge.target)) {
            return false;
        }
        return type.equals(edge.type);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
