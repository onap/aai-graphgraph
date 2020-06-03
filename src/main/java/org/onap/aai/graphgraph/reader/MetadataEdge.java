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
package org.onap.aai.graphgraph.reader;

import org.jgrapht.graph.DefaultEdge;

public class MetadataEdge extends DefaultEdge {

    private final String type;
    private final String target;
    private final String source;
    private final String label;

    MetadataEdge(String type, String source, String target) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.label = "";
    }

    MetadataEdge(String type, String target, String source, String label) {
        this.type = type;
        this.target = target;
        this.source = source;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MetadataEdge{" +
                "type='" + type + '\'' +
                ", target='" + target + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
