import dayjs from 'dayjs';

export interface ISector {
  id?: number;
  name?: string;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<ISector> = {};
