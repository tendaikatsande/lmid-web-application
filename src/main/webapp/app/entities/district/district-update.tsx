import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProvince } from 'app/shared/model/province.model';
import { getEntities as getProvinces } from 'app/entities/province/province.reducer';
import { IDistrict } from 'app/shared/model/district.model';
import { getEntity, updateEntity, createEntity, reset } from './district.reducer';

export const DistrictUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const provinces = useAppSelector(state => state.province.entities);
  const districtEntity = useAppSelector(state => state.district.entity);
  const loading = useAppSelector(state => state.district.loading);
  const updating = useAppSelector(state => state.district.updating);
  const updateSuccess = useAppSelector(state => state.district.updateSuccess);

  const handleClose = () => {
    navigate('/district' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProvinces({}));
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
      ...districtEntity,
      ...values,
      province: provinces.find(it => it.id.toString() === values.province.toString()),
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
          ...districtEntity,
          createdDate: convertDateTimeFromServer(districtEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(districtEntity.lastModifiedDate),
          province: districtEntity?.province?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.district.home.createOrEditLabel" data-cy="DistrictCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.district.home.createOrEditLabel">Create or edit a District</Translate>
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
                  id="district-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterApp.district.name')}
                id="district-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('jhipsterApp.district.lng')} id="district-lng" name="lng" data-cy="lng" type="text" />
              <ValidatedField label={translate('jhipsterApp.district.lat')} id="district-lat" name="lat" data-cy="lat" type="text" />
              <ValidatedField
                label={translate('jhipsterApp.district.createdDate')}
                id="district-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterApp.district.lastModifiedDate')}
                id="district-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="district-province"
                name="province"
                data-cy="province"
                label={translate('jhipsterApp.district.province')}
                type="select"
              >
                <option value="" key="0" />
                {provinces
                  ? provinces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/district" replace color="info">
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

export default DistrictUpdate;
