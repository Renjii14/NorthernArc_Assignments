import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncrementCount } from './increment-count';

describe('IncrementCount', () => {
  let component: IncrementCount;
  let fixture: ComponentFixture<IncrementCount>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncrementCount],
    }).compileComponents();

    fixture = TestBed.createComponent(IncrementCount);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
