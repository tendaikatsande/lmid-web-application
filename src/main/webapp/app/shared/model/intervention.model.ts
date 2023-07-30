import dayjs from 'dayjs';
import { IInterventionType } from 'app/shared/model/intervention-type.model';
import { IProject } from 'app/shared/model/project.model';
import { IDistrict } from 'app/shared/model/district.model';
import { IWard } from 'app/shared/model/ward.model';

export interface IIntervention {
  id?: number;
  startDate?: string | null;
  targetArea?: number | null;
  targetDate?: string | null;
  achievedArea?: number | null;
  costOfIntervention?: number | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  type?: IInterventionType | null;
  project?: IProject | null;
  location?: IDistrict | null;
  ward?: IWard | null;
}

export const defaultValue: Readonly<IIntervention> = {};
