import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmmessageComponent } from './confirmmessage.component';

describe('ConfirmmessageComponent', () => {
  let component: ConfirmmessageComponent;
  let fixture: ComponentFixture<ConfirmmessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmmessageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmmessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
