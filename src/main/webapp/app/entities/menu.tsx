import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/intervention">
        <Translate contentKey="global.menu.entities.intervention" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/intervention-type">
        <Translate contentKey="global.menu.entities.interventionType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/project">
        <Translate contentKey="global.menu.entities.project" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/funder">
        <Translate contentKey="global.menu.entities.funder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sector">
        <Translate contentKey="global.menu.entities.sector" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/province">
        <Translate contentKey="global.menu.entities.province" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/district">
        <Translate contentKey="global.menu.entities.district" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ward">
        <Translate contentKey="global.menu.entities.ward" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/intervention-funder">
        <Translate contentKey="global.menu.entities.interventionFunder" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/application-user">
        <Translate contentKey="global.menu.entities.applicationUser" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
