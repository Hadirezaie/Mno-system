import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SimPairs from './sim-pairs';
import SimPairsDetail from './sim-pairs-detail';
import SimPairsUpdate from './sim-pairs-update';
import SimPairsDeleteDialog from './sim-pairs-delete-dialog';

const SimPairsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SimPairs />} />
    <Route path="new" element={<SimPairsUpdate />} />
    <Route path=":id">
      <Route index element={<SimPairsDetail />} />
      <Route path="edit" element={<SimPairsUpdate />} />
      <Route path="delete" element={<SimPairsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SimPairsRoutes;
