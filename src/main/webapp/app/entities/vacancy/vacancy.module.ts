import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BlackvagasSharedModule } from 'app/shared';
import {
  VacancyComponent,
  VacancyDetailComponent,
  VacancyUpdateComponent,
  VacancyDeletePopupComponent,
  VacancyDeleteDialogComponent,
  vacancyRoute,
  vacancyPopupRoute
} from './';

const ENTITY_STATES = [...vacancyRoute, ...vacancyPopupRoute];

@NgModule({
  imports: [BlackvagasSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    VacancyComponent,
    VacancyDetailComponent,
    VacancyUpdateComponent,
    VacancyDeleteDialogComponent,
    VacancyDeletePopupComponent
  ],
  entryComponents: [VacancyComponent, VacancyUpdateComponent, VacancyDeleteDialogComponent, VacancyDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BlackvagasVacancyModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
