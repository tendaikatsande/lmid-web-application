import intervention from 'app/entities/intervention/intervention.reducer';
import interventionType from 'app/entities/intervention-type/intervention-type.reducer';
import project from 'app/entities/project/project.reducer';
import funder from 'app/entities/funder/funder.reducer';
import sector from 'app/entities/sector/sector.reducer';
import province from 'app/entities/province/province.reducer';
import district from 'app/entities/district/district.reducer';
import ward from 'app/entities/ward/ward.reducer';
import interventionFunder from 'app/entities/intervention-funder/intervention-funder.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  intervention,
  interventionType,
  project,
  funder,
  sector,
  province,
  district,
  ward,
  interventionFunder,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
