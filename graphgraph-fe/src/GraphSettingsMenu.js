import React from 'react'
import GraphSettings from './GraphSettings'
import { Navbar, FormGroup } from 'react-bootstrap'
import './GraphSettingsMenu.css'

class GraphSettingsMenu extends React.Component {
  render () {
    return (
      <Navbar className='navbar-adjust'>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="https://gerrit.onap.org/r/gitweb?p=aai/graphgraph.git">GraphGraph</a>
          </Navbar.Brand>
          <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
          <Navbar.Form pullLeft>
            <FormGroup>
              <GraphSettings selectedNode={this.props.selectedNode} graphData={this.props.graphData} nodePropsLoader={this.props.nodePropsLoader} />
            </FormGroup>{' '}
          </Navbar.Form>
        </Navbar.Collapse>
      </Navbar>)
  }
}

export default GraphSettingsMenu
