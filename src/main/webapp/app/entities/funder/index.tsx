import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Funder from './funder';
import FunderDetail from './funder-detail';
import FunderUpdate from './funder-update';
import FunderDeleteDialog from './funder-delete-dialog';

const FunderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Funder />} />
    <Route path="new" element={<FunderUpdate />} />
    <Route path=":id">
      <Route index element={<FunderDetail />} />
      <Route path="edit" element={<FunderUpdate />} />
      <Route path="delete" element={<FunderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FunderRoutes;
