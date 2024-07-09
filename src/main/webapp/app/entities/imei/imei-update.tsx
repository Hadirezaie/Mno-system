import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IImei } from 'app/shared/model/imei.model';
import { ImeiStatus } from 'app/shared/model/enumerations/imei-status.model';
import { getEntity, updateEntity, createEntity, reset } from './imei.reducer';

export const ImeiUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const imeiEntity = useAppSelector(state => state.imei.entity);
  const loading = useAppSelector(state => state.imei.loading);
  const updating = useAppSelector(state => state.imei.updating);
  const updateSuccess = useAppSelector(state => state.imei.updateSuccess);
  const imeiStatusValues = Object.keys(ImeiStatus);

  const handleClose = () => {
    navigate('/imei' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...imeiEntity,
      ...values,
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
          status: 'GRAY',
          ...imeiEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mnoSystemApp.imei.home.createOrEditLabel" data-cy="ImeiCreateUpdateHeading">
            Create or edit a Imei
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="imei-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Imei Number" id="imei-imeiNumber" name="imeiNumber" data-cy="imeiNumber" type="text" />
              <ValidatedField label="Status" id="imei-status" name="status" data-cy="status" type="select">
                {imeiStatusValues.map(imeiStatus => (
                  <option value={imeiStatus} key={imeiStatus}>
                    {imeiStatus}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/imei" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ImeiUpdate;
