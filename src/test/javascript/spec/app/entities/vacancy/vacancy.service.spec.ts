/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { VacancyService } from 'app/entities/vacancy/vacancy.service';
import { IVacancy, Vacancy } from 'app/shared/model/vacancy.model';

describe('Service Tests', () => {
  describe('Vacancy Service', () => {
    let injector: TestBed;
    let service: VacancyService;
    let httpMock: HttpTestingController;
    let elemDefault: IVacancy;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(VacancyService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Vacancy(0, 'AAAAAAA', 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateLimit: currentDate.format(DATE_FORMAT),
            datePublication: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Vacancy', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateLimit: currentDate.format(DATE_FORMAT),
            datePublication: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateLimit: currentDate,
            datePublication: currentDate
          },
          returnedFromService
        );
        service
          .create(new Vacancy(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Vacancy', async () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            numberCandidats: 1,
            dateLimit: currentDate.format(DATE_FORMAT),
            datePublication: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateLimit: currentDate,
            datePublication: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Vacancy', async () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            numberCandidats: 1,
            dateLimit: currentDate.format(DATE_FORMAT),
            datePublication: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateLimit: currentDate,
            datePublication: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vacancy', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
