import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Initiative from './initiative';
import InitiativeDetail from './initiative-detail';
import InitiativeUpdate from './initiative-update';
import InitiativeDeleteDialog from './initiative-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InitiativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InitiativeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InitiativeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Initiative} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InitiativeDeleteDialog} />
  </>
);

export default Routes;
