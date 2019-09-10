import { Moment } from 'moment';

export interface IVacancy {
  id?: number;
  description?: string;
  numberCandidats?: number;
  dateLimit?: Moment;
  datePublication?: Moment;
  companyId?: number;
  companyName?: string;
}

export class Vacancy implements IVacancy {
  constructor(
    public id?: number,
    public description?: string,
    public numberCandidats?: number,
    public dateLimit?: Moment,
    public datePublication?: Moment,
    public companyId?: number,
    public companyName?: string
  ) {}
}
