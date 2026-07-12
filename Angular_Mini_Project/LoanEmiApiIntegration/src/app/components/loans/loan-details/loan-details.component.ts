import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { LoanService, LoanSummary } from '../../../services/loan/loan.service';
import { TokenStorageService } from '../../../services/auth/token-storage.service';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { StatusBadgeComponent } from '../../shared/badges/status-badge.component';

@Component({
  selector: 'app-loan-details',
  standalone: true,
  imports: [PageHeaderComponent, StatusBadgeComponent, RouterLink],
  templateUrl: './loan-details.component.html',
  styleUrls: ['./loan-details.component.css'],
})
export class LoanDetailsComponent implements OnInit {
  private readonly loanService = inject(LoanService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly tokenStorage = inject(TokenStorageService);

  readonly loan = signal<LoanSummary | null>(null);
  readonly status = signal({ loading: false, error: '', success: '' });

  ngOnInit(): void {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    if (mode === 'edit') {
      this.status.set({
        loading: false,
        error: '',
        success: 'Edit mode opened. Use Update Interest Rate for now; full loan-field editing requires a backend update endpoint.'
      });
    }

    this.loadData();
  }

  loadData(): void {
    const loanId = Number(this.route.snapshot.queryParamMap.get('id') || 1001);
    this.status.set({ loading: true, error: '', success: '' });
    this.loanService.getLoan(loanId).subscribe({
      next: (response) => {
        this.loan.set(response);
        this.status.set({ loading: false, error: '', success: '' });
      },
      error: (error) => {
        this.status.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Something went wrong.',
          success: ''
        });
      }
    });
  }

  edit(): void {
    const currentLoan = this.loan();
    if (!currentLoan) {
      return;
    }

    this.router.navigate(['/update-interest'], {
      queryParams: {
        loanId: currentLoan.loanId,
        rate: currentLoan.annualInterestRate
      }
    });
  }

  delete(): void {
    const currentLoan = this.loan();
    if (!currentLoan) {
      return;
    }
    const confirmed = window.confirm('Delete this loan?');
    if (!confirmed) {
      return;
    }
    this.status.set({ loading: true, error: '', success: '' });
    this.loanService.deleteLoan(currentLoan.loanId).subscribe({
      next: () => {
        this.status.set({ loading: false, error: '', success: 'Loan deleted successfully.' });
      },
      error: (error) => {
        this.status.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Something went wrong.',
          success: ''
        });
      }
    });
  }

  canManageLoan(): boolean {
    return this.tokenStorage.hasRole(['MANAGER', 'ADMIN']);
  }

  formatCurrency(value: number): string {
    return `INR ${value.toLocaleString('en-IN', { maximumFractionDigits: 2 })}`;
  }

  formatOptionalCurrency(value?: number): string {
    if (typeof value !== 'number' || !Number.isFinite(value)) {
      return 'N/A';
    }
    return this.formatCurrency(value);
  }
}

