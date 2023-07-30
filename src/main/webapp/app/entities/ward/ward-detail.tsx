import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ward.reducer';

export const WardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wardEntity = useAppSelector(state => state.ward.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wardDetailsHeading">
          <Translate contentKey="jhipsterApp.ward.detail.title">Ward</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wardEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.ward.name">Name</Translate>
            </span>
          </dt>
          <dd>{wardEntity.name}</dd>
          <dt>
            <span id="lng">
              <Translate contentKey="jhipsterApp.ward.lng">Lng</Translate>
            </span>
          </dt>
          <dd>{wardEntity.lng}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="jhipsterApp.ward.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{wardEntity.lat}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.ward.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{wardEntity.createdDate ? <TextFormat value={wardEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.ward.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {wardEntity.lastModifiedDate ? <TextFormat value={wardEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterApp.ward.district">District</Translate>
          </dt>
          <dd>{wardEntity.district ? wardEntity.district.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/ward" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ward/${wardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WardDetail;
