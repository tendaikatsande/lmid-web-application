import dayjs from 'dayjs';

export interface IInterventionType {
  id?: number;
  name?: string;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
}

export const defaultValue: Readonly<IInterventionType> = {};
