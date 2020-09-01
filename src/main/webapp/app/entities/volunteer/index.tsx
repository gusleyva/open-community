import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Volunteer from './volunteer';
import VolunteerDetail from './volunteer-detail';
import VolunteerUpdate from './volunteer-update';
import VolunteerDeleteDialog from './volunteer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VolunteerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VolunteerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VolunteerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Volunteer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VolunteerDeleteDialog} />
  </>
);

export default Routes;
