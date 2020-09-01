import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './project-history.reducer';
import { IProjectHistory } from 'app/shared/model/project-history.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProjectHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProjectHistoryDetail = (props: IProjectHistoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { projectHistoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="opencommunityApp.projectHistory.detail.title">ProjectHistory</Translate> [<b>{projectHistoryEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="startDate">
              <Translate contentKey="opencommunityApp.projectHistory.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {projectHistoryEntity.startDate ? (
              <TextFormat value={projectHistoryEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="opencommunityApp.projectHistory.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {projectHistoryEntity.endDate ? <TextFormat value={projectHistoryEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="language">
              <Translate contentKey="opencommunityApp.projectHistory.language">Language</Translate>
            </span>
          </dt>
          <dd>{projectHistoryEntity.language}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.projectHistory.project">Project</Translate>
          </dt>
          <dd>{projectHistoryEntity.project ? projectHistoryEntity.project.id : ''}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.projectHistory.volunteer">Volunteer</Translate>
          </dt>
          <dd>{projectHistoryEntity.volunteer ? projectHistoryEntity.volunteer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/project-history" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/project-history/${projectHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ projectHistory }: IRootState) => ({
  projectHistoryEntity: projectHistory.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProjectHistoryDetail);
