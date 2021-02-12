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

import com.google.common.collect.Multimap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.EscapeTool;
import org.eclipse.jetty.util.StringUtil;
import org.onap.aai.edges.EdgeRule;
import org.onap.aai.edges.enums.DirectionNotation;
import org.onap.aai.edges.enums.EdgeField;
import org.onap.aai.edges.enums.MultiplicityRule;
import org.onap.aai.edges.exceptions.EdgeRuleNotFoundException;
import org.onap.aai.graphgraph.velocity.VelocityAssociation;
import org.onap.aai.graphgraph.velocity.VelocityEntity;
import org.onap.aai.graphgraph.velocity.VelocityEntityProperty;
import org.onap.aai.introspection.Introspector;
import org.onap.aai.schema.enums.ObjectMetadata;
import org.onap.aai.schema.enums.PropertyMetadata;
import org.onap.aai.setup.SchemaVersion;

public class ModelExporter {

    private static final String AAIMODEL_UML_FILENAME = "aaimodel.uml";
    private static final String VELOCITY_TEMPLATE_FILENAME = "model_export.vm";
    private static final boolean OXM_ENABLED = false;
    private static final String camelCaseRegex = "(?=[A-Z][a-z])";
    private static Map<String, Introspector> allEntities;

    private static Multimap<String, EdgeRule> getEdgeRules(String schemaVersion) {
        try {
            Multimap<String, EdgeRule> allRules = App.edgeIngestor.getAllRules(new SchemaVersion(schemaVersion));
            allEntities = App.moxyLoaders.get(schemaVersion).getAllObjects();
            if (OXM_ENABLED) {
                addOxmRelationships(allRules, allEntities);
            }
            return allRules;
        } catch (EdgeRuleNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void addOxmRelationships(
            Multimap<String, EdgeRule> allRules,
            Map<String, Introspector> allEntities
    ) {
        for (Entry<String, Introspector> currentParent : allEntities.entrySet()) {
            currentParent.getValue().getProperties().stream()
                    .filter(allEntities::containsKey)
                    .filter(v -> !currentParent.getKey().equals(v))
                    .forEach(v -> {
                        String key = currentParent.getKey() + "|" + v;
                        if (!allRules.containsKey(key)) {
                            allRules.put(key, createEdgeRule(currentParent.getKey(), v));
                        }
                    });
        }
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

    static String exportModel(String schemaVersion) {
        Map<String, Introspector> allObjects = App.moxyLoaders.get(schemaVersion).getAllObjects();
        Template t = initVelocity();
        VelocityContext context = populateVelocityContext(schemaVersion, allObjects);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }

    static void writeExportedModel(String result) {
        try {
            FileWriter fw = new FileWriter(AAIMODEL_UML_FILENAME);
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static VelocityContext populateVelocityContext(
            String schemaVersion,
            Map<String, Introspector> allObjects
    ) {
        VelocityContext context = new VelocityContext();
        Multimap<String, EdgeRule> edgeRules = getEdgeRules(schemaVersion);
        Set<VelocityEntity> entityList = createEntityList(edgeRules);
        Set<VelocityAssociation> associationsList = createVelocityAssociations(
                entityList, Objects.requireNonNull(edgeRules));
        updateEntities(entityList, associationsList, allObjects);
        context.put("entityList", entityList);
        context.put("associationList", associationsList);
        context.put("esc", new EscapeTool());
        return context;
    }

    private static Template initVelocity() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        return ve.getTemplate(VELOCITY_TEMPLATE_FILENAME);
    }

    private static void updateEntities(
            Set<VelocityEntity> entityList,
            Set<VelocityAssociation> associationsList,
            Map<String, Introspector> allObjects
    ) {
        entityList.forEach(e -> {
            List<VelocityAssociation> associations = associationsList.stream()
                    .filter(a -> a.getFromEntityId().equals(e.getId())).collect(
                            Collectors.toList());
            updateNeighbour(entityList, associations);
            String description = allEntities.get(e.getName()).getMetadata(ObjectMetadata.DESCRIPTION);
            e.setDescription(StringUtil.isBlank(description) ? "no description is available" : description);
        });

        entityList.forEach(
                e -> e.setProperties(getPropertiesForEntity(allObjects.get(e.getName()), entityList)));
    }

    private static void updateNeighbour(
            Set<VelocityEntity> entityList, List<VelocityAssociation> associations
    ) {
        associations.forEach(ass -> {
            Optional<VelocityEntity> velocityEntity = entityList.stream()
                    .filter(e -> e.getId().equals(ass.getToEntityId())).findFirst();
            velocityEntity.ifPresent(entity -> entity.addNeighbours(ass));
        });
    }

    private static Set<VelocityEntityProperty> getPropertiesForEntity(
            Introspector introspector,
            Set<VelocityEntity> entityList) {
        return introspector.getProperties().stream()
                .map(p -> new VelocityEntityProperty(
                        p,
                        introspector.getType(p),
                        introspector.getPropertyMetadata(p).get(PropertyMetadata.DESCRIPTION),
                        findVelocityEntity(introspector.getType(p), entityList)))
                .collect(
                        Collectors.toSet());
    }

    private static Set<VelocityEntity> createEntityList(
            Multimap<String, EdgeRule> edgeRules) {
        return Objects.requireNonNull(edgeRules).values().stream()
                .flatMap(er -> Stream.of(er.getFrom(), er.getTo()))
                .map(VelocityEntity::new)
                .collect(Collectors.toSet());
    }

    private static Set<VelocityAssociation> createVelocityAssociations(
            Set<VelocityEntity> entities,
            Multimap<String, EdgeRule> edgeRules) {
        return edgeRules.values().stream().flatMap(er -> {
            VelocityAssociation out = createVelocityAssociation(entities, er.getFrom(), er.getTo(),
                    er.getLabel(), er.getDescription(), er.getMultiplicityRule().name(), er.getContains());
            VelocityAssociation in = createVelocityAssociation(entities, er.getTo(), er.getFrom(),
                    er.getLabel(), er.getDescription(), er.getMultiplicityRule().name(), er.getContains());
            switch (er.getDirection()) {
                case OUT:
                    return Stream.of(out);
                case IN:
                    return Stream.of(in);
                case BOTH:
                    return Stream.of(out, in);
                default:
                    return null;
            }
        }).collect(Collectors.toSet());
    }

    private static VelocityAssociation createVelocityAssociation(
            Set<VelocityEntity> entities, String from, String to, String label, String description, String multiplicity, String contains) {
        Optional<VelocityEntity> fromEntity = entities.stream()
                .filter(ent -> ent.getName().equals(from)).findFirst();
        Optional<VelocityEntity> toEntity = entities.stream()
                .filter(ent -> ent.getName().equals(to)).findFirst();
        if (fromEntity.isPresent() && toEntity.isPresent()) {
            switch (contains) {
                case "IN":
                    return new VelocityAssociation(
                            fromEntity.get(),
                            toEntity.get(),
                            String.format("%s - %s (%s)", from, to, shortenLabel(label)),
                            description,
                            multiplicity,
                            true);
                case "OUT":
                    return new VelocityAssociation(
                            toEntity.get(),
                            fromEntity.get(),
                            String.format("%s - %s (%s)", to, from, shortenLabel(label)),
                            description,
                            multiplicity.equals("ONE2MANY") ? "MANY2ONE" : multiplicity,
                            true);
                default:
                    return new VelocityAssociation(
                            fromEntity.get(),
                            toEntity.get(),
                            String.format("%s - %s (%s)", from, to, shortenLabel(label)),
                            description,
                            multiplicity,
                            false);
            }
        }
        return null;
    }

    private static String shortenLabel(String label) {
        if (label.contains(".")) {
            String[] split = label.split("\\.");
            return split[split.length - 1];
        }

        return label;
    }

    private static VelocityEntity findVelocityEntity(String entityName, Set<VelocityEntity> entities) {
        if (entityName.startsWith("java.lang")) {
            return null;
        }

        if (!entityName.startsWith("inventory.aai.onap.org")) {
            return null;
        }

        String[] split = entityName.split("\\.");
        String entityNameRoot = split[split.length - 1];
        final Pattern pattern = Pattern.compile(camelCaseRegex);
        final Matcher matcher = pattern.matcher(entityNameRoot.substring(1));
        String finalEntityNameRoot = (entityNameRoot.charAt(0) + matcher.replaceAll("-")).toLowerCase();
        return entities.stream().filter(e -> e.getName().equals(finalEntityNameRoot)).findFirst().orElse(null);
    }
}
