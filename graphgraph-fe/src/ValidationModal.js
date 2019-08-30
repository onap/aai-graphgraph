import _ from 'underscore'
import React from 'react'
import './ValidationModal.css'
import { Button, Modal, ListGroup, ListGroupItem } from 'react-bootstrap'


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

  renderBackdrop(props) {
    return <div {...props} className="modal-backdrop" />;
  }

  render() {
    var problems = this.props.schemaProblems
    var items = _.map(problems, (problem, i) => <ListGroupItem key={i}> {problem} </ListGroupItem>)
      return (
      <div>
        <Button onClick={this.open}>Validate schema</Button>
        <Modal
          onHide={this.close}
          className="modal-validator"
          aria-labelledby="modal-label"
          show={this.state.showModal}
          renderBackdrop={this.renderBackdrop}
        >
        <div className="modal-list">
        <ListGroup>
          {items}
        </ListGroup>
        </div>
        </Modal>
      </div>
    );
  }
}

export default ValidationModal
