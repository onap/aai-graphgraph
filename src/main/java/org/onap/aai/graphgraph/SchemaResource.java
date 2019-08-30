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
package org.onap.aai.graphgraph;

import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.NodeProperty;
import org.onap.aai.graphgraph.dto.Property;
import org.onap.aai.graphgraph.dto.ValidationProblems;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
public class SchemaResource {

    @Resource
    SchemaRepository repository;

    @RequestMapping("/schemas")
    public List<String> loadSchemaNames()  {
        return repository.getAllSchemaNames();
    }


    @RequestMapping("/schemas/{schema}/nodes")
    public List<NodeName> loadVertexNames(@PathVariable("schema") String schemaName,
        @RequestParam("edgeFilter") String edgeFilter)  {
        return repository.getSchemaReader(schemaName).getAllVertexNames(edgeFilter);
    }

    @RequestMapping("/schemas/{schema}/nodes/{node}")
    public List<NodeProperty> loadProperties(@PathVariable("schema") String schemaName, @PathVariable("node") String node) {
        return repository.getSchemaReader(schemaName).getVertexProperties(node);
    }


    @RequestMapping("/schemas/{schema}/edges")
    public List<Property> loadedgeProperties (
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNodeName,
            @RequestParam("toNode") String toNodeName) {
        return repository.getSchemaReader(schemaName).getEdgeProperties(fromNodeName, toNodeName, "edgerule");
    }


    @RequestMapping("/schemas/{schema}/graph/basic")
    public Graph loadGraph (
            @PathVariable("schema") String schemaName,
            @RequestParam("node") String initialNodeName,
            @RequestParam("parentHops") Integer parentHops,
            @RequestParam("cousinHops") Integer cousinHops,
            @RequestParam("childHops") Integer childHops,
            @RequestParam("edgeFilter") String edgeFilter)
            {
                Graph graph = repository.getSchemaReader(schemaName).getGraph(initialNodeName, parentHops, cousinHops, childHops, edgeFilter);
                graph.setPaths(Collections.emptyList());
                return graph;
    }


    @RequestMapping("/schemas/{schema}/graph/paths")
    public Graph loadGraphWithPaths (
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNode,
            @RequestParam("toNode") String toNode,
            @RequestParam("edgeFilter") String edgeFilter)
            {
        return repository.getSchemaReader(schemaName).getGraph(fromNode, toNode, edgeFilter);
    }

    @RequestMapping("/schemas/{schema}/validation")
    public ValidationProblems validateSchema ( @PathVariable("schema") String schemaName) {
        return new SchemaValidator().validate(schemaName);
    }
}


