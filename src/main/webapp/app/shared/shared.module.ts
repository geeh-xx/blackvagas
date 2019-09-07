import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BlackvagasSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [BlackvagasSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [BlackvagasSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BlackvagasSharedModule {
  static forRoot() {
    return {
      ngModule: BlackvagasSharedModule
    };
  }
}
