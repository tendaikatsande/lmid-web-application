import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Province from './province';
import ProvinceDetail from './province-detail';
import ProvinceUpdate from './province-update';
import ProvinceDeleteDialog from './province-delete-dialog';

const ProvinceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Province />} />
    <Route path="new" element={<ProvinceUpdate />} />
    <Route path=":id">
      <Route index element={<ProvinceDetail />} />
      <Route path="edit" element={<ProvinceUpdate />} />
      <Route path="delete" element={<ProvinceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProvinceRoutes;
