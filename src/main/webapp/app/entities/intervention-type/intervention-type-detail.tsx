import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './intervention-type.reducer';

export const InterventionTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const interventionTypeEntity = useAppSelector(state => state.interventionType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="interventionTypeDetailsHeading">
          <Translate contentKey="jhipsterApp.interventionType.detail.title">InterventionType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{interventionTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.interventionType.name">Name</Translate>
            </span>
          </dt>
          <dd>{interventionTypeEntity.name}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.interventionType.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionTypeEntity.createdDate ? (
              <TextFormat value={interventionTypeEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.interventionType.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {interventionTypeEntity.lastModifiedDate ? (
              <TextFormat value={interventionTypeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/intervention-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/intervention-type/${interventionTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InterventionTypeDetail;
