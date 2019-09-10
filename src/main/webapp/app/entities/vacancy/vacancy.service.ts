import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVacancy } from 'app/shared/model/vacancy.model';

type EntityResponseType = HttpResponse<IVacancy>;
type EntityArrayResponseType = HttpResponse<IVacancy[]>;

@Injectable({ providedIn: 'root' })
export class VacancyService {
  public resourceUrl = SERVER_API_URL + 'api/vacancies';

  constructor(protected http: HttpClient) {}

  create(vacancy: IVacancy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vacancy);
    return this.http
      .post<IVacancy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vacancy: IVacancy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vacancy);
    return this.http
      .put<IVacancy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVacancy>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVacancy[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(vacancy: IVacancy): IVacancy {
    const copy: IVacancy = Object.assign({}, vacancy, {
      dateLimit: vacancy.dateLimit != null && vacancy.dateLimit.isValid() ? vacancy.dateLimit.format(DATE_FORMAT) : null,
      datePublication:
        vacancy.datePublication != null && vacancy.datePublication.isValid() ? vacancy.datePublication.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLimit = res.body.dateLimit != null ? moment(res.body.dateLimit) : null;
      res.body.datePublication = res.body.datePublication != null ? moment(res.body.datePublication) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vacancy: IVacancy) => {
        vacancy.dateLimit = vacancy.dateLimit != null ? moment(vacancy.dateLimit) : null;
        vacancy.datePublication = vacancy.datePublication != null ? moment(vacancy.datePublication) : null;
      });
    }
    return res;
  }
}
