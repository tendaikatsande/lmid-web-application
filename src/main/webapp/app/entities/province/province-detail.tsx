import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './province.reducer';

export const ProvinceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const provinceEntity = useAppSelector(state => state.province.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="provinceDetailsHeading">
          <Translate contentKey="jhipsterApp.province.detail.title">Province</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{provinceEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.province.name">Name</Translate>
            </span>
          </dt>
          <dd>{provinceEntity.name}</dd>
          <dt>
            <span id="lng">
              <Translate contentKey="jhipsterApp.province.lng">Lng</Translate>
            </span>
          </dt>
          <dd>{provinceEntity.lng}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="jhipsterApp.province.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{provinceEntity.lat}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.province.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {provinceEntity.createdDate ? <TextFormat value={provinceEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.province.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {provinceEntity.lastModifiedDate ? (
              <TextFormat value={provinceEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/province" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/province/${provinceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProvinceDetail;
