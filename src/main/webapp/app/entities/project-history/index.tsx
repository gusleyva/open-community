import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProjectHistory from './project-history';
import ProjectHistoryDetail from './project-history-detail';
import ProjectHistoryUpdate from './project-history-update';
import ProjectHistoryDeleteDialog from './project-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProjectHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProjectHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProjectHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProjectHistory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProjectHistoryDeleteDialog} />
  </>
);

export default Routes;
