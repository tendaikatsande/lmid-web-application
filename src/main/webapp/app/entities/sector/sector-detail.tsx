import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sector.reducer';

export const SectorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sectorEntity = useAppSelector(state => state.sector.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sectorDetailsHeading">
          <Translate contentKey="jhipsterApp.sector.detail.title">Sector</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sectorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="jhipsterApp.sector.name">Name</Translate>
            </span>
          </dt>
          <dd>{sectorEntity.name}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="jhipsterApp.sector.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{sectorEntity.createdDate ? <TextFormat value={sectorEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="jhipsterApp.sector.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {sectorEntity.lastModifiedDate ? (
              <TextFormat value={sectorEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/sector" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sector/${sectorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SectorDetail;
