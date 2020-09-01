import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './project.reducer';
import { IProject } from 'app/shared/model/project.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProjectDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjectDetail = (props: IProjectDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { projectEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="opencommunityApp.project.detail.title">Project</Translate> [<b>{projectEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="projectTitle">
              <Translate contentKey="opencommunityApp.project.projectTitle">Project Title</Translate>
            </span>
          </dt>
          <dd>{projectEntity.projectTitle}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="opencommunityApp.project.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{projectEntity.startDate ? <TextFormat value={projectEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="opencommunityApp.project.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{projectEntity.endDate ? <TextFormat value={projectEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="registrationDeadLine">
              <Translate contentKey="opencommunityApp.project.registrationDeadLine">Registration Dead Line</Translate>
            </span>
          </dt>
          <dd>
            {projectEntity.registrationDeadLine ? (
              <TextFormat value={projectEntity.registrationDeadLine} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="opencommunityApp.project.description">Description</Translate>
            </span>
          </dt>
          <dd>{projectEntity.description}</dd>
          <dt>
            <span id="volunteerTask">
              <Translate contentKey="opencommunityApp.project.volunteerTask">Volunteer Task</Translate>
            </span>
          </dt>
          <dd>{projectEntity.volunteerTask}</dd>
          <dt>
            <span id="maxVolunteer">
              <Translate contentKey="opencommunityApp.project.maxVolunteer">Max Volunteer</Translate>
            </span>
          </dt>
          <dd>{projectEntity.maxVolunteer}</dd>
          <dt>
            <span id="minAge">
              <Translate contentKey="opencommunityApp.project.minAge">Min Age</Translate>
            </span>
          </dt>
          <dd>{projectEntity.minAge}</dd>
          <dt>
            <span id="maxGuest">
              <Translate contentKey="opencommunityApp.project.maxGuest">Max Guest</Translate>
            </span>
          </dt>
          <dd>{projectEntity.maxGuest}</dd>
          <dt>
            <span id="volunteerInstructions">
              <Translate contentKey="opencommunityApp.project.volunteerInstructions">Volunteer Instructions</Translate>
            </span>
          </dt>
          <dd>{projectEntity.volunteerInstructions}</dd>
          <dt>
            <span id="additionalProjectDetails">
              <Translate contentKey="opencommunityApp.project.additionalProjectDetails">Additional Project Details</Translate>
            </span>
          </dt>
          <dd>{projectEntity.additionalProjectDetails}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.project.location">Location</Translate>
          </dt>
          <dd>{projectEntity.location ? projectEntity.location.id : ''}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.project.photo">Photo</Translate>
          </dt>
          <dd>{projectEntity.photo ? projectEntity.photo.id : ''}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.project.initiative">Initiative</Translate>
          </dt>
          <dd>
            {projectEntity.initiatives
              ? projectEntity.initiatives.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {projectEntity.initiatives && i === projectEntity.initiatives.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="opencommunityApp.project.volunteer">Volunteer</Translate>
          </dt>
          <dd>{projectEntity.volunteer ? projectEntity.volunteer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/project" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project/${projectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ project }: IRootState) => ({
  projectEntity: project.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProjectDetail);
