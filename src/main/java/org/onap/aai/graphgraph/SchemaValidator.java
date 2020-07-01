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

import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.onap.aai.graphgraph.dto.Edge;
import org.onap.aai.graphgraph.dto.Graph;
import org.onap.aai.graphgraph.dto.ValidationProblems;
import org.onap.aai.graphgraph.reader.BasicSchemaReader;

public class SchemaValidator {

    private Graph edgerules;
    private Graph oxm;

    public ValidationProblems validate(String schemaVersion) {
        ValidationProblems validationProblems = new ValidationProblems();
        BasicSchemaReader schema = new BasicSchemaReader(schemaVersion);
        oxm = schema.getGraph("all", 0, 0, 0, "Parents");
        edgerules = schema.getGraph("all", 0, 0, 0, "Edgerules");

        checkIfDanglingEdgerules(validationProblems);
        checkIfObsoleteOxm(validationProblems);
        schema.getSchemaErrors().forEach(validationProblems::addProblem);
        return validationProblems;
    }

    /**
     * computes nodes connected to relationship-list but not used in edgerules
     * @param validationProblems
     */
    private void checkIfObsoleteOxm(ValidationProblems validationProblems) {
        Set<String> relationshipListConnected = getAllNodesConnectedToRelationshipList();
        Set<String> nodesInEdgerules = getEdgerulePairs().stream()
                .flatMap(p -> Stream.of(p._1, p._2))
                .collect(Collectors.toSet());
        relationshipListConnected.removeAll(nodesInEdgerules);
        relationshipListConnected.forEach(n -> validationProblems.addProblem(
                String.format("%s is associated with relationship-list in OXM but not present in edgerules",
                        n)));
    }

    private Set<Tuple2<String, String>> getEdgerulePairs() {
        return edgerules.getEdges().stream()
                .map(e -> Tuple.of(e.getSource(), e.getTarget()))
                .collect(Collectors.toSet());
    }

    /**
     * computes edgerules which don't have the necessary connection to relationship-list in OXM
     * @param validationProblems
     */
    private void checkIfDanglingEdgerules(ValidationProblems validationProblems) {
        Set<Tuple2<String, String>> edgerulePairs = getEdgerulePairs();
        edgerulePairs.removeAll(getOxmPairs());
        edgerulePairs.forEach(erp -> validationProblems.addProblem(
                        String.format("%s and %s are associated in edgerules but not in OXM (via relationship-list)",
                                erp._1, erp._2)));
    }

    private Set<Tuple2<String, String>> getOxmPairs() {
        Set<Tuple2<String, String>> pairs = new HashSet<>();
        Set<String> inRelationshipList = getAllNodesConnectedToRelationshipList();

        inRelationshipList.forEach(edge1 -> inRelationshipList
                .forEach(edge2 -> pairs.add(Tuple.of(edge1, edge2))));
        return pairs;
    }

    private Set<String> getAllNodesConnectedToRelationshipList() {
        List<Edge> edges = oxm.getEdges();
        Set<String> inRelationshipList = edges.stream()
                .filter(e -> e.getSource().equals("relationship-list"))
                .map(Edge::getTarget)
                .collect(Collectors.toSet());
        inRelationshipList.addAll(edges.stream()
                .filter(e -> e.getTarget().equals("relationship-list"))
                .map(Edge::getSource)
                .collect(Collectors.toSet()));
        return inRelationshipList;
    }
}
