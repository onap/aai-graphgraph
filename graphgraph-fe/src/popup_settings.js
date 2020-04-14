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
import Popup from 'reactjs-popup';
import './popup_settings.css';
import GraphHops from './graph_hops.js';

class PopupMenu extends React.Component {
    render () {
        return (
                <Popup trigger={<button className='settings-button' disabled={this.props.isDisabled}>Hops</button>} position="bottom right">
                {close => (
                        <div>
                        <GraphHops edgeFilter={this.props.edgeFilter} parentHops={this.props.parentHops} childHops={this.props.childHops} cousinHops={this.props.cousinHops} updateHops={this.props.updateHops}/>
                        <button type="button" className="link-button, close" onClick={close}>&times;</button>
                        </div>
                )}</Popup>
        );
    }
}

export default PopupMenu;
