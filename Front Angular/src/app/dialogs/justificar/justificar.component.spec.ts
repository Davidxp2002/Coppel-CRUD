import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JustificarComponent } from './justificar.component';

describe('JustificarComponent', () => {
  let component: JustificarComponent;
  let fixture: ComponentFixture<JustificarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JustificarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JustificarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
