import dayjs from 'dayjs';

export interface IProject {
  id?: number;
  name?: string;
  description?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<IProject> = {};
