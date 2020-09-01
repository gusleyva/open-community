import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label, UncontrolledTooltip } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntities as getVolunteers } from 'app/entities/volunteer/volunteer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './volunteer.reducer';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVolunteerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VolunteerUpdate = (props: IVolunteerUpdateProps) => {
  const [locationId, setLocationId] = useState('0');
  const [coordinatorId, setCoordinatorId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { volunteerEntity, locations, volunteers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/volunteer');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getLocations();
    props.getVolunteers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...volunteerEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="opencommunityApp.volunteer.home.createOrEditLabel">
            <Translate contentKey="opencommunityApp.volunteer.home.createOrEditLabel">Create or edit a Volunteer</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : volunteerEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="volunteer-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="volunteer-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="volunteer-firstName">
                  <Translate contentKey="opencommunityApp.volunteer.firstName">First Name</Translate>
                </Label>
                <AvField
                  id="volunteer-firstName"
                  type="text"
                  name="firstName"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
                <UncontrolledTooltip target="firstNameLabel">
                  <Translate contentKey="opencommunityApp.volunteer.help.firstName" />
                </UncontrolledTooltip>
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="volunteer-lastName">
                  <Translate contentKey="opencommunityApp.volunteer.lastName">Last Name</Translate>
                </Label>
                <AvField
                  id="volunteer-lastName"
                  type="text"
                  name="lastName"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="volunteer-email">
                  <Translate contentKey="opencommunityApp.volunteer.email">Email</Translate>
                </Label>
                <AvField
                  id="volunteer-email"
                  type="text"
                  name="email"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phoneNumberLabel" for="volunteer-phoneNumber">
                  <Translate contentKey="opencommunityApp.volunteer.phoneNumber">Phone Number</Translate>
                </Label>
                <AvField id="volunteer-phoneNumber" type="text" name="phoneNumber" />
              </AvGroup>
              <AvGroup>
                <Label id="ageLabel" for="volunteer-age">
                  <Translate contentKey="opencommunityApp.volunteer.age">Age</Translate>
                </Label>
                <AvField id="volunteer-age" type="string" className="form-control" name="age" />
              </AvGroup>
              <AvGroup>
                <Label for="volunteer-location">
                  <Translate contentKey="opencommunityApp.volunteer.location">Location</Translate>
                </Label>
                <AvInput id="volunteer-location" type="select" className="form-control" name="location.id">
                  <option value="" key="0" />
                  {locations
                    ? locations.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="volunteer-coordinator">
                  <Translate contentKey="opencommunityApp.volunteer.coordinator">Coordinator</Translate>
                </Label>
                <AvInput id="volunteer-coordinator" type="select" className="form-control" name="coordinator.id">
                  <option value="" key="0" />
                  {volunteers
                    ? volunteers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/volunteer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  locations: storeState.location.entities,
  volunteers: storeState.volunteer.entities,
  volunteerEntity: storeState.volunteer.entity,
  loading: storeState.volunteer.loading,
  updating: storeState.volunteer.updating,
  updateSuccess: storeState.volunteer.updateSuccess,
});

const mapDispatchToProps = {
  getLocations,
  getVolunteers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VolunteerUpdate);
