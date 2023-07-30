import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './intervention.reducer';

export const InterventionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const interventionEntity = useAppSelector(state => state.intervention.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="interventionDetailsHeading">
          <Translate contentKey="jhipsterApp.intervention.detail.title">Intervention</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{interventionEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="jhipsterApp.intervention.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionEntity.startDate ? (
              <TextFormat value={interventionEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="targetArea">
              <Translate contentKey="jhipsterApp.intervention.targetArea">Target Area</Translate>
            </span>
          </dt>
          <dd>{interventionEntity.targetArea}</dd>
          <dt>
            <span id="targetDate">
              <Translate contentKey="jhipsterApp.intervention.targetDate">Target Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionEntity.targetDate ? (
              <TextFormat value={interventionEntity.targetDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="achievedArea">
              <Translate contentKey="jhipsterApp.intervention.achievedArea">Achieved Area</Translate>
            </span>
          </dt>
          <dd>{interventionEntity.achievedArea}</dd>
          <dt>
            <span id="costOfIntervention">
              <Translate contentKey="jhipsterApp.intervention.costOfIntervention">Cost Of Intervention</Translate>
            </span>
          </dt>
          <dd>{interventionEntity.costOfIntervention}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.intervention.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionEntity.createdDate ? (
              <TextFormat value={interventionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.intervention.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionEntity.lastModifiedDate ? (
              <TextFormat value={interventionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterApp.intervention.type">Type</Translate>
          </dt>
          <dd>{interventionEntity.type ? interventionEntity.type.name : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.intervention.project">Project</Translate>
          </dt>
          <dd>{interventionEntity.project ? interventionEntity.project.name : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.intervention.location">Location</Translate>
          </dt>
          <dd>{interventionEntity.location ? interventionEntity.location.name : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.intervention.ward">Ward</Translate>
          </dt>
          <dd>{interventionEntity.ward ? interventionEntity.ward.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/intervention" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/intervention/${interventionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InterventionDetail;
