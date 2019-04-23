import React from 'react'
import { Label } from 'react-bootstrap'
import NumericInput from 'react-numeric-input'
import './GraphHops.css'

var createNumInput = function (label, callback, current) {
  return (
    <div>
      <Label>{label}</Label>
      <NumericInput onChange={callback} min={1} max={500} value={current} className="hops-input-field" />
    </div>
  )
}

class GraphHops extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      value: this.props.defaultValue
    }
    let p = props.parentHops
    let c = props.cousinHops
    let ch = props.childHops

    this.onChangeParent = (e) => this._onChangeParent(e)
    this.onChangeCousin = (e) => this._onChangeCousin(e)
    this.onChangeChild = (e) => this._onChangeChild(e)
    this.onChange = (hopsName, num) => this._onChange(hopsName, num)
    this.state = { parentHops: p, childHops: ch, cousinHops: c }
  }

  _onChange (hopsName, num) {
    var s = this.state
    s[hopsName] = num
    this.setState(s)
    console.log('novy stav je ', s)
    this.props.updateHops(this.state.parentHops, this.state.cousinHops, this.state.childHops)
  }

  _onChangeParent (e) {
    this.onChange('parentHops', e)
  }

  _onChangeCousin (e) {
    this.onChange('cousinHops', e)
  }

  _onChangeChild (e) {
    this.onChange('childHops', e)
  }

  render () {
    return (
      <div className="hops-input">
        {createNumInput('parent hops', this.onChangeParent, this.state.parentHops)}
        {createNumInput('cousin hops', this.onChangeCousin, this.state.cousinHops)}
        {createNumInput('child hops', this.onChangeChild, this.state.childHops)}

      </div>
    )
  }
}

export default GraphHops
