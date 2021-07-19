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

import _ from 'underscore';
import React from 'react';
import './validation_modal.css';
import { Button, Modal, ListGroup, ListGroupItem } from 'react-bootstrap';

class ValidationModal extends React.Component {
    constructor(...args) {
        super(...args);
        this.state = { showModal: false };
        this.close = () => {
            this.setState({ showModal: false });
        };
        this.open = () => {
            this.setState({ showModal: true });
        };
    }

    render() {
        var problems = this.props.schemaProblems;
        var items = _.map(problems, (problem, i) => <ListGroupItem key={i}>{problem}</ListGroupItem>);
        return (
                <div>
                <Button onClick={this.open}>Validate schema</Button>
                <Modal onHide={this.close} className="modal-validator" aria-labelledby="modal-label" show={this.state.showModal} renderBackdrop={this.renderBackdrop}>
                <div className="modal-list">
                <ListGroup>{items}</ListGroup>
                </div>
                </Modal>
                </div>
        );
    }
}

export default ValidationModal;
