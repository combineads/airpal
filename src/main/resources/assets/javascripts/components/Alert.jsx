import React from 'react';
import { Modal } from 'react-bootstrap';

let Alert = React.createClass({
  displayName: 'Alert',

  componentDidMount() {
    this.refs.name.getDOMNode().focus();
  },

   render() {
    return (
      <Modal {...this.props} title="Alert message">
        <div className="modal-body alert-modal">
          <form className="form-horizontal" action="#" onSubmit={this.handlequeryRequest}>

            <pre>{this.props.query}</pre>

           <div>
            You dont have aceess for this query !  </div>
           </form>
        </div>

        <div className="modal-footer">
            
            <button type="button" className="btn btn-default" onClick={this.props.onRequestHide}>OK</button>
        </div>
      </Modal>
    );
  }
});

export default Alert;

