package org.onap.aai.graphgraph;

import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.Property;
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
    public List<NodeName> loadVertexNames(@PathVariable("schema") String schemaName)  {
        return repository.getSchemaReader(schemaName).getAllVertexNames();
    }

    @RequestMapping("/schemas/{schema}/nodes/{node}")
    public List<Property> loadProperties(@PathVariable("schema") String schemaName, @PathVariable("node") String node) {
        return repository.getSchemaReader(schemaName).getVertexProperties(node);
    }


    @RequestMapping("/schemas/{schema}/edges")
    public List<Property> loadedgeProperties(
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNodeName,
            @RequestParam("toNode") String toNodeName) {
        return repository.getSchemaReader(schemaName).getEdgeProperties(fromNodeName, toNodeName);
    }


    @RequestMapping("/schemas/{schema}/graph/basic")
    public Graph loadGraph(
            @PathVariable("schema") String schemaName,
            @RequestParam("node") String initialNodeName,
            @RequestParam("parentHops") Integer parentHops,
            @RequestParam("cousinHops") Integer cousinHops,
            @RequestParam("childHops") Integer childHops)
            {
                Graph graph = repository.getSchemaReader(schemaName).getGraph(initialNodeName, parentHops, cousinHops, childHops);
                graph.setPaths(Collections.emptyList());
                return graph;
    }


    @RequestMapping("/schemas/{schema}/graph/paths")
    public Graph loadGraphWithPaths(
            @PathVariable("schema") String schemaName,
            @RequestParam("fromNode") String fromNode,
            @RequestParam("toNode") String toNode)
            {
        return repository.getSchemaReader(schemaName).getGraph(fromNode, toNode);
    }


}


