import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sector from './sector';
import SectorDetail from './sector-detail';
import SectorUpdate from './sector-update';
import SectorDeleteDialog from './sector-delete-dialog';

const SectorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sector />} />
    <Route path="new" element={<SectorUpdate />} />
    <Route path=":id">
      <Route index element={<SectorDetail />} />
      <Route path="edit" element={<SectorUpdate />} />
      <Route path="delete" element={<SectorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SectorRoutes;
