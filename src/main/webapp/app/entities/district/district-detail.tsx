import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './district.reducer';

export const DistrictDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const districtEntity = useAppSelector(state => state.district.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="districtDetailsHeading">
          <Translate contentKey="jhipsterApp.district.detail.title">District</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{districtEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.district.name">Name</Translate>
            </span>
          </dt>
          <dd>{districtEntity.name}</dd>
          <dt>
            <span id="lng">
              <Translate contentKey="jhipsterApp.district.lng">Lng</Translate>
            </span>
          </dt>
          <dd>{districtEntity.lng}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="jhipsterApp.district.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{districtEntity.lat}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.district.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {districtEntity.createdDate ? <TextFormat value={districtEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.district.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {districtEntity.lastModifiedDate ? (
              <TextFormat value={districtEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterApp.district.province">Province</Translate>
          </dt>
          <dd>{districtEntity.province ? districtEntity.province.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/district" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/district/${districtEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DistrictDetail;
