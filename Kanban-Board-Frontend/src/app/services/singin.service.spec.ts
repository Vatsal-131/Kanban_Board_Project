import { TestBed } from '@angular/core/testing';

import { SigninService } from './singin.service';

describe('SinginService', () => {
  let service: SigninService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SigninService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
