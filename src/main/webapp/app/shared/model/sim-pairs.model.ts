export interface ISimPairs {
  id?: number;
  msisdn?: string | null;
  imsi?: string | null;
  imeiNumber?: number | null;
  sent?: boolean | null;
}

export const defaultValue: Readonly<ISimPairs> = {
  sent: false,
};
