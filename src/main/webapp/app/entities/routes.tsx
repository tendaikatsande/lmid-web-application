import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Intervention from './intervention';
import InterventionType from './intervention-type';
import Project from './project';
import Funder from './funder';
import Sector from './sector';
import Province from './province';
import District from './district';
import Ward from './ward';
import InterventionFunder from './intervention-funder';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="intervention/*" element={<Intervention />} />
        <Route path="intervention-type/*" element={<InterventionType />} />
        <Route path="project/*" element={<Project />} />
        <Route path="funder/*" element={<Funder />} />
        <Route path="sector/*" element={<Sector />} />
        <Route path="province/*" element={<Province />} />
        <Route path="district/*" element={<District />} />
        <Route path="ward/*" element={<Ward />} />
        <Route path="intervention-funder/*" element={<InterventionFunder />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
