import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';

@Component({
  selector: 'jhi-vacancy-delete-dialog',
  templateUrl: './vacancy-delete-dialog.component.html'
})
export class VacancyDeleteDialogComponent {
  vacancy: IVacancy;

  constructor(protected vacancyService: VacancyService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.vacancyService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'vacancyListModification',
        content: 'Deleted an vacancy'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-vacancy-delete-popup',
  template: ''
})
export class VacancyDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ vacancy }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(VacancyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.vacancy = vacancy;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/vacancy', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/vacancy', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
