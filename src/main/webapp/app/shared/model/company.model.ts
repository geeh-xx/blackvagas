import { IVacancy } from 'app/shared/model/vacancy.model';

export interface ICompany {
  id?: number;
  name?: string;
  email?: string;
  adress?: string;
  vacancys?: IVacancy[];
}

export class Company implements ICompany {
  constructor(public id?: number, public name?: string, public email?: string, public adress?: string, public vacancys?: IVacancy[]) {}
}
