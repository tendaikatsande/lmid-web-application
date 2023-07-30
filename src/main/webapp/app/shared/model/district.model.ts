import dayjs from 'dayjs';
import { IProvince } from 'app/shared/model/province.model';

export interface IDistrict {
  id?: number;
  name?: string;
  lng?: number | null;
  lat?: number | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
  province?: IProvince | null;
}

export const defaultValue: Readonly<IDistrict> = {};
