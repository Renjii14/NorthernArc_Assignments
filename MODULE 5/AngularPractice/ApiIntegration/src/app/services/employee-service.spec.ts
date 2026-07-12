import { TestBed } from '@angular/core/testing';

import { EmployeeServices } from './employee-service';

describe('EmployeeService', () => {
  let service: EmployeeServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
