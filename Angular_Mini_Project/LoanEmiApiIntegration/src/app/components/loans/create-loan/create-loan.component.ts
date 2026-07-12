import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { FormActionButtonsComponent } from '../../shared/buttons/form-action-buttons.component';
import { SelectInputComponent } from '../../shared/forms/inputs/select-input.component';
import { TextInputComponent } from '../../shared/forms/inputs/text-input.component';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { CreateLoanRequest, EmiCalculationResponse, LoanService } from '../../../services/loan/loan.service';
import { LOAN_TYPE_OPTIONS, normalizeLoanType } from '../../../constants/loan-types';

@Component({
  selector: 'app-create-loan',
  standalone: true,
  imports: [FormsModule, PageHeaderComponent, TextInputComponent, SelectInputComponent, FormActionButtonsComponent],
  templateUrl: './create-loan.component.html',
  styleUrls: ['./create-loan.component.css'],
})
export class CreateLoanComponent implements OnInit, OnDestroy {
  private readonly loanService = inject(LoanService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private calculationTimer: ReturnType<typeof setTimeout> | null = null;
  private calculationRequestId = 0;

  readonly loanTypes = LOAN_TYPE_OPTIONS.map((item) => item.code);
  readonly sourceApplicationId = signal<number | null>(null);
  readonly createdFromSourceApplication = signal(false);

  loan: CreateLoanRequest = {
    loanType: 'HOME',
    principalAmount: 0,
    annualInterestRate: 0,
    tenureMonths: 0,
    customerId: 0
  };

  readonly status = signal({
    loading: false,
    success: false,
    error: '',
    message: ''
  });

  readonly emiPreview = signal<{
    loading: boolean;
    error: string;
    data: EmiCalculationResponse | null;
  }>({
    loading: false,
    error: '',
    data: null
  });

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      if (params['loanType']) {
        this.loan.loanType = normalizeLoanType(String(params['loanType']));
      }

      const customerId = Number(params['customerId']);
      if (Number.isFinite(customerId) && customerId > 0) {
        this.loan.customerId = customerId;
      }

      const principalAmount = Number(params['principalAmount']);
      if (Number.isFinite(principalAmount) && principalAmount > 0) {
        this.loan.principalAmount = principalAmount;
      }

      const annualInterestRate = Number(params['annualInterestRate']);
      if (Number.isFinite(annualInterestRate) && annualInterestRate > 0) {
        this.loan.annualInterestRate = annualInterestRate;
      }

      const tenureMonths = Number(params['tenureMonths']);
      if (Number.isFinite(tenureMonths) && tenureMonths > 0) {
        this.loan.tenureMonths = tenureMonths;
      }

      const applicationId = Number(params['applicationId']);
      if (Number.isFinite(applicationId) && applicationId > 0) {
        this.sourceApplicationId.set(applicationId);
        this.createdFromSourceApplication.set(false);
      } else {
        this.sourceApplicationId.set(null);
        this.createdFromSourceApplication.set(false);
      }

      this.scheduleAutoCalculate();
    });
  }

  ngOnDestroy(): void {
    this.clearCalculationTimer();
  }

  submit(): void {
    if (this.status().loading) {
      return;
    }

    if (this.sourceApplicationId() && this.createdFromSourceApplication()) {
      this.status.set({
        loading: false,
        success: false,
        error: 'Loan already created for this approved request in this session. Avoid duplicate submissions.',
        message: ''
      });
      return;
    }

    const validationError = this.validateForm();
    if (validationError) {
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.status.set({ loading: true, success: false, error: '', message: '' });
    this.loanService.createLoan(this.loan).subscribe({
      next: () => {
        if (this.sourceApplicationId()) {
          this.createdFromSourceApplication.set(true);
        }
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Loan created successfully.'
        });
        this.reset();
        this.refresh();
      },
      error: (error) => {
        this.status.set({
          loading: false,
          success: false,
          error: error?.error?.message || error?.message || 'Something went wrong.',
          message: ''
        });
      }
    });
  }

  reset(): void {
    this.loan = {
      loanType: 'HOME',
      principalAmount: 0,
      annualInterestRate: 0,
      tenureMonths: 0,
      customerId: 0
    };
    this.resetPreview();
  }

  refresh(): void {
    // Reserved for parent list refresh wiring.
  }

  onPrincipalChange(value: number): void {
    this.loan.principalAmount = value;
    this.scheduleAutoCalculate();
  }

  onRateChange(value: number): void {
    this.loan.annualInterestRate = value;
    this.scheduleAutoCalculate();
  }

  onTenureChange(value: number): void {
    this.loan.tenureMonths = value;
    this.scheduleAutoCalculate();
  }

  calculateEmi(): void {
    if (!this.hasValidCalculationInputs()) {
      this.emiPreview.set({
        loading: false,
        error: 'Enter principal amount, interest rate, and tenure to calculate EMI.',
        data: null
      });
      return;
    }

    const requestId = ++this.calculationRequestId;
    this.emiPreview.set({ loading: true, error: '', data: null });

    this.loanService.calculateEmi({
      principalAmount: this.loan.principalAmount,
      annualInterestRate: this.loan.annualInterestRate,
      tenureMonths: this.loan.tenureMonths
    }).subscribe({
      next: (response) => {
        if (requestId !== this.calculationRequestId) {
          return;
        }
        this.emiPreview.set({ loading: false, error: '', data: response });
      },
      error: (error) => {
        if (requestId !== this.calculationRequestId) {
          return;
        }
        this.emiPreview.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Failed to calculate EMI.',
          data: null
        });
      }
    });
  }

  formatCurrency(value: number): string {
    return `INR ${value.toLocaleString('en-IN', { maximumFractionDigits: 2 })}`;
  }

  private scheduleAutoCalculate(): void {
    this.clearCalculationTimer();

    if (!this.hasValidCalculationInputs()) {
      this.resetPreview();
      return;
    }

    this.calculationTimer = setTimeout(() => {
      this.calculateEmi();
    }, 350);
  }

  private hasValidCalculationInputs(): boolean {
    return !!this.loan.principalAmount && this.loan.principalAmount > 0 &&
      !!this.loan.annualInterestRate && this.loan.annualInterestRate > 0 && this.loan.annualInterestRate <= 100 &&
      !!this.loan.tenureMonths && this.loan.tenureMonths > 0;
  }

  private resetPreview(): void {
    this.clearCalculationTimer();
    this.emiPreview.set({ loading: false, error: '', data: null });
  }

  private clearCalculationTimer(): void {
    if (!this.calculationTimer) {
      return;
    }
    clearTimeout(this.calculationTimer);
    this.calculationTimer = null;
  }

  private validateForm(): string {
    if (!this.loan.loanType) {
      return 'Loan type is required.';
    }

    if (!this.loan.customerId || this.loan.customerId <= 0) {
      return 'Enter a valid customer ID.';
    }

    if (!this.loan.principalAmount || this.loan.principalAmount <= 0) {
      return 'Enter a valid principal amount.';
    }

    if (!this.loan.annualInterestRate || this.loan.annualInterestRate <= 0 || this.loan.annualInterestRate > 100) {
      return 'Enter a valid annual interest rate between 0 and 100.';
    }

    if (!this.loan.tenureMonths || this.loan.tenureMonths <= 0) {
      return 'Enter a valid tenure in months.';
    }

    return '';
  }

}

