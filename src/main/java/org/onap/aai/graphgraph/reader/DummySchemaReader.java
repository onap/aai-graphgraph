package org.onap.aai.graphgraph.reader;

import org.onap.aai.graphgraph.dto.Edge;
import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.Property;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DummySchemaReader implements SchemaReader{

    @Override
    public String getSchemaName() {
        return "dummy";
    }

    @Override
    public List<NodeName> getAllVertexNames() {
        try {
            Parser.parse();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        List<NodeName> names = Parser.getNodes().stream()
                .map(NodeName::new).sorted(Comparator.comparing(NodeName::getId)).collect(Collectors.toList());
        return names;
    }

    @Override
    public List<Property> getVertexProperties(String nodeName) {
        return getProperties("node");
    }

    @Override
    public List<Property> getEdgeProperties(String fromNode, String toNode) {
        return getProperties("edge");
    }

    @Override
    public Graph getGraph(String initialNode, int parentHops, int cousingHops, int childHops) {
        try {
            Parser.parse();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        List<Edge> edges = new LinkedList<>();
        Parser.getNodes();
        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < 20; i++) {
            int v1 = rand.nextInt(Parser.getNodes().size() - 1);
            int v2 = rand.nextInt(Parser.getNodes().size() - 1);

            edges.add(new Edge(Parser.getNodes().get(v1), Parser.getNodes().get(v2), "parent", getProperties("edgeshort")));
        }

        return new Graph( Parser.getNodes().stream().map(NodeName::new).collect(Collectors.toList()), edges, getPaths(), getProperties("node"));
    }

    @Override
    public Graph getGraph(String fromNode, String toNode) {
       return getGraph(fromNode,1,1,1);
    }


    public static List<Property> getProperties(String type) {
        List<Property> result = new LinkedList<>();

        Property p0 = new Property("proptype", type);
        Property p1 = new Property("prop1", "test1");
        Property p2 = new Property("prop2", "test2");
        Property p3 = new Property("random", String.valueOf(new Random(System.currentTimeMillis()).nextInt(1000)));

        result.add(p1);
        result.add(p2);
        result.add(p3);
        result.add(p0);

        return result;
    }

    private List<List<NodeName>> getPaths() {
        List <List<NodeName>> l = new LinkedList<>();
        List <NodeName> l1 = new LinkedList<>();
        List <NodeName> l2 = new LinkedList<>();

        l1.add(new NodeName("action"));
        l1.add(new NodeName("pserver"));
        l1.add(new NodeName("vserver"));
        l1.add(new NodeName("complex"));

        l2.add(new NodeName("XXaction"));
        l2.add(new NodeName("XXaction2"));
        l2.add(new NodeName("XXserver"));
        l2.add(new NodeName("XXservers"));

        l.add(l1);
        l.add(l2);

        return l;
    }
}
