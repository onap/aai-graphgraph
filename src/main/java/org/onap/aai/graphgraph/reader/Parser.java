package org.onap.aai.graphgraph.reader;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import org.onap.aai.graphgraph.dto.Edge;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.onap.aai.graphgraph.reader.DummySchemaReader.getProperties;

public class Parser {

    static Graph g = TinkerGraphFactory.createTinkerGraph();
    static int counter = 10000;
    private static List<String> nodeNames = new ArrayList<>();
    static List <Edge> edges = new LinkedList<>();

    public static List<String> getNodes(){
        Set<String> hs = new HashSet<>();
        hs.addAll(nodeNames);
        nodeNames.clear();
        nodeNames.addAll(hs);
        return nodeNames;
    }


    public static void parse() throws IOException, SAXException, ParserConfigurationException {

        //File fXmlFile = new File("src/main/resources/aai_schema_v11.xsd");
        File fXmlFile = new ClassPathResource("aai_schema_v11.xsd").getFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList elements = doc.getElementsByTagName("xs:element");

        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            handleNode(node, null);
        }
    }

    private static boolean isType(Node c) {
        String type = c.getAttributes().getNamedItem("type").getNodeValue();
        return type.equals("xs:string")
                || type.equals("xs:int")
                || type.equals("xs:boolean")
                || type.equals("xs:unsignedInt");
    }

    static void handleNode(Node c, Node parent){
        if (c.getNodeName().equals("xs:complexType") || c.getNodeName().equals("xs:sequence")){
            for (int i = 0; i < c.getChildNodes().getLength(); i++)
                handleNode(c.getChildNodes().item(i), parent);
            return;
        }

        if(c.getAttributes() == null)
            return;

        if(c.getAttributes().getNamedItem("name") == null && c.getAttributes().getNamedItem("ref") == null)
            return;

        if(c.getAttributes().getNamedItem("type") != null)
            if(isType(c))
                return;

        String vertexId = determineVertexId(c);
        nodeNames.add(vertexId);

        Vertex current;

        if (g.getVertex(vertexId) == null) {
            current = g.addVertex(vertexId);
        }else
            current = g.getVertex(vertexId);

        NodeList childNodes = getChildren(c);
        for (int i = 0; i < childNodes.getLength(); i++)
            handleNode(childNodes.item(i), c);

        if(parent != null) {
            g.addEdge(counter++, g.getVertex(determineVertexId(parent)), current, "parent");
            edges.add(new Edge(g.getVertex(determineVertexId(parent)).getId().toString(), current.getId().toString(), "parent", getProperties("edgeshort")));
        }

    }

    private static NodeList getChildren(Node n) {
        NodeList childNodes = n.getChildNodes();
        return childNodes;
    }

    private static String determineVertexId(Node n) {
        Node nameAttrib = n.getAttributes().getNamedItem("name");
        return nameAttrib != null ? nameAttrib.getNodeValue() : n.getAttributes().getNamedItem("ref").getNodeValue().split(":")[1];
    }

}
