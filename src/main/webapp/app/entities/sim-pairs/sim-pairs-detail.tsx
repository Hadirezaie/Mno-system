import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sim-pairs.reducer';

export const SimPairsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const simPairsEntity = useAppSelector(state => state.simPairs.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="simPairsDetailsHeading">Sim Pairs</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{simPairsEntity.id}</dd>
          <dt>
            <span id="msisdn">Msisdn</span>
          </dt>
          <dd>{simPairsEntity.msisdn}</dd>
          <dt>
            <span id="imsi">Imsi</span>
          </dt>
          <dd>{simPairsEntity.imsi}</dd>
          <dt>
            <span id="imeiNumber">Imei Number</span>
          </dt>
          <dd>{simPairsEntity.imeiNumber}</dd>
          <dt>
            <span id="sent">Sent</span>
          </dt>
          <dd>{simPairsEntity.sent ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/sim-pairs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sim-pairs/${simPairsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SimPairsDetail;
