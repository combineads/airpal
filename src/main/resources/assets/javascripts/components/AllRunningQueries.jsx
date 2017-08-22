import React from 'react';
import RunActions from '../actions/RunActions';
import FetchRunsTable from './FetchRunsTable';
import UserStore from '../stores/UserStore';
import TableStore from '../stores/TableStore';

function getStateFromStore() {
  return {
    user: UserStore.getCurrentUser(),
  };
}

let AllRunningQueries = React.createClass({

getInitialState() {
    return getStateFromStore();
  },

  componentWillMount() {
    RunActions.fetchHistory();
  },

  render() {
   let user = this.state.user;
    return (
      <FetchRunsTable
        cancreatecsv={user.executionPermissions.canCreateCsv}
        tableWidth={this.props.tableWidth}
        tableHeight={this.props.tableHeight} />
    );
  }
});

export default AllRunningQueries;

