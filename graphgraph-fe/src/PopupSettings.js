import React from 'react'
import Popup from 'reactjs-popup'
import './PopupSettings.css'
import GraphHops from './GraphHops'

class PopupMenu extends React.Component {
  render () {
    return (
      <Popup trigger={<button className='settings-button' disabled={this.props.isDisabled}>Hops</button>} position="bottom right">
        {close => (
          <div>
            <GraphHops edgeFilter={this.props.edgeFilter} parentHops={this.props.parentHops} childHops={this.props.childHops} cousinHops={this.props.cousinHops} updateHops={this.props.updateHops} />
            <button
              type="button"
              className="link-button, close"
              onClick={close}>
          &times;
            </button>
          </div>
        )}
      </Popup>

    )
  }
}

export default PopupMenu
