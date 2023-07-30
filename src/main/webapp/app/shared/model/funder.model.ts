import dayjs from 'dayjs';
import { ISector } from 'app/shared/model/sector.model';

export interface IFunder {
  id?: number;
  name?: string;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  sector?: ISector | null;
}

export const defaultValue: Readonly<IFunder> = {};
