import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISimPairs } from 'app/shared/model/sim-pairs.model';
import { getEntity, updateEntity, createEntity, reset } from './sim-pairs.reducer';

export const SimPairsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const simPairsEntity = useAppSelector(state => state.simPairs.entity);
  const loading = useAppSelector(state => state.simPairs.loading);
  const updating = useAppSelector(state => state.simPairs.updating);
  const updateSuccess = useAppSelector(state => state.simPairs.updateSuccess);

  const handleClose = () => {
    navigate('/sim-pairs' + location.search);
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
      ...simPairsEntity,
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
          ...simPairsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mnoSystemApp.simPairs.home.createOrEditLabel" data-cy="SimPairsCreateUpdateHeading">
            Create or edit a Sim Pairs
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="sim-pairs-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Msisdn" id="sim-pairs-msisdn" name="msisdn" data-cy="msisdn" type="text" />
              <ValidatedField label="Imsi" id="sim-pairs-imsi" name="imsi" data-cy="imsi" type="text" />
              <ValidatedField label="Imei Number" id="sim-pairs-imeiNumber" name="imeiNumber" data-cy="imeiNumber" type="text" />
              <ValidatedField label="Sent" id="sim-pairs-sent" name="sent" data-cy="sent" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sim-pairs" replace color="info">
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

export default SimPairsUpdate;
