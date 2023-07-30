import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIntervention } from 'app/shared/model/intervention.model';
import { getEntities as getInterventions } from 'app/entities/intervention/intervention.reducer';
import { IFunder } from 'app/shared/model/funder.model';
import { getEntities as getFunders } from 'app/entities/funder/funder.reducer';
import { IInterventionFunder } from 'app/shared/model/intervention-funder.model';
import { getEntity, updateEntity, createEntity, reset } from './intervention-funder.reducer';

export const InterventionFunderUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const interventions = useAppSelector(state => state.intervention.entities);
  const funders = useAppSelector(state => state.funder.entities);
  const interventionFunderEntity = useAppSelector(state => state.interventionFunder.entity);
  const loading = useAppSelector(state => state.interventionFunder.loading);
  const updating = useAppSelector(state => state.interventionFunder.updating);
  const updateSuccess = useAppSelector(state => state.interventionFunder.updateSuccess);

  const handleClose = () => {
    navigate('/intervention-funder' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInterventions({}));
    dispatch(getFunders({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...interventionFunderEntity,
      ...values,
      intervention: interventions.find(it => it.id.toString() === values.intervention.toString()),
      funder: funders.find(it => it.id.toString() === values.funder.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...interventionFunderEntity,
          intervention: interventionFunderEntity?.intervention?.id,
          funder: interventionFunderEntity?.funder?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.interventionFunder.home.createOrEditLabel" data-cy="InterventionFunderCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.interventionFunder.home.createOrEditLabel">Create or edit a InterventionFunder</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="intervention-funder-id"
                  label={translate('jhipsterApp.interventionFunder.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="intervention-funder-intervention"
                name="intervention"
                data-cy="intervention"
                label={translate('jhipsterApp.interventionFunder.intervention')}
                type="select"
              >
                <option value="" key="0" />
                {interventions
                  ? interventions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="intervention-funder-funder"
                name="funder"
                data-cy="funder"
                label={translate('jhipsterApp.interventionFunder.funder')}
                type="select"
              >
                <option value="" key="0" />
                {funders
                  ? funders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/intervention-funder" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InterventionFunderUpdate;
