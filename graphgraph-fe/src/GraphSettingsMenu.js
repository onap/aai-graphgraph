import React from 'react'
import GraphSettings from './GraphSettings'
import { Navbar, Nav } from 'react-bootstrap'
import './GraphSettingsMenu.css'

class GraphSettingsMenu extends React.Component {
  render () {
    return (
      <Navbar className='navbar-adjust'>
        <Navbar.Header>
          <Navbar.Brand>
            <a href="https://gerrit.onap.org/r/gitweb?p=aai/graphgraph.git">GraphGraph</a>
          </Navbar.Brand>
        </Navbar.Header>
 <Nav className="mr-auto">
        <Navbar.Collapse className='mr-sm-2'>
              <GraphSettings selectedNode={this.props.selectedNode} graphData={this.props.graphData} nodePropsLoader={this.props.nodePropsLoader} />
        </Navbar.Collapse>
</Nav>
      </Navbar>)
  }
}

export default GraphSettingsMenu
