import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormActionButtonsComponent } from '../../shared/buttons/form-action-buttons.component';
import { SelectInputComponent } from '../../shared/forms/inputs/select-input.component';
import { TextInputComponent } from '../../shared/forms/inputs/text-input.component';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { EmiPaymentRequest, EmiPaymentResponse, LoanService, LoanSummary } from '../../../services/loan/loan.service';

interface PendingEmiOption {
  emiId: number;
  installmentLabel: string;
  dueDate: string;
  amountDue: number;
}

@Component({
  selector: 'app-pay-emi',
  standalone: true,
  imports: [FormsModule, PageHeaderComponent, TextInputComponent, SelectInputComponent, FormActionButtonsComponent],
  templateUrl: './pay-emi.component.html',
  styleUrls: ['./pay-emi.component.css'],
})
export class PayEmiComponent implements OnInit {
  private readonly loanService = inject(LoanService);

  readonly paymentModes = ['UPI', 'NET_BANKING', 'CARD'];
  readonly allLoans = signal<LoanSummary[]>([]);
  readonly selectedCustomerId = signal<number | null>(null);
  readonly selectedCustomerOption = signal('');
  readonly selectedLoanId = signal<number | null>(null);
  readonly selectedLoanOption = signal('');
  readonly pendingEmis = signal<PendingEmiOption[]>([]);
  readonly selectedPendingEmiId = signal<number | null>(null);
  readonly selectedPendingEmiOption = signal('');

  payment: EmiPaymentRequest = {
    emiId: 0,
    amount: 0,
    paymentMode: 'UPI',
    referenceNumber: ''
  };

  readonly status = signal({ loading: false, success: false, error: '', message: '' });
  readonly paymentResponse = signal<EmiPaymentResponse | null>(null);

  ngOnInit(): void {
    this.loadLoans();
  }

  customerOptions(): string[] {
    const unique = new Map<number, string>();
    this.allLoans().forEach((loan) => {
      unique.set(loan.customerId, `#${loan.customerId} - ${loan.customerName}`);
    });
    return Array.from(unique.values());
  }

  loanOptions(): string[] {
    const customerId = this.selectedCustomerId();
    if (!customerId) {
      return [];
    }

    return this.allLoans()
      .filter((loan) => loan.customerId === customerId)
      .map((loan) => `#${loan.loanId} - ${loan.loanType} (${loan.loanStatus})`);
  }

  pendingEmiOptions(): string[] {
    return this.pendingEmis().map((emi) => `${emi.installmentLabel} | Due ${emi.dueDate} | INR ${emi.amountDue.toLocaleString('en-IN')}`);
  }

  onCustomerChange(option: string): void {
    this.selectedCustomerOption.set(option);
    const selected = Number(option.split(' - ')[0].replace('#', ''));
    this.selectedCustomerId.set(Number.isNaN(selected) ? null : selected);
    this.selectedLoanId.set(null);
    this.selectedLoanOption.set('');
    this.pendingEmis.set([]);
    this.selectedPendingEmiId.set(null);
    this.selectedPendingEmiOption.set('');
    this.payment.emiId = 0;
    this.payment.amount = 0;
  }

  onLoanChange(option: string): void {
    this.selectedLoanOption.set(option);
    const selected = Number(option.split(' - ')[0].replace('#', ''));
    const loanId = Number.isNaN(selected) ? null : selected;
    this.selectedLoanId.set(loanId);

    const loan = this.allLoans().find((item) => item.loanId === loanId);
    this.pendingEmis.set(loan ? this.buildPendingEmis(loan) : []);
    this.selectedPendingEmiId.set(null);
    this.selectedPendingEmiOption.set('');
    this.payment.emiId = 0;
    this.payment.amount = 0;
  }

  onPendingEmiChange(option: string): void {
    this.selectedPendingEmiOption.set(option);
    const selected = Number(option.split(' | ')[0].replace('EMI #', ''));
    const pendingEmi = this.pendingEmis().find((item) => item.emiId === selected);
    this.selectedPendingEmiId.set(pendingEmi?.emiId || null);

    if (!pendingEmi) {
      return;
    }

    this.payment.emiId = pendingEmi.emiId;
    this.payment.amount = pendingEmi.amountDue;
    this.payment.referenceNumber = this.generateReferenceNumber();
  }

  submit(): void {
    if (!this.payment.emiId || this.payment.amount <= 0) {
      this.status.set({
        loading: false,
        success: false,
        error: 'Select customer, loan, and pending EMI before making payment.',
        message: ''
      });
      return;
    }

    this.status.set({ loading: true, success: false, error: '', message: '' });
    this.loanService.payEmi(this.payment).subscribe({
      next: (response) => {
        this.paymentResponse.set(response);
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Payment recorded successfully.'
        });
        this.reset();
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
    this.selectedCustomerId.set(null);
    this.selectedCustomerOption.set('');
    this.selectedLoanId.set(null);
    this.selectedLoanOption.set('');
    this.pendingEmis.set([]);
    this.selectedPendingEmiId.set(null);
    this.selectedPendingEmiOption.set('');
    this.payment = {
      emiId: 0,
      amount: 0,
      paymentMode: 'UPI',
      referenceNumber: ''
    };
  }

  private loadLoans(): void {
    this.loanService.getLoans(0, 200).subscribe({
      next: (response) => {
        this.allLoans.set(response.content);
      },
      error: () => {
        this.status.set({
          loading: false,
          success: false,
          error: 'Unable to load loans for payment workflow.',
          message: ''
        });
      }
    });
  }

  private buildPendingEmis(loan: LoanSummary): PendingEmiOption[] {
    const emiAmount = Math.max(1, this.toNumber(loan.emiAmount));
    return [1, 2, 3].map((offset) => {
      const dueDate = new Date();
      dueDate.setMonth(dueDate.getMonth() + offset - 1);

      return {
        emiId: loan.loanId * 100 + offset,
        installmentLabel: `EMI #${loan.loanId * 100 + offset}`,
        dueDate: dueDate.toISOString().slice(0, 10),
        amountDue: emiAmount
      };
    });
  }

  private generateReferenceNumber(): string {
    return `PAY-${Date.now()}`;
  }

  private toNumber(value: number | string): number {
    if (typeof value === 'number') {
      return value;
    }
    const parsed = Number(String(value).replace(/[^\d.-]/g, ''));
    return Number.isFinite(parsed) ? parsed : 0;
  }
}

