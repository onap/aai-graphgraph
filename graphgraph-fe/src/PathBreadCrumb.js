import _ from 'underscore'
import React from 'react'
import { Breadcrumb } from 'react-bootstrap'

class PathBreadCrumb extends React.Component {
  constructor (props, context) {
    super(props, context)
    this.pathSelected = this.pathSelected.bind(this)
  }

  pathSelected (evt) {
    evt.preventDefault()
    // the data is only piggyback riding on the "target" property .. not nice but works
    this.props.pathCallback(this.props.index, evt.target.getAttribute('target'))
  }

  render () {
    var path = this.props.path
    var callback = this.pathSelected
    var items = _.map(path, (item, i) => <Breadcrumb.Item key={i} target={item.id} onClick={callback}> {item.id} </Breadcrumb.Item>)

    return (<Breadcrumb>{items}</Breadcrumb>)
  }
}

export default PathBreadCrumb
