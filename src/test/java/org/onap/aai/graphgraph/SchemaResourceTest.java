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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.aai.edges.EdgeIngestor;
import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.NodeName;
import org.onap.aai.graphgraph.dto.NodeProperty;
import org.onap.aai.graphgraph.dto.ValidationProblems;
import org.onap.aai.introspection.MoxyLoader;
import org.onap.aai.nodes.NodeIngestor;
import org.onap.aai.setup.SchemaVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
public class SchemaResourceTest {

    @Autowired
    private SchemaResource schemaResource;

    @Autowired
    private NodeIngestor nodeIngestor;

    @Autowired
    private EdgeIngestor edgeIngestor;

    @Before
    public void setUp() {
        App.moxyLoaders.put("v10", new MoxyLoader(new SchemaVersion("v10"), nodeIngestor));
        App.edgeIngestor = edgeIngestor;
    }

    @Test
    public void loadSchemaVersionsTest() {
        List<String> schemaNames = schemaResource.loadSchemaNames();
        Assert.assertTrue(schemaNames.contains("v10"));
    }

    @Test
    public void loadGraphBasicTest() {
        Graph graph = schemaResource.loadGraph("v10", "all", 1, 1, 1, "Edgerules");

        Assert.assertNotNull(graph.getEdges());
        Assert.assertEquals(159, graph.getEdges().size());
        Assert.assertTrue(containsNodeName(graph.getNodeNames(), "subnet"));
        Assert.assertTrue(containsNodeName(graph.getNodeNames(), "cloud-region"));
        Assert.assertTrue(containsNodeName(graph.getNodeNames(), "volume-group"));
    }

    @Test
    public void loadGraphPathsTest() {
        Graph graph = schemaResource.loadGraphWithPaths("v10", "cloud-region", "image", "Edgerules");

        Assert.assertNotNull(graph.getEdges());
        Assert.assertEquals(1, graph.getEdges().size());
        Assert.assertTrue(containsNodeName(graph.getNodeNames(), "image"));
        Assert.assertTrue(containsNodeName(graph.getNodeNames(), "cloud-region"));
    }

    @Test
    public void validateSchemaTest() {
        ValidationProblems validationProblems = schemaResource.validateSchema("v10");
        Assert.assertTrue(validationProblems.getProblems().size() > 0);
    }

    @Test
    public void checkNodePropertiesTest() {
        List<NodeProperty> nodeProperties = schemaResource.loadProperties("v10", "cloud-region");
        Assert.assertTrue(containsNodeProperty(nodeProperties, "cloud-owner"));
        Assert.assertTrue(containsNodeProperty(nodeProperties, "cloud-region-id"));
        Assert.assertTrue(containsNodeProperty(nodeProperties, "owner-defined-type"));
    }

    @Test
    public void exportSchemaTest() {
        String schemaExport = schemaResource.exportSchema("v10");
        Assert.assertNotNull(schemaExport);
        Assert.assertEquals(844919, schemaExport.length());
    }

    private boolean containsNodeProperty(List<NodeProperty> nodeProperties, String propertyName) {

        for (NodeProperty nodeProperty : nodeProperties) {
            if (propertyName.equals(nodeProperty.getPropertyName())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNodeName(List<NodeName> nodeNames, String nodeName) {
        for (NodeName name : nodeNames) {
            if(nodeName.equals(name.getId())) {
                return true;
            }
        }
        return false;
    }

}
