import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InterventionFunder from './intervention-funder';
import InterventionFunderDetail from './intervention-funder-detail';
import InterventionFunderUpdate from './intervention-funder-update';
import InterventionFunderDeleteDialog from './intervention-funder-delete-dialog';

const InterventionFunderRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InterventionFunder />} />
    <Route path="new" element={<InterventionFunderUpdate />} />
    <Route path=":id">
      <Route index element={<InterventionFunderDetail />} />
      <Route path="edit" element={<InterventionFunderUpdate />} />
      <Route path="delete" element={<InterventionFunderDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InterventionFunderRoutes;
