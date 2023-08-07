import dayjs from 'dayjs';

export interface IProvince {
  id?: number;
  name?: string;
  lng?: number;
  lat?: number;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<IProvince> = {};
