import dayjs from 'dayjs';

export interface IProvince {
  id?: number;
  name?: string;
  lng?: number | null;
  lat?: number | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<IProvince> = {};
