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

import DownloadExport from './download_export';

describe('component DownloadExport', () => {

    const testData = {
        schemaVersion: 'v10',
        downloadUrl: 'http://localhost:/schemas/v10/xmiexport'
    };

    it('renders download export button', async () => {
        let downloadExport = mount(<DownloadExport schemaVersion={testData.schemaVersion}/>);

        jest.spyOn(window, 'open').mockImplementation(() => {});

        // click on download button
        downloadExport.find('button').simulate('click');

        // wait for window open call
        await new Promise((resolve, reject) => setTimeout(() => resolve(), 150));
        
        // assert download action
        expect(window.open).toBeCalledWith(testData.downloadUrl);
    });
});