import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { getEntities as getVolunteers } from 'app/entities/volunteer/volunteer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './project-history.reducer';
import { IProjectHistory } from 'app/shared/model/project-history.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProjectHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjectHistoryUpdate = (props: IProjectHistoryUpdateProps) => {
  const [projectId, setProjectId] = useState('0');
  const [volunteerId, setVolunteerId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { projectHistoryEntity, projects, volunteers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/project-history');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getProjects();
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

    if (errors.length === 0) {
      const entity = {
        ...projectHistoryEntity,
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
          <h2 id="opencommunityApp.projectHistory.home.createOrEditLabel">
            <Translate contentKey="opencommunityApp.projectHistory.home.createOrEditLabel">Create or edit a ProjectHistory</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : projectHistoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="project-history-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="project-history-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="startDateLabel" for="project-history-startDate">
                  <Translate contentKey="opencommunityApp.projectHistory.startDate">Start Date</Translate>
                </Label>
                <AvInput
                  id="project-history-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.projectHistoryEntity.startDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="project-history-endDate">
                  <Translate contentKey="opencommunityApp.projectHistory.endDate">End Date</Translate>
                </Label>
                <AvInput
                  id="project-history-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.projectHistoryEntity.endDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="languageLabel" for="project-history-language">
                  <Translate contentKey="opencommunityApp.projectHistory.language">Language</Translate>
                </Label>
                <AvInput
                  id="project-history-language"
                  type="select"
                  className="form-control"
                  name="language"
                  value={(!isNew && projectHistoryEntity.language) || 'FRENCH'}
                >
                  <option value="FRENCH">{translate('opencommunityApp.Language.FRENCH')}</option>
                  <option value="ENGLISH">{translate('opencommunityApp.Language.ENGLISH')}</option>
                  <option value="SPANISH">{translate('opencommunityApp.Language.SPANISH')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="project-history-project">
                  <Translate contentKey="opencommunityApp.projectHistory.project">Project</Translate>
                </Label>
                <AvInput id="project-history-project" type="select" className="form-control" name="project.id">
                  <option value="" key="0" />
                  {projects
                    ? projects.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="project-history-volunteer">
                  <Translate contentKey="opencommunityApp.projectHistory.volunteer">Volunteer</Translate>
                </Label>
                <AvInput id="project-history-volunteer" type="select" className="form-control" name="volunteer.id">
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
              <Button tag={Link} id="cancel-save" to="/project-history" replace color="info">
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
  projects: storeState.project.entities,
  volunteers: storeState.volunteer.entities,
  projectHistoryEntity: storeState.projectHistory.entity,
  loading: storeState.projectHistory.loading,
  updating: storeState.projectHistory.updating,
  updateSuccess: storeState.projectHistory.updateSuccess,
});

const mapDispatchToProps = {
  getProjects,
  getVolunteers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProjectHistoryUpdate);
