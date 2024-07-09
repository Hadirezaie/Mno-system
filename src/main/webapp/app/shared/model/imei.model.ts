import { ImeiStatus } from 'app/shared/model/enumerations/imei-status.model';

export interface IImei {
  id?: number;
  imeiNumber?: number | null;
  status?: ImeiStatus | null;
}

export const defaultValue: Readonly<IImei> = {};
