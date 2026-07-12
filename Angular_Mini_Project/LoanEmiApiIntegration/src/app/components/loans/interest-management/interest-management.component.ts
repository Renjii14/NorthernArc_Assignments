import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TextInputComponent } from '../../shared/forms/inputs/text-input.component';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { LoanService } from '../../../services/loan/loan.service';
import { LOAN_TYPE_OPTIONS, LoanTypeOption } from '../../../constants/loan-types';

@Component({
  selector: 'app-interest-management',
  standalone: true,
  imports: [FormsModule, PageHeaderComponent, TextInputComponent],
  templateUrl: './interest-management.component.html',
  styleUrls: ['./interest-management.component.css'],
})
export class InterestManagementComponent implements OnInit {
  private readonly loanService = inject(LoanService);
  private readonly route = inject(ActivatedRoute);

  readonly bulkLoanTypes: LoanTypeOption[] = LOAN_TYPE_OPTIONS.map((item) => ({
    code: item.code,
    label: item.label,
    icon: item.icon || 'bi-bank'
  }));

  selectedLoanTypes: string[] = ['HOME', 'PERSONAL'];
  bulkRate = 10;
  individualLoanId: number | null = null;
  individualRate: number | null = null;

  readonly showConfirmModal = signal(false);
  readonly confirmMessage = signal('');
  readonly status = signal({ loading: false, success: false, error: '', message: '' });

  ngOnInit(): void {
    const loanIdParam = Number(this.route.snapshot.queryParamMap.get('loanId'));
    const rateParam = Number(this.route.snapshot.queryParamMap.get('rate'));

    if (!Number.isNaN(loanIdParam) && loanIdParam > 0) {
      this.individualLoanId = loanIdParam;
    }

    if (!Number.isNaN(rateParam) && rateParam > 0) {
      this.individualRate = rateParam;
      this.status.set({
        loading: false,
        success: true,
        error: '',
        message: `Loan #${loanIdParam} opened for direct interest update.`
      });
    }
  }

  isLoanTypeSelected(code: string): boolean {
    return this.selectedLoanTypes.includes(code);
  }

  toggleLoanType(code: string): void {
    if (this.isLoanTypeSelected(code)) {
      this.selectedLoanTypes = this.selectedLoanTypes.filter((item) => item !== code);
      return;
    }
    this.selectedLoanTypes = [...this.selectedLoanTypes, code];
  }

  projectedImpactLabel(): string {
    const selected = this.selectedLoanTypes.length;
    const rate = this.bulkRate || 0;
    if (selected === 0 || rate <= 0) {
      return 'Configure loan types and a valid rate to preview impact.';
    }

    const estimatedLoans = selected * 240;
    return `Estimated impacted loans: ${estimatedLoans.toLocaleString('en-IN')}`;
  }

  currentRateLabel(): string {
    return 'Current average rate: 10.25%';
  }

  newRateLabel(): string {
    if (!this.bulkRate || this.bulkRate <= 0) {
      return 'New proposed rate: N/A';
    }
    return `New proposed rate: ${this.bulkRate.toFixed(2)}%`;
  }

  affectedLoansLabel(): string {
    const selected = this.selectedLoanTypes.length;
    if (selected === 0) {
      return 'Affected loans: N/A';
    }
    return `Affected loans (estimated): ${(selected * 240).toLocaleString('en-IN')}`;
  }

  projectedDeltaLabel(): string {
    const selected = this.selectedLoanTypes.length;
    const rate = this.bulkRate || 0;
    if (selected === 0 || rate <= 0) {
      return 'Projected monthly delta: N/A';
    }

    const deltaLakhs = (selected * rate * 0.72).toFixed(1);
    return `Projected monthly delta: INR ${deltaLakhs} L`;
  }

  submitBulkPreview(): void {
    const validationError = this.validateBulkRequest();
    if (validationError) {
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.confirmMessage.set(
      `Apply ${this.bulkRate}% annual interest to ${this.selectedLoanTypes.length} selected loan categories?`
    );
    this.showConfirmModal.set(true);
  }

  submitBulkUpdate(): void {
    const validationError = this.validateBulkRequest();
    if (validationError) {
      this.showConfirmModal.set(false);
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.showConfirmModal.set(false);
    this.status.set({ loading: true, success: false, error: '', message: '' });

    this.loanService.reviseInterestRates(this.selectedLoanTypes, this.bulkRate).subscribe({
      next: (updatedCount) => {
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: `Interest rates updated for ${updatedCount} loans.`
        });
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

  submitIndividualUpdate(): void {
    const validationError = this.validateIndividualRequest();
    if (validationError) {
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.status.set({ loading: true, success: false, error: '', message: '' });
    this.loanService.updateLoanInterest(this.individualLoanId!, this.individualRate!).subscribe({
      next: () => {
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Loan interest rate updated successfully.'
        });
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

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  reset(): void {
    this.selectedLoanTypes = ['HOME', 'PERSONAL'];
    this.bulkRate = 10;
    this.individualLoanId = null;
    this.individualRate = null;
    this.status.set({ loading: false, success: false, error: '', message: '' });
  }

  refresh(): void {
    // Reserved for future rate history refresh.
  }

  private validateBulkRequest(): string {
    if (this.selectedLoanTypes.length === 0) {
      return 'Select at least one loan category for bulk revision.';
    }

    if (!this.bulkRate || this.bulkRate <= 0 || this.bulkRate > 100) {
      return 'Enter a valid bulk interest rate between 0 and 100.';
    }

    return '';
  }

  private validateIndividualRequest(): string {
    if (!this.individualLoanId || this.individualLoanId <= 0) {
      return 'Enter a valid loan ID for individual update.';
    }

    if (!this.individualRate || this.individualRate <= 0 || this.individualRate > 100) {
      return 'Enter a valid individual interest rate between 0 and 100.';
    }

    return '';
  }
}
