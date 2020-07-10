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
import PathBreadcrumb from './path_breadcrumb';
import { Breadcrumb } from 'react-bootstrap';

describe('component PathBreadcrumb', () => {

    const testData = {
        index: 1,
        path:[ { id:'root' }, { id:'parent' }, { id:'children' } ]
    };

    it('renders breadcrumbs that can be selected', () => {
        let pathCallback = (index, target) => {
            // assert breadcrumb selection callback
            expect(index).toEqual(1);
            expect(target).toEqual(testData.path.pop().id);
        };

        let pathBreadcrumb = mount(<PathBreadcrumb index={testData.index} path={testData.path} pathCallback={pathCallback}/>);
        let renderedBreadcrumbItems = pathBreadcrumb.find(Breadcrumb.Item);

        // assert rendered breadcrumb items
        expect(renderedBreadcrumbItems.map(item => item.props().target)).toEqual(testData.path.map(item => item.id));

        // click on last breadcrumb item
        renderedBreadcrumbItems.last().find('a').simulate('click');
    });
});