import { IUser } from 'app/shared/model/user.model';
import { IProvince } from 'app/shared/model/province.model';
import { IDistrict } from 'app/shared/model/district.model';

export interface IApplicationUser {
  id?: number;
  provinceId?: number | null;
  districtId?: number | null;
  user?: IUser | null;
  province?: IProvince | null;
  district?: IDistrict | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
