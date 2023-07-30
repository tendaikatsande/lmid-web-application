import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Intervention from './intervention';
import InterventionDetail from './intervention-detail';
import InterventionUpdate from './intervention-update';
import InterventionDeleteDialog from './intervention-delete-dialog';

const InterventionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Intervention />} />
    <Route path="new" element={<InterventionUpdate />} />
    <Route path=":id">
      <Route index element={<InterventionDetail />} />
      <Route path="edit" element={<InterventionUpdate />} />
      <Route path="delete" element={<InterventionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InterventionRoutes;
