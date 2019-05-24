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
package org.onap.aai.graphgraph.reader;

import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.NodeProperty;
import org.onap.aai.graphgraph.dto.Property;

import java.util.List;

public interface SchemaReader {
    String getSchemaName();
    List<NodeName> getAllVertexNames(String edgeFilter);
    List<NodeProperty> getVertexProperties(String nodeName);
    List<Property> getEdgeProperties(String fromNode, String toNode, String type);
    Graph getGraph(String initialNode, int parentHops, int cousinHops, int childHops,
        String edgeFilter);
    Graph getGraph(String fromNode, String toNode, String edgeFilter);
}
