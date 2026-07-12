import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DecrementCount } from './decrement-count';

describe('DecrementCount', () => {
  let component: DecrementCount;
  let fixture: ComponentFixture<DecrementCount>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DecrementCount],
    }).compileComponents();

    fixture = TestBed.createComponent(DecrementCount);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
