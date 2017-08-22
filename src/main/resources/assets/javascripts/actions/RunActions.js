import alt from '../alt';
import RunApiUtils from '../utils/RunApiUtils';
import logError from '../utils/logError'

class RunActions {
  constructor() {
    this.generateActions(
      'addMultipleRuns',
      'addQuery',
      'addRun',
      'connect',
      'addFetch',
      'disconnect',
      'handleConnectionError',
      'handleConnectionOpen',
      'resetOnlineStatus',
      'wentOffline',
      'wentOnline'
    );
  }

  fetchHistory() {
    RunApiUtils.fetchHistory().then((results) => {
      this.actions.addFetch(results);
    }).catch(logError);
  }

  execute({ query, tmpTable }) {
    RunApiUtils.execute(query, tmpTable).then((runObject) => {
      this.dispatch();
      this.actions.addRun(runObject);
    }).catch(logError);
  }

fetchForUser(user){
    RunApiUtils.fetchForUser(user).then((results) => {
     this.actions.addQuery(results);
    }).catch(logError);
  }

  kill(uuid) {
    RunApiUtils.kill(uuid);
  }

  handleConnectionMessage(data) {
    this.dispatch(data.job);
  }
}

export default alt.createActions(RunActions);
