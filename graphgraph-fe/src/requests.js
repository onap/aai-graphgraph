/*
 *  ============LICENSE_START=======================================================
 * org.onap.aai
 *  ================================================================================
 *  Copyright © 2019-2020 Orange Intellectual Property. All rights reserved.
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

const host = window.location.hostname;
const port = window.location.port;
const protocol = window.location.protocol;

export function schemas () {
    return `${protocol}//${host}:${port}/schemas`;
};

export function validateSchema (schema) {
    return `${protocol}//${host}:${port}/schemas/${schema}/validation`;
};

export function exportSchema (schema) {
    return `${protocol}//${host}:${port}/schemas/${schema}/xmiexport`;
};

export function nodeNames (schema, edgeFilter) {
    return `${protocol}//${host}:${port}/schemas/${schema}/nodes?edgeFilter=${edgeFilter}`;
};

export function basicGraph (schema, node, parentHops, cousinHops, childHops, edgeFilter) {
    return `${protocol}//${host}:${port}/schemas/${schema}/graph/basic?node=${node}&parentHops=${parentHops}&cousinHops=${cousinHops}&childHops=${childHops}&edgeFilter=${edgeFilter}`;
};

export function pathGraph (schema, fromNode, toNode, edgeFilter) {
    return `${protocol}//${host}:${port}/schemas/${schema}/graph/paths?fromNode=${fromNode}&toNode=${toNode}&edgeFilter=${edgeFilter}`;
};

export function nodeProperty (schema, node) {
    return `${protocol}//${host}:${port}/schemas/${schema}/nodes/${node}`;
};

export function edgeProperty (schema, fromNode, toNode) {
    return `${protocol}//${host}:${port}/schemas/${schema}/edges?fromNode=${fromNode}&toNode=${toNode}`;
};
