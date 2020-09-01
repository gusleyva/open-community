import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './volunteer.reducer';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVolunteerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VolunteerDetail = (props: IVolunteerDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { volunteerEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="opencommunityApp.volunteer.detail.title">Volunteer</Translate> [<b>{volunteerEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="firstName">
              <Translate contentKey="opencommunityApp.volunteer.firstName">First Name</Translate>
            </span>
            <UncontrolledTooltip target="firstName">
              <Translate contentKey="opencommunityApp.volunteer.help.firstName" />
            </UncontrolledTooltip>
          </dt>
          <dd>{volunteerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="opencommunityApp.volunteer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{volunteerEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="opencommunityApp.volunteer.email">Email</Translate>
            </span>
          </dt>
          <dd>{volunteerEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="opencommunityApp.volunteer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{volunteerEntity.phoneNumber}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="opencommunityApp.volunteer.age">Age</Translate>
            </span>
          </dt>
          <dd>{volunteerEntity.age}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.volunteer.location">Location</Translate>
          </dt>
          <dd>{volunteerEntity.location ? volunteerEntity.location.id : ''}</dd>
          <dt>
            <Translate contentKey="opencommunityApp.volunteer.coordinator">Coordinator</Translate>
          </dt>
          <dd>{volunteerEntity.coordinator ? volunteerEntity.coordinator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/volunteer" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/volunteer/${volunteerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ volunteer }: IRootState) => ({
  volunteerEntity: volunteer.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VolunteerDetail);
