import { IIntervention } from 'app/shared/model/intervention.model';
import { IFunder } from 'app/shared/model/funder.model';

export interface IInterventionFunder {
  id?: number;
  intervention?: IIntervention | null;
  funder?: IFunder | null;
}

export const defaultValue: Readonly<IInterventionFunder> = {};
