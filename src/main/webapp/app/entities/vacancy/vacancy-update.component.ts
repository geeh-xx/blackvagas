import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IVacancy, Vacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
  selector: 'jhi-vacancy-update',
  templateUrl: './vacancy-update.component.html'
})
export class VacancyUpdateComponent implements OnInit {
  isSaving: boolean;

  companies: ICompany[];
  dateLimitDp: any;
  datePublicationDp: any;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    numberCandidats: [null, [Validators.required]],
    dateLimit: [],
    datePublication: [],
    companyId: [],
    companyName: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected vacancyService: VacancyService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ vacancy }) => {
      this.updateForm(vacancy);
    });
    this.companyService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICompany[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICompany[]>) => response.body)
      )
      .subscribe((res: ICompany[]) => (this.companies = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(vacancy: IVacancy) {
    this.editForm.patchValue({
      id: vacancy.id,
      description: vacancy.description,
      numberCandidats: vacancy.numberCandidats,
      dateLimit: vacancy.dateLimit,
      datePublication: vacancy.datePublication,
      companyId: vacancy.companyId,
      companyName: vacancy.companyName
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const vacancy = this.createFromForm();
    if (vacancy.id !== undefined) {
      this.subscribeToSaveResponse(this.vacancyService.update(vacancy));
    } else {
      this.subscribeToSaveResponse(this.vacancyService.create(vacancy));
    }
  }

  private createFromForm(): IVacancy {
    return {
      ...new Vacancy(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      numberCandidats: this.editForm.get(['numberCandidats']).value,
      dateLimit: this.editForm.get(['dateLimit']).value,
      datePublication: this.editForm.get(['datePublication']).value,
      companyId: this.editForm.get(['companyId']).value,
      companyName: this.editForm.get(['companyName']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVacancy>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackCompanyById(index: number, item: ICompany) {
    return item.id;
  }
}
