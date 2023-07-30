import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './funder.reducer';

export const FunderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const funderEntity = useAppSelector(state => state.funder.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="funderDetailsHeading">
          <Translate contentKey="jhipsterApp.funder.detail.title">Funder</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{funderEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.funder.name">Name</Translate>
            </span>
          </dt>
          <dd>{funderEntity.name}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.funder.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{funderEntity.createdDate ? <TextFormat value={funderEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.funder.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {funderEntity.lastModifiedDate ? (
              <TextFormat value={funderEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="jhipsterApp.funder.sector">Sector</Translate>
          </dt>
          <dd>{funderEntity.sector ? funderEntity.sector.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/funder" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/funder/${funderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FunderDetail;
