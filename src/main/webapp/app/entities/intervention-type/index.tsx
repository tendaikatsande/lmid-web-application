import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InterventionType from './intervention-type';
import InterventionTypeDetail from './intervention-type-detail';
import InterventionTypeUpdate from './intervention-type-update';
import InterventionTypeDeleteDialog from './intervention-type-delete-dialog';

const InterventionTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InterventionType />} />
    <Route path="new" element={<InterventionTypeUpdate />} />
    <Route path=":id">
      <Route index element={<InterventionTypeDetail />} />
      <Route path="edit" element={<InterventionTypeUpdate />} />
      <Route path="delete" element={<InterventionTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InterventionTypeRoutes;
