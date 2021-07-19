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
import { FormLabel } from 'react-bootstrap';
import NumericInput from 'react-numeric-input2';
import './graph_hops.css';

var createNumInput = function (label, callback, current) {
    return (
            <div>
            <FormLabel>{label}</FormLabel>
            <NumericInput onChange={callback} min={1} max={500} value={current} className="hops-input-field"/>
            </div>
    );
};

class GraphHops extends React.Component {
    constructor (props) {
        super(props);

        this.state = { value: this.props.defaultValue };
        let p = props.parentHops;
        let c = props.cousinHops;
        let ch = props.childHops;

        this.onChangeParent = (e) => this._onChangeParent(e);
        this.onChangeCousin = (e) => this._onChangeCousin(e);
        this.onChangeChild = (e) => this._onChangeChild(e);
        this.onChange = (hopsName, num) => this._onChange(hopsName, num);
        this.state = { parentHops: p, childHops: ch, cousinHops: c };
    }

    _onChange (hopsName, num) {
        var s = this.state;
        s[hopsName] = num;
        this.setState(s);
        this.props.updateHops(this.state.parentHops, this.state.cousinHops, this.state.childHops);
    }

    _onChangeParent (e) {
        this.onChange('parentHops', e);
    }

    _onChangeCousin (e) {
        this.onChange('cousinHops', e);
    }

    _onChangeChild (e) {
        this.onChange('childHops', e);
    }

    render () {
        if (this.props.edgeFilter === 'Edgerules') {
            return <div className="hops-input">{createNumInput('edgerule hops', this.onChangeCousin, this.state.cousinHops)}</div>;
        }

        return <div className="hops-input">{createNumInput('parent hops', this.onChangeParent, this.state.parentHops)} {createNumInput('child hops', this.onChangeChild, this.state.childHops)}</div>;
    }
}

export default GraphHops;
