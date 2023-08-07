import dayjs from 'dayjs';

export interface IProject {
  id?: number;
  name?: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<IProject> = {};
