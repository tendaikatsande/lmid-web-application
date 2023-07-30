import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ward from './ward';
import WardDetail from './ward-detail';
import WardUpdate from './ward-update';
import WardDeleteDialog from './ward-delete-dialog';

const WardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ward />} />
    <Route path="new" element={<WardUpdate />} />
    <Route path=":id">
      <Route index element={<WardDetail />} />
      <Route path="edit" element={<WardUpdate />} />
      <Route path="delete" element={<WardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WardRoutes;
