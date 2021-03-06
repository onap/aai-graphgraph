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
package org.onap.aai.graphgraph;

import org.onap.aai.graphgraph.dto.*;
import org.onap.aai.graphgraph.reader.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
public class SchemaResource {

    @Resource
    private SchemaRepository schemaRepository;

    @Autowired
    private ModelExporter modelExporter;

    @Autowired
    private SchemaValidator schemaValidator;

    @GetMapping("/schemas")
    public List<String> loadSchemaNames() {
        return schemaRepository.getAllSchemaNames();
    }

    @GetMapping("/schemas/{schema}/nodes")
    public List<NodeName> loadVertexNames(
            @PathVariable("schema") String schemaName,
            @RequestParam("edgeFilter") String edgeFilter) {
        return schemaRepository.getSchemaReader(schemaName).getAllVertexNames(edgeFilter);
    }

    @GetMapping("/schemas/{schema}/nodes/{node}")
    public List<NodeProperty> loadProperties(
            @PathVariable("schema") String schemaName,
            @PathVariable("node") String node) {
        return schemaRepository.getSchemaReader(schemaName).getVertexProperties(node);
    }

    @GetMapping("/schemas/{schema}/edges")
    public List<Property> loadEdgeProperties(
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNodeName,
            @RequestParam("toNode") String toNodeName) {
        return schemaRepository.getSchemaReader(schemaName).getEdgeProperties(fromNodeName, toNodeName, "edgerule");
    }

    @GetMapping("/schemas/{schema}/graph/basic")
    public Graph loadGraph(
            @PathVariable("schema") String schemaName,
            @RequestParam("node") String initialNodeName,
            @RequestParam("parentHops") Integer parentHops,
            @RequestParam("cousinHops") Integer cousinHops,
            @RequestParam("childHops") Integer childHops,
            @RequestParam("edgeFilter") String edgeFilter) {
        Graph graph = schemaRepository.getSchemaReader(schemaName)
                .getGraph(initialNodeName, parentHops, cousinHops, childHops, edgeFilter);
        graph.setPaths(Collections.emptyList());
        return graph;
    }

    @GetMapping("/schemas/{schema}/graph/paths")
    public Graph loadGraphWithPaths(
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNode,
            @RequestParam("toNode") String toNode,
            @RequestParam("edgeFilter") String edgeFilter) {
        return schemaRepository.getSchemaReader(schemaName).getGraph(fromNode, toNode, edgeFilter);
    }

    @GetMapping("/schemas/{schema}/validation")
    public ValidationProblems validateSchema(
            @PathVariable("schema") String schemaName) {
        return schemaValidator.validate(schemaName);
    }

    @GetMapping(value = "/schemas/{schema}/xmiexport", produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public String exportSchema(
            @PathVariable("schema") String schemaName) {
        return modelExporter.exportModel(schemaName);
    }
}

