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

import GraphHops from './graph_hops';
import NumericInput from 'react-numeric-input';

describe('component GraphHops', () => {

    const testData = {
        parentHops: 10,
        cousinHops: 20,
        childHops: 30,
        edgeFilter: 'Edgerules'
    };

    it('renders graph hops variables', () => {
        let updateHops = (parentHops, cousinHops, childHops) => {
            testData.parentHops = parentHops;
            testData.cousinHops = cousinHops;
            testData.childHops = childHops;
            expect(graphHops.state().parentHops).toEqual(testData.parentHops);
            expect(graphHops.state().cousinHops).toEqual(testData.cousinHops);
            expect(graphHops.state().childHops).toEqual(testData.childHops);
        };
        let graphHops = mount(<GraphHops parentHops={testData.parentHops} cousinHops={testData.cousinHops} childHops={testData.childHops} edgeFilter={testData.edgeFilter} updateHops={updateHops}/>);

        // 1. edgeFilter === 'Edgerules'
        // assert rendering
        expect(graphHops.find(NumericInput).length).toEqual(1);

        // change cousin hops
        graphHops.find(NumericInput).find('input').simulate('change', { target: { value: 22 } });

        // 2. edgeFilter !== 'Edgerules'
        // force change prop
        testData.edgeFilter = '';
        graphHops.setProps({ edgeFilter: testData.edgeFilter });

        // assert rendering
        expect(graphHops.find(NumericInput).length).toEqual(2);

        // change parent and children hops
        graphHops.find(NumericInput).at(0).find('input').simulate('change', { target: { value: 11 } });
        graphHops.find(NumericInput).at(1).find('input').simulate('change', { target: { value: 33 } });
    });

});