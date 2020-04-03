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

import java.util.Collections;
import java.util.List;

public class Graph {
    private List<NodeName> nodeNames;
    private List<Edge> edges;
    private List<List<NodeName>> paths;
    private List<NodeProperty> startNodeProperties;

    public Graph(List<NodeName> nodeNames, List<Edge> edges, List<List<NodeName>> pathsList, List<NodeProperty> startNodeProperties) {
        this.nodeNames = nodeNames;
        this.edges = edges;
        this.paths = pathsList;
        this.startNodeProperties = startNodeProperties;
    }

    public static Graph emptyGraph() {
        return new Graph(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
            Collections.emptyList());
    }

    public List<NodeProperty> getStartNodeProperties() {
        return startNodeProperties;
    }

    public void setStartNodeProperties(List<NodeProperty> startNodeProperties) {
        this.startNodeProperties = startNodeProperties;
    }

    public List<List<NodeName>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<NodeName>> paths) {
        this.paths = paths;
    }

    public List<NodeName> getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(List<NodeName> nodeNames) {
        this.nodeNames = nodeNames;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
