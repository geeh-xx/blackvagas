/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlackvagasTestModule } from '../../../test.module';
import { VacancyDetailComponent } from 'app/entities/vacancy/vacancy-detail.component';
import { Vacancy } from 'app/shared/model/vacancy.model';

describe('Component Tests', () => {
  describe('Vacancy Management Detail Component', () => {
    let comp: VacancyDetailComponent;
    let fixture: ComponentFixture<VacancyDetailComponent>;
    const route = ({ data: of({ vacancy: new Vacancy(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BlackvagasTestModule],
        declarations: [VacancyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VacancyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VacancyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vacancy).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
