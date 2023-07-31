import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './application-user.reducer';

export const ApplicationUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationUserDetailsHeading">
          <Translate contentKey="jhipsterApp.applicationUser.detail.title">ApplicationUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.id}</dd>
          <dt>
            <span id="provinceId">
              <Translate contentKey="jhipsterApp.applicationUser.provinceId">Province Id</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.provinceId}</dd>
          <dt>
            <span id="districtId">
              <Translate contentKey="jhipsterApp.applicationUser.districtId">District Id</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.districtId}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.applicationUser.user">User</Translate>
          </dt>
          <dd>{applicationUserEntity.user ? applicationUserEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.applicationUser.province">Province</Translate>
          </dt>
          <dd>{applicationUserEntity.province ? applicationUserEntity.province.provinceId : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterApp.applicationUser.district">District</Translate>
          </dt>
          <dd>{applicationUserEntity.district ? applicationUserEntity.district.districtId : ''}</dd>
        </dl>
        <Button tag={Link} to="/application-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-user/${applicationUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApplicationUserDetail;
