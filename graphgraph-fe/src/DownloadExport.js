import React from 'react'
import { Button } from 'react-bootstrap'
import { exportSchema } from './requests'


class DownloadExport extends React.Component {
  constructor (props, context) {
    super(props, context)
    this.download = this.download.bind(this)
  }

  download() {

 setTimeout(() => {
    const response = {
      file: exportSchema(this.props.schemaVersion),
    };
    window.open(response.file);
  }, 100);
  }

  render() {
      return (
        <Button onClick={this.download}>Download as XMI</Button>
    );
  }
}

export default DownloadExport
