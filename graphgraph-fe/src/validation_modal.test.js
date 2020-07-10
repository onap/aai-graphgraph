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

import ValidationModal from './validation_modal';
import { ListGroupItem } from 'react-bootstrap';

describe('component ValidationModal', () => {

    const testData = {
        problems:[
            "constrained-element-set and model-constraint are associated in edgerules but not in OXM (via relationship-list)",
            "metadatum and service-instance are associated in edgerules but not in OXM (via relationship-list)",
            "metadatum and image are associated in edgerules but not in OXM (via relationship-list)"
        ]
    };

    it('renders validation modal', () => {
        let validationModal = mount(<ValidationModal schemaProblems={testData.problems}/>);

        // click on show modal button
        validationModal.find('button').simulate('click');

        // assert rendered breadcrumb items
        let renderedProblems = validationModal.find(ListGroupItem);
        expect(renderedProblems.map(item => item.props().children)).toEqual(testData.problems);
    });

});