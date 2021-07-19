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

import React from 'react';
import { mount } from 'enzyme';
import fetchMock from 'fetch-mock-jest';
import { waitForState, waitForProps } from 'enzyme-async-helpers';

import App from './app';
import GraphSettings from './graph_settings';
import { DropdownButton, Dropdown } from 'react-bootstrap';

const baseUrl = 'http://localhost';
// fetchMock.config.fallbackToNetwork = true;

describe('component GraphSettings', () => {

    const testData = {
        schemas: ['v10', 'v11'],
        edgeFilter: [ 'Edgerules', 'Parent-child (OXM structure)' ],
        nodes:{
            edgerules: [{ id:'class-of-service' }, { id: 'allotted-resource' }],
            parents: [{ id: 'aai-internal' }, { id: 'action' }, { id: 'action-data' }]
        },
        validation:{ problems:[ 'test-problem' ] },
        graph: {
            "nodeNames": [{
                "id": "class-of-service"
            }, {
                "id": "site-pair"
            }],
            "edges": [{
                "source": "class-of-service",
                "target": "site-pair",
                "type": "BelongsTo",
                "tooltipProperties": [{
                    "propertyName": "From",
                    "propertyValue": "site-pair"
                }, {
                    "propertyName": "To",
                    "propertyValue": "class-of-service"
                }, {
                    "propertyName": "Type",
                    "propertyValue": "edgerule"
                }, {
                    "propertyName": "Relationship",
                    "propertyValue": "BelongsTo"
                }, {
                    "propertyName": "Multiplicity",
                    "propertyValue": "MANY2ONE"
                }, {
                    "propertyName": "Is default edge",
                    "propertyValue": "true"
                }, {
                    "propertyName": "Description",
                    "propertyValue": ""
                }, {
                    "propertyName": "Is private edge",
                    "propertyValue": "false"
                }, {
                    "propertyName": "Contains",
                    "propertyValue": "IN"
                }, {
                    "propertyName": "Prevent delete",
                    "propertyValue": "NONE"
                }, {
                    "propertyName": "Label",
                    "propertyValue": "org.onap.relationships.inventory.BelongsTo"
                }, {
                    "propertyName": "Delete other v",
                    "propertyValue": "IN"
                }],
                "nodeNames": [{
                    "id": "class-of-service"
                }, {
                    "id": "site-pair"
                }]
            }],
            "paths": [],
            "startNodeProperties": [{
                "propertyName": "cos",
                "propertyValue": "",
                "description": "unique identifier of probe",
                "type": "java.lang.String",
                "key": true,
                "index": true,
                "required": true
            }, {
                "propertyName": "probe-id",
                "propertyValue": "",
                "description": "identifier of probe",
                "type": "java.lang.String",
                "key": false,
                "index": false,
                "required": false
            }, {
                "propertyName": "probe-type",
                "propertyValue": "",
                "description": "type of probe",
                "type": "java.lang.String",
                "key": false,
                "index": false,
                "required": false
            }, {
                "propertyName": "relationship-list",
                "propertyValue": "",
                "description": "no description available",
                "type": "inventory.aai.onap.org.v10.RelationshipList",
                "key": false,
                "index": false,
                "required": false
            }, {
                "propertyName": "resource-version",
                "propertyValue": "",
                "description": "Used for optimistic concurrency.  Must be empty on create, valid on update and delete.",
                "type": "java.lang.String",
                "key": false,
                "index": false,
                "required": false
            }]
        },
        nodeClassOfService: [{
            "propertyName": "cos",
            "propertyValue": "",
            "description": "unique identifier of probe",
            "type": "java.lang.String",
            "key": true,
            "index": true,
            "required": true
        }, {
            "propertyName": "probe-id",
            "propertyValue": "",
            "description": "identifier of probe",
            "type": "java.lang.String",
            "key": false,
            "index": false,
            "required": false
        }, {
            "propertyName": "probe-type",
            "propertyValue": "",
            "description": "type of probe",
            "type": "java.lang.String",
            "key": false,
            "index": false,
            "required": false
        }, {
            "propertyName": "relationship-list",
            "propertyValue": "",
            "description": "no description available",
            "type": "inventory.aai.onap.org.v10.RelationshipList",
            "key": false,
            "index": false,
            "required": false
        }, {
            "propertyName": "resource-version",
            "propertyValue": "",
            "description": "Used for optimistic concurrency.  Must be empty on create, valid on update and delete.",
            "type": "java.lang.String",
            "key": false,
            "index": false,
            "required": false
        }],
        nodeSitePair:[
            {
                description: "no description available",
                index: false,
                key: false,
                propertyName: "site-pair",
                propertyValue: "",
                required: false,
                type: "java.util.List"
            }
        ],
        graphPaths: {
            "nodeNames": [],
            "edges": [],
            "paths": [],
            "startNodeProperties": []
        }
    };

    beforeAll(() => {
        fetchMock.get(baseUrl + '/schemas', testData.schemas);
        fetchMock.get(baseUrl + '/schemas/v10/nodes?edgeFilter=Edgerules', testData.nodes.edgerules);
        fetchMock.get(baseUrl + '/schemas/v10/validation', testData.validation);
        fetchMock.get(baseUrl + '/schemas/v10/graph/basic?node=class-of-service&parentHops=1&cousinHops=1&childHops=1&edgeFilter=Edgerules', testData.graph);
        fetchMock.get(baseUrl + '/schemas/v10/nodes/class-of-service', testData.nodeClassOfService);
        fetchMock.get(baseUrl + '/schemas/v10/graph/paths?fromNode=class-of-service&toNode=allotted-resource&edgeFilter=Edgerules', testData.graphPaths);
        fetchMock.get(baseUrl + '/schemas/v10/nodes?edgeFilter=Parents', testData.nodes.parents);
        fetchMock.get(baseUrl + '/schemas/v10/graph/basic?node=class-of-service&parentHops=1&cousinHops=1&childHops=1&edgeFilter=Parents', testData.graph);
        fetchMock.get(baseUrl + '/schemas/v10/nodes/site-pair', testData.nodeSitePair);
    });
    
    afterAll(() => {
        fetchMock.reset();
        fetchMock.restore();
    });

    let app;

    it('fetches data and has desired initial state', async () => {
        app = mount(<App/>);
        let graphSettings = app.find(GraphSettings);
        
        // wait while schema loads
        await waitForState(graphSettings, state => !!state.schemas);

        // assert initial state
        expect(graphSettings.state()).toEqual({
            schemaProblems: [],
            nodeNames: [],
            fromNode: '',
            graph: { nodeNames: [], edges: [] },
            showHops: false,
            enableDestinationNode: false,
            toNode: '',
            edgeFilter: 'Edgerules',
            hops: { parents: 1, cousin: 1, child: 1 },
            selectedSchema: '',
            schemas: testData.schemas
        });

        // assert basic rendering
        expect(graphSettings.find(DropdownButton).length).toEqual(5);
    });

    it('has correct state after choosing "Schema"', async () => {
        let graphSettings = app.find(GraphSettings);

        // 1. click on schemas dropdown
        graphSettings.find(DropdownButton).at(0).find('button').simulate('click');
        graphSettings = app.find(GraphSettings);

        // 2. assert schema menu items and click on first schema 'v10'
        let menuItems = graphSettings.find(DropdownButton).at(0).find(Dropdown.Item).find('a');
        expect( menuItems.map(menuItem => menuItem.text()) ).toEqual(testData.schemas);
        menuItems.first().simulate('click');

        // wait for data load and set component state
        await waitForState(graphSettings, state => state.schemaProblems.length > 0);

        // assert component state
        expect(graphSettings.state().nodeNames).toEqual(testData.nodes.edgerules);
        expect(graphSettings.state().schemaProblems).toEqual(testData.validation.problems);
        expect(graphSettings.state().fromNode).toEqual('none');
        expect(graphSettings.state().toNode).toEqual('none');
    });

    it('has correct state after choosing "Source Node"', async () => {
        let graphSettings = app.find(GraphSettings);

        // 1. click on source node dropdown
        graphSettings.find(DropdownButton).at(1).find('button').simulate('click');
        graphSettings = app.find(GraphSettings);

        // 2. assert source node menu items and click on 'class-of-service' option
        let menuItems = graphSettings.find(DropdownButton).at(1).find(Dropdown.Item).find('a');
        let desiredSourceNodeItems = ['all'].concat(testData.nodes.edgerules.map(node => node.id));
        menuItems.map(menuItem => console.log("MenuItem: " + menuItem.text()));
        expect( menuItems.map(menuItem => menuItem.text()) ).toEqual(desiredSourceNodeItems);
        menuItems.at(1).simulate('click');

        // wait for data load and set component state
        await waitForState(graphSettings, state => state.fromNode === testData.nodes.edgerules[0].id);

        // assert component state
        expect(graphSettings.state().fromNode).toEqual(testData.nodes.edgerules[0].id);
    });

    it('has correct state after choosing "Destination Node"', async () => {
        let graphSettings = app.find(GraphSettings);

        // 1. click on destination node dropdown
        graphSettings.find(DropdownButton).at(2).find('button').simulate('click');
        graphSettings = app.find(GraphSettings);

        // 2. assert destination node menu items and click on 'allotted-resource' option
        let menuItems = graphSettings.find(DropdownButton).at(2).find(Dropdown.Item).find('a');
        let desiredDestinationNodeItems = ['none'].concat(testData.nodes.edgerules.map(node => node.id));
        expect( menuItems.map(menuItem => menuItem.text()) ).toEqual(desiredDestinationNodeItems);
        
        // catch alert message
        jest.spyOn(window, 'alert').mockImplementation(() => {});

        menuItems.at(2).simulate('click');

        // wait for data load and set component state
        await waitForState(graphSettings, state => state.toNode === testData.nodes.edgerules[1].id);
        
        // assert alert call
        expect(window.alert).toBeCalledWith('The graph has no edges, nothing to display');

        // assert component state
        expect(graphSettings.state().toNode).toEqual(testData.nodes.edgerules[1].id);

        // switch back destination node to 'none'
        graphSettings.find(DropdownButton).at(2).find('button').simulate('click');
        graphSettings.find(DropdownButton).at(2).find(Dropdown.Item).find('a').at(0).simulate('click');
        
        // wait for data load and set component state
        await waitForState(graphSettings, state => state.toNode === 'none');

        // assert component state
        expect(graphSettings.state().toNode).toEqual('none');
    });

    it('has correct state after choosing "Edge Filter"', async () => {
        let graphSettings = app.find(GraphSettings);

        // 1. click on edge filter dropdown
        graphSettings.find(DropdownButton).at(3).find('button').simulate('click');
        graphSettings = app.find(GraphSettings);

        // 2. assert edge filter menu items and click on 'Parent-child (OXM structure)' option
        let menuItems = graphSettings.find(DropdownButton).at(3).find(Dropdown.Item).find('a');
        expect( menuItems.map(menuItem => menuItem.text()) ).toEqual(testData.edgeFilter);
        menuItems.at(1).simulate('click');

        // wait for data load and set component state
        await waitForState(graphSettings, state => state.edgeFilter === 'Parents');

        // assert component state
        expect(graphSettings.state().edgeFilter).toEqual('Parents');
    });

    it('has correct state after choosing "Selected Node"', async () => {
        let graphSettings = app.find(GraphSettings);

        // 1. click on Selected Node dropdown
        graphSettings.find(DropdownButton).at(4).find('button').simulate('click');
        graphSettings = app.find(GraphSettings);

        // 2. assert Selected Node menu items and click on 'site-pair' option
        let menuItems = graphSettings.find(DropdownButton).at(4).find(Dropdown.Item).find('a');
        let desiredSelectedNodeItems = testData.graph.nodeNames.map(node => node.id);
        expect( menuItems.map(menuItem => menuItem.text()) ).toEqual(desiredSelectedNodeItems);
        menuItems.at(1).simulate('click');

        // wait for data load and set component state
        await waitForProps(graphSettings, props => props.selectedNode === desiredSelectedNodeItems[1]);

        // assert component state
        expect(graphSettings.instance().props.selectedNode).toEqual(desiredSelectedNodeItems[1]);
    });
});