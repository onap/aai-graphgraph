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
import { Button } from 'react-bootstrap';
import { exportSchema } from './requests.js';

class DownloadExport extends React.Component {
    constructor (props, context) {
        super(props, context);
        this.download = this.download.bind(this);
    }

    download() {
        setTimeout(() => {
            const response = { file: exportSchema(this.props.schemaVersion) };
            window.open(response.file);
        }, 100);
    }

    render() {
        return <Button onClick={this.download}>Download as XMI</Button>;
    }
}

export default DownloadExport;
