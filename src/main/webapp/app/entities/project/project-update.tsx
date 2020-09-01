import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IPhoto } from 'app/shared/model/photo.model';
import { getEntities as getPhotos } from 'app/entities/photo/photo.reducer';
import { IInitiative } from 'app/shared/model/initiative.model';
import { getEntities as getInitiatives } from 'app/entities/initiative/initiative.reducer';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { getEntities as getVolunteers } from 'app/entities/volunteer/volunteer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './project.reducer';
import { IProject } from 'app/shared/model/project.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProjectUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjectUpdate = (props: IProjectUpdateProps) => {
  const [idsinitiative, setIdsinitiative] = useState([]);
  const [locationId, setLocationId] = useState('0');
  const [photoId, setPhotoId] = useState('0');
  const [volunteerId, setVolunteerId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { projectEntity, locations, photos, initiatives, volunteers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/project' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLocations();
    props.getPhotos();
    props.getInitiatives();
    props.getVolunteers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    values.registrationDeadLine = convertDateTimeToServer(values.registrationDeadLine);

    if (errors.length === 0) {
      const entity = {
        ...projectEntity,
        ...values,
        initiatives: mapIdList(values.initiatives),
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
          <h2 id="opencommunityApp.project.home.createOrEditLabel">
            <Translate contentKey="opencommunityApp.project.home.createOrEditLabel">Create or edit a Project</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : projectEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="project-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="project-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="projectTitleLabel" for="project-projectTitle">
                  <Translate contentKey="opencommunityApp.project.projectTitle">Project Title</Translate>
                </Label>
                <AvField
                  id="project-projectTitle"
                  type="text"
                  name="projectTitle"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    minLength: { value: 3, errorMessage: translate('entity.validation.minlength', { min: 3 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="startDateLabel" for="project-startDate">
                  <Translate contentKey="opencommunityApp.project.startDate">Start Date</Translate>
                </Label>
                <AvInput
                  id="project-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.projectEntity.startDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="project-endDate">
                  <Translate contentKey="opencommunityApp.project.endDate">End Date</Translate>
                </Label>
                <AvInput
                  id="project-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.projectEntity.endDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="registrationDeadLineLabel" for="project-registrationDeadLine">
                  <Translate contentKey="opencommunityApp.project.registrationDeadLine">Registration Dead Line</Translate>
                </Label>
                <AvInput
                  id="project-registrationDeadLine"
                  type="datetime-local"
                  className="form-control"
                  name="registrationDeadLine"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.projectEntity.registrationDeadLine)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="project-description">
                  <Translate contentKey="opencommunityApp.project.description">Description</Translate>
                </Label>
                <AvField id="project-description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="volunteerTaskLabel" for="project-volunteerTask">
                  <Translate contentKey="opencommunityApp.project.volunteerTask">Volunteer Task</Translate>
                </Label>
                <AvField id="project-volunteerTask" type="text" name="volunteerTask" />
              </AvGroup>
              <AvGroup>
                <Label id="maxVolunteerLabel" for="project-maxVolunteer">
                  <Translate contentKey="opencommunityApp.project.maxVolunteer">Max Volunteer</Translate>
                </Label>
                <AvField id="project-maxVolunteer" type="string" className="form-control" name="maxVolunteer" />
              </AvGroup>
              <AvGroup>
                <Label id="minAgeLabel" for="project-minAge">
                  <Translate contentKey="opencommunityApp.project.minAge">Min Age</Translate>
                </Label>
                <AvField id="project-minAge" type="string" className="form-control" name="minAge" />
              </AvGroup>
              <AvGroup>
                <Label id="maxGuestLabel" for="project-maxGuest">
                  <Translate contentKey="opencommunityApp.project.maxGuest">Max Guest</Translate>
                </Label>
                <AvField id="project-maxGuest" type="string" className="form-control" name="maxGuest" />
              </AvGroup>
              <AvGroup>
                <Label id="volunteerInstructionsLabel" for="project-volunteerInstructions">
                  <Translate contentKey="opencommunityApp.project.volunteerInstructions">Volunteer Instructions</Translate>
                </Label>
                <AvField id="project-volunteerInstructions" type="text" name="volunteerInstructions" />
              </AvGroup>
              <AvGroup>
                <Label id="additionalProjectDetailsLabel" for="project-additionalProjectDetails">
                  <Translate contentKey="opencommunityApp.project.additionalProjectDetails">Additional Project Details</Translate>
                </Label>
                <AvField id="project-additionalProjectDetails" type="text" name="additionalProjectDetails" />
              </AvGroup>
              <AvGroup>
                <Label for="project-location">
                  <Translate contentKey="opencommunityApp.project.location">Location</Translate>
                </Label>
                <AvInput id="project-location" type="select" className="form-control" name="location.id">
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
                <Label for="project-photo">
                  <Translate contentKey="opencommunityApp.project.photo">Photo</Translate>
                </Label>
                <AvInput id="project-photo" type="select" className="form-control" name="photo.id">
                  <option value="" key="0" />
                  {photos
                    ? photos.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="project-initiative">
                  <Translate contentKey="opencommunityApp.project.initiative">Initiative</Translate>
                </Label>
                <AvInput
                  id="project-initiative"
                  type="select"
                  multiple
                  className="form-control"
                  name="initiatives"
                  value={projectEntity.initiatives && projectEntity.initiatives.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {initiatives
                    ? initiatives.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.title}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="project-volunteer">
                  <Translate contentKey="opencommunityApp.project.volunteer">Volunteer</Translate>
                </Label>
                <AvInput id="project-volunteer" type="select" className="form-control" name="volunteer.id">
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
              <Button tag={Link} id="cancel-save" to="/project" replace color="info">
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
  photos: storeState.photo.entities,
  initiatives: storeState.initiative.entities,
  volunteers: storeState.volunteer.entities,
  projectEntity: storeState.project.entity,
  loading: storeState.project.loading,
  updating: storeState.project.updating,
  updateSuccess: storeState.project.updateSuccess,
});

const mapDispatchToProps = {
  getLocations,
  getPhotos,
  getInitiatives,
  getVolunteers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProjectUpdate);
