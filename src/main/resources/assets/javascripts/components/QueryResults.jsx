import React from 'react';
import RunActions from '../actions/RunActions';
import Queryruntable from './Queryruntable';
import UserStore from '../stores/UserStore';
import RunsTable from './RunsTable';

function getStateFromStore() {
  return {
    user: UserStore.getCurrentUser()
  };
}

let QueryResults = React.createClass({

getInitialState() {
    return getStateFromStore();
  },

  componentWillMount() {
    RunActions.fetchForUser(this.state.user);
  },

  render() {
  let user = this.state.user;
    return (
         <Queryruntable
          user={user.name}
          cancreatecsv={user.executionPermissions.canCreateCsv}
           tableWidth={this.props.tableWidth}
          tableHeight={this.props.tableHeight} />
    );
  }   
});

export default QueryResults;


