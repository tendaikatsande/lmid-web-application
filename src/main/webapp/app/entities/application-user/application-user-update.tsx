import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IProvince } from 'app/shared/model/province.model';
import { getEntities as getProvinces } from 'app/entities/province/province.reducer';
import { IDistrict } from 'app/shared/model/district.model';
import { getEntities as getDistricts } from 'app/entities/district/district.reducer';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntity, updateEntity, createEntity, reset } from './application-user.reducer';

export const ApplicationUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const provinces = useAppSelector(state => state.province.entities);
  const districts = useAppSelector(state => state.district.entities);
  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  const loading = useAppSelector(state => state.applicationUser.loading);
  const updating = useAppSelector(state => state.applicationUser.updating);
  const updateSuccess = useAppSelector(state => state.applicationUser.updateSuccess);

  const handleClose = () => {
    navigate('/application-user' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getProvinces({}));
    dispatch(getDistricts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...applicationUserEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      province: provinces.find(it => it.id.toString() === values.province.toString()),
      district: districts.find(it => it.id.toString() === values.district.toString()),
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
          ...applicationUserEntity,
          user: applicationUserEntity?.user?.id,
          province: applicationUserEntity?.province?.id,
          district: applicationUserEntity?.district?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterApp.applicationUser.home.createOrEditLabel" data-cy="ApplicationUserCreateUpdateHeading">
            <Translate contentKey="jhipsterApp.applicationUser.home.createOrEditLabel">Create or edit a ApplicationUser</Translate>
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
                  id="application-user-id"
                  label={translate('jhipsterApp.applicationUser.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="application-user-user"
                name="user"
                data-cy="user"
                label={translate('jhipsterApp.applicationUser.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="application-user-province"
                name="province"
                data-cy="province"
                label={translate('jhipsterApp.applicationUser.province')}
                type="select"
              >
                <option value="" key="0" />
                {provinces
                  ? provinces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.provinceId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="application-user-district"
                name="district"
                data-cy="district"
                label={translate('jhipsterApp.applicationUser.district')}
                type="select"
              >
                <option value="" key="0" />
                {districts
                  ? districts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.districtId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/application-user" replace color="info">
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

export default ApplicationUserUpdate;
