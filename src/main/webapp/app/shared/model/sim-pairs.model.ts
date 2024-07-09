import { IImei } from 'app/shared/model/imei.model';

export interface ISimPairs {
  id?: number;
  msisdn?: string | null;
  imsi?: string | null;
  sent?: boolean | null;
  imei?: IImei | null;
}

export const defaultValue: Readonly<ISimPairs> = {
  sent: false,
};
