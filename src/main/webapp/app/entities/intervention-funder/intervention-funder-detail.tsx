import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './intervention-funder.reducer';

export const InterventionFunderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const interventionFunderEntity = useAppSelector(state => state.interventionFunder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="interventionFunderDetailsHeading">
          <Translate contentKey="jhipsterApp.interventionFunder.detail.title">InterventionFunder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="jhipsterApp.interventionFunder.id">Id</Translate>
            </span>
          </dt>
          <dd>{interventionFunderEntity.id}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.interventionFunder.intervention">Intervention</Translate>
          </dt>
          <dd>{interventionFunderEntity.intervention ? interventionFunderEntity.intervention.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.interventionFunder.funder">Funder</Translate>
          </dt>
          <dd>{interventionFunderEntity.funder ? interventionFunderEntity.funder.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/intervention-funder" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/intervention-funder/${interventionFunderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InterventionFunderDetail;
