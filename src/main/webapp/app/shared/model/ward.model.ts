import dayjs from 'dayjs';
import { IDistrict } from 'app/shared/model/district.model';

export interface IWard {
  id?: number;
  name?: string;
  lng?: number;
  lat?: number;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  district?: IDistrict | null;
}

export const defaultValue: Readonly<IWard> = {};
