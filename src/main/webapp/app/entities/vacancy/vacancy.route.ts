import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Vacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';
import { VacancyComponent } from './vacancy.component';
import { VacancyDetailComponent } from './vacancy-detail.component';
import { VacancyUpdateComponent } from './vacancy-update.component';
import { VacancyDeletePopupComponent } from './vacancy-delete-dialog.component';
import { IVacancy } from 'app/shared/model/vacancy.model';

@Injectable({ providedIn: 'root' })
export class VacancyResolve implements Resolve<IVacancy> {
  constructor(private service: VacancyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVacancy> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Vacancy>) => response.ok),
        map((vacancy: HttpResponse<Vacancy>) => vacancy.body)
      );
    }
    return of(new Vacancy());
  }
}

export const vacancyRoute: Routes = [
  {
    path: '',
    component: VacancyComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'blackvagasApp.vacancy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VacancyDetailComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'blackvagasApp.vacancy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VacancyUpdateComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'blackvagasApp.vacancy.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VacancyUpdateComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'blackvagasApp.vacancy.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const vacancyPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VacancyDeletePopupComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'blackvagasApp.vacancy.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
