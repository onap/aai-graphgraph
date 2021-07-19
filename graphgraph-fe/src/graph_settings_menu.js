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
import GraphSettings from './graph_settings.js';
import { Navbar } from 'react-bootstrap';
import './graph_settings_menu.css';

const GraphSettingsMenu = (props) => {
        return (
                <Navbar className='navbar-adjust'>
                    <Navbar.Brand>
                        <a href="https://gerrit.onap.org/r/gitweb?p=aai/graphgraph.git">GraphGraph</a>
                    </Navbar.Brand>
                    <Navbar.Collapse className='mr-sm-2'>
                        <GraphSettings selectedNode={props.selectedNode} graphData={props.graphData} nodePropsLoader={props.nodePropsLoader}/>
                    </Navbar.Collapse>
                </Navbar>
        );
}

export default GraphSettingsMenu;
