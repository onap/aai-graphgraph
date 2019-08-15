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

import com.google.common.collect.Multimap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.onap.aai.edges.EdgeRule;
import org.onap.aai.edges.enums.DirectionNotation;
import org.onap.aai.edges.enums.EdgeField;
import org.onap.aai.edges.enums.MultiplicityRule;
import org.onap.aai.edges.exceptions.EdgeRuleNotFoundException;
import org.onap.aai.graphgraph.velocity.VelocityAssociation;
import org.onap.aai.graphgraph.velocity.VelocityEntity;
import org.onap.aai.introspection.Introspector;
import org.onap.aai.setup.SchemaVersion;

public class ModelExporter {

  private static final String AAIMODEL_UML_FILENAME = "aaimodel.uml";
  public static final String VELOCITY_TEMPLATE_FILENAME = "model_export.vm";

  private static Multimap<String, EdgeRule> getEdgeRules(String schemaVersion) {
      try {
        Multimap<String, EdgeRule> allRules = App.edgeIngestor.getAllRules(new SchemaVersion(schemaVersion));
        Map<String, Introspector> allEntities = App.moxyLoaders.get("v16").getAllObjects();
        for (Entry<String, Introspector> currentParent : allEntities.entrySet()) {
          currentParent.getValue().getProperties().stream()
              .filter(v -> allEntities.containsKey(v))
              .filter(v -> !currentParent.getKey().equals(v))
              .forEach(v -> {
                String key = currentParent.getKey() + "|" + v;
                if (!allRules.containsKey(key)) {
                  allRules.put(key, createEdgeRule(currentParent.getKey(), v));
                }
              });
        }
        return allRules;
      } catch (EdgeRuleNotFoundException e) {
        e.printStackTrace();
      }

    return null;
  }

  private static EdgeRule createEdgeRule(String parent, String child) {
    Map<String, String> edgeRuleProps = new HashMap<>();
    edgeRuleProps.put(EdgeField.FROM.toString(), child);
    edgeRuleProps.put(EdgeField.TO.toString(), parent);
    edgeRuleProps.put(EdgeField.DIRECTION.toString(), Direction.OUT.toString()); //TODO check direction
    edgeRuleProps.put(EdgeField.LABEL.toString(), "OXM Parent-Child");
    edgeRuleProps.put(EdgeField.MULTIPLICITY.toString(), MultiplicityRule.MANY2ONE.toString());
    edgeRuleProps.put(EdgeField.DEFAULT.toString(), Boolean.toString(false));
    edgeRuleProps.put(EdgeField.PRIVATE.toString(), Boolean.toString(false));
    edgeRuleProps.put(EdgeField.DELETE_OTHER_V.toString(), DirectionNotation.DIRECTION.toString());
    edgeRuleProps.put(EdgeField.PREVENT_DELETE.toString(), DirectionNotation.DIRECTION.toString());
    edgeRuleProps.put(EdgeField.CONTAINS.toString(), DirectionNotation.DIRECTION.toString());
    edgeRuleProps.put(EdgeField.DESCRIPTION.toString(), "fake edgerule representing parent-child");
    return new EdgeRule(edgeRuleProps);
  }

  static void exportModel(String schemaVersion) {
    Map<String, Introspector> allObjects = App.moxyLoaders.get(schemaVersion).getAllObjects();
    Template t = initVelocity();
    VelocityContext context = populateVelocityContext(schemaVersion, allObjects);
    StringWriter writer = new StringWriter();
    t.merge( context, writer );
    try {
      FileWriter fw = new FileWriter(AAIMODEL_UML_FILENAME);
      fw.write(writer.toString());
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static VelocityContext populateVelocityContext(String schemaVersion,
      Map<String, Introspector> allObjects) {
    VelocityContext context = new VelocityContext();
    Multimap<String, EdgeRule> edgeRules = getEdgeRules(schemaVersion);
    Set<VelocityEntity> entityList = createEntityList(edgeRules);
    Set<VelocityAssociation> associationsList = createVelocityAssociations(entityList, edgeRules);
    updateEntities(entityList, associationsList, allObjects);
    context.put("entityList", entityList);
    context.put("associationList", associationsList);
    return context;
  }

  private static Template initVelocity() {
    VelocityEngine ve = new VelocityEngine();
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    ve.init();
    return ve.getTemplate(VELOCITY_TEMPLATE_FILENAME);
  }

  private static void updateEntities(Set<VelocityEntity> entityList,
      Set<VelocityAssociation> associationsList,
      Map<String, Introspector> allObjects) {
    entityList.forEach(e -> {
      List<VelocityAssociation> associations = associationsList.stream()
          .filter(a -> a.getFromEntityId().equals(e.getId())).collect(
              Collectors.toList());

      entityList.forEach(entity -> entity.setProperties(allObjects.get(entity.getName()).getProperties()));

      e.setNeighbours(associations);
    });
  }

  private static Set<VelocityEntity> createEntityList(
      Multimap<String, EdgeRule> edgeRules) {
    return Objects.requireNonNull(edgeRules).values().stream()
        .flatMap(er -> Stream.of(er.getFrom(), er.getTo()))
        .map(VelocityEntity::new)
        .collect(Collectors.toSet());
  }

  private static Set<VelocityAssociation> createVelocityAssociations(Set<VelocityEntity> entities,
      Multimap<String, EdgeRule> edgeRules) {
    Set<VelocityAssociation> result = new HashSet<>();
      result.addAll(edgeRules.values().stream().map(er ->
          new VelocityAssociation(
              findVelocityEntity(er.getFrom(), entities),
              findVelocityEntity(er.getTo(), entities),
              String.format("%s - %s (label: %s)", er.getFrom(), er.getTo(), er.getLabel()),
              er.getMultiplicityRule().name(),
              er.getLabel().equals("org.onap.relationships.inventory.BelongsTo")
          )).collect(Collectors.toSet()));

    return result;
  }

  private static VelocityEntity findVelocityEntity(String from, Set<VelocityEntity> entities) {
    return entities.stream().filter(e -> e.getName().equals(from)).findFirst().get();
  }
}