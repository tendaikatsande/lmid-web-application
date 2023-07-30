import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInterventionType } from 'app/shared/model/intervention-type.model';
import { getEntities as getInterventionTypes } from 'app/entities/intervention-type/intervention-type.reducer';
import { IProject } from 'app/shared/model/project.model';
import { getEntities as getProjects } from 'app/entities/project/project.reducer';
import { IDistrict } from 'app/shared/model/district.model';
import { getEntities as getDistricts } from 'app/entities/district/district.reducer';
import { IWard } from 'app/shared/model/ward.model';
import { getEntities as getWards } from 'app/entities/ward/ward.reducer';
import { IIntervention } from 'app/shared/model/intervention.model';
import { getEntity, updateEntity, createEntity, reset } from './intervention.reducer';

export const InterventionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const interventionTypes = useAppSelector(state => state.interventionType.entities);
  const projects = useAppSelector(state => state.project.entities);
  const districts = useAppSelector(state => state.district.entities);
  const wards = useAppSelector(state => state.ward.entities);
  const interventionEntity = useAppSelector(state => state.intervention.entity);
  const loading = useAppSelector(state => state.intervention.loading);
  const updating = useAppSelector(state => state.intervention.updating);
  const updateSuccess = useAppSelector(state => state.intervention.updateSuccess);

  const handleClose = () => {
    navigate('/intervention' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInterventionTypes({}));
    dispatch(getProjects({}));
    dispatch(getDistricts({}));
    dispatch(getWards({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...interventionEntity,
      ...values,
      type: interventionTypes.find(it => it.id.toString() === values.type.toString()),
      project: projects.find(it => it.id.toString() === values.project.toString()),
      location: districts.find(it => it.id.toString() === values.location.toString()),
      ward: wards.find(it => it.id.toString() === values.ward.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...interventionEntity,
          createdDate: convertDateTimeFromServer(interventionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(interventionEntity.lastModifiedDate),
          type: interventionEntity?.type?.id,
          project: interventionEntity?.project?.id,
          location: interventionEntity?.location?.id,
          ward: interventionEntity?.ward?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.intervention.home.createOrEditLabel" data-cy="InterventionCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.intervention.home.createOrEditLabel">Create or edit a Intervention</Translate>
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
                  id="intervention-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterApp.intervention.startDate')}
                id="intervention-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.targetArea')}
                id="intervention-targetArea"
                name="targetArea"
                data-cy="targetArea"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.targetDate')}
                id="intervention-targetDate"
                name="targetDate"
                data-cy="targetDate"
                type="date"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.achievedArea')}
                id="intervention-achievedArea"
                name="achievedArea"
                data-cy="achievedArea"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.costOfIntervention')}
                id="intervention-costOfIntervention"
                name="costOfIntervention"
                data-cy="costOfIntervention"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.createdDate')}
                id="intervention-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterApp.intervention.lastModifiedDate')}
                id="intervention-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="intervention-type"
                name="type"
                data-cy="type"
                label={translate('jhipsterApp.intervention.type')}
                type="select"
              >
                <option value="" key="0" />
                {interventionTypes
                  ? interventionTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="intervention-project"
                name="project"
                data-cy="project"
                label={translate('jhipsterApp.intervention.project')}
                type="select"
              >
                <option value="" key="0" />
                {projects
                  ? projects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="intervention-location"
                name="location"
                data-cy="location"
                label={translate('jhipsterApp.intervention.location')}
                type="select"
              >
                <option value="" key="0" />
                {districts
                  ? districts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="intervention-ward"
                name="ward"
                data-cy="ward"
                label={translate('jhipsterApp.intervention.ward')}
                type="select"
              >
                <option value="" key="0" />
                {wards
                  ? wards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/intervention" replace color="info">
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

export default InterventionUpdate;
