import { CommonModule } from '@angular/common';
import { Component, OnInit, WritableSignal, inject, signal } from '@angular/core';
import { TokenStorageService } from '../../services/auth/token-storage.service';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';
import { StatCardComponent } from '../shared/cards/stat-card.component';
import { SummaryCardComponent } from './summary-card/summary-card.component';
import { QuickActionCardComponent } from './quick-action-card/quick-action-card.component';
import { ActivityCardComponent } from './activity-card/activity-card.component';
import { NotificationCardComponent } from './notification-card/notification-card.component';
import { DashboardService, EmiInsights } from '../../services/dashboard/dashboard.service';
import { LoanService } from '../../services/loan/loan.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    PageHeaderComponent,
    StatCardComponent,
    SummaryCardComponent,
    QuickActionCardComponent,
    ActivityCardComponent,
    NotificationCardComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly tokenStorage = inject(TokenStorageService);
  private readonly loanService = inject(LoanService);

  readonly metrics = signal<Array<{ label: string; value: string; icon: string; tone: 'primary' | 'info' | 'success' | 'warning' | 'danger' }>>([]);
  readonly collections = signal<Array<{ title: string; value: string; growth: string }>>([]);
  readonly keyHighlights = signal<string[]>([]);
  readonly activities = signal<string[]>([]);
  readonly status: WritableSignal<{ loading: boolean; error: string }> = signal({
    loading: false,
    error: ''
  });
  readonly emiInsights = signal<EmiInsights | null>(null);
  readonly emiInsightsStatus = signal({ loading: false, error: '' });
  readonly role = signal<'USER' | 'MANAGER' | 'ADMIN'>(this.resolveRole());
  readonly collectionTrend = signal([62, 68, 65, 74, 78, 71, 82]);
  readonly overdueBreakup = signal([
    { label: 'On-Time', value: 72 },
    { label: 'Due Soon', value: 18 },
    { label: 'Overdue', value: 10 }
  ]);
  readonly upcomingEmis = signal<Array<{ loanId: number; customer: string; dueDate: string; amount: string }>>([]);
  readonly topDefaulters = signal<Array<{ customer: string; overdueCount: number; overdueAmount: string }>>([]);

  readonly quickActionsByRole = {
    USER: [
      { label: 'Pay EMI', icon: 'bi-credit-card', className: 'btn btn-primary text-start', routerLink: '/emi/pay' },
      { label: 'Track My Requests', icon: 'bi-hourglass-split', className: 'btn btn-outline-warning text-start', routerLink: '/my-requests' },
      { label: 'View Payment History', icon: 'bi-receipt', className: 'btn btn-outline-primary text-start', routerLink: '/payments' },
      { label: 'Browse Loan Products', icon: 'bi-collection', className: 'btn btn-outline-secondary text-start', routerLink: '/loan-products' },
      { label: 'Open Profile', icon: 'bi-person-vcard', className: 'btn btn-outline-info text-start', routerLink: '/profile' }
    ],
    MANAGER: [
      { label: 'Add Customer', icon: 'bi-person-plus', className: 'btn btn-primary text-start', routerLink: '/create-customer' },
      { label: 'Create Loan', icon: 'bi-cash-coin', className: 'btn btn-outline-primary text-start', routerLink: '/create-loan' },
      { label: 'Review Customers', icon: 'bi-people', className: 'btn btn-outline-secondary text-start', routerLink: '/customers' },
      { label: 'Update Interest', icon: 'bi-percent', className: 'btn btn-outline-warning text-start', routerLink: '/update-interest' }
    ],
    ADMIN: [
      { label: 'Manage Customers', icon: 'bi-people', className: 'btn btn-primary text-start', routerLink: '/customers' },
      { label: 'Create Loan', icon: 'bi-cash-coin', className: 'btn btn-outline-secondary text-start', routerLink: '/create-loan' },
      { label: 'Govern Interest Rules', icon: 'bi-percent', className: 'btn btn-outline-warning text-start', routerLink: '/update-interest' }
    ]
  };

  ngOnInit(): void {
    if (this.role() === 'USER') {
      this.loadUserData();
      return;
    }

    this.loadEmiInsights();
    this.loadData();
  }

  loadData(): void {
    this.status.set({ loading: true, error: '' });
    this.dashboardService.getDashboard().subscribe({
      next: (response) => {
        const totalCustomers = this.toNumber(response.totalCustomers);
        const totalLoans = this.toNumber(response.totalLoans);
        const activeLoans = this.toNumber(response.activeLoans);
        const closedLoans = this.toNumber(response.closedLoans);
        const overdueEmis = this.toNumber(response.overdueEMIs);
        const npaAccounts = this.toNumber(response.NPAAccounts);
        const totalEmiCollected = this.toNumber(response.totalEMICollected);
        const totalPenaltyCollected = this.toNumber(response.totalPenaltyCollected);
        const averageInterestRate = this.toNumber(response.averageInterestRate);

        const baseMetrics: Array<{ label: string; value: string; icon: string; tone: 'primary' | 'info' | 'success' | 'warning' | 'danger' }> = [
          { label: 'Total Customers', value: this.formatCount(totalCustomers), icon: 'bi-people-fill', tone: 'primary' },
          { label: 'Total Loans', value: this.formatCount(totalLoans), icon: 'bi-bank2', tone: 'info' },
          { label: 'Active Loans', value: this.formatCount(activeLoans), icon: 'bi-graph-up-arrow', tone: 'success' },
          { label: 'Closed Loans', value: this.formatCount(closedLoans), icon: 'bi-check2-circle', tone: 'primary' },
          { label: 'Overdue EMIs', value: this.formatCount(overdueEmis), icon: 'bi-exclamation-triangle-fill', tone: 'warning' }
        ];

        if (this.role() === 'ADMIN') {
          baseMetrics.push({ label: 'NPA Accounts', value: this.formatCount(npaAccounts), icon: 'bi-shield-exclamation', tone: 'danger' });
        }

        this.metrics.set(baseMetrics);
        this.collections.set([
          { title: 'Total EMI Collected', value: this.formatCurrency(totalEmiCollected), growth: 'Live from API' },
          { title: 'Penalty Collected', value: this.formatCurrency(totalPenaltyCollected), growth: 'Live from API' },
          { title: 'Average Interest Rate', value: this.formatPercent(averageInterestRate), growth: 'Live from API' }
        ]);
        const highlightsByRole = [
          `Highest Outstanding Loan: ${this.formatText(response.highestOutstandingLoan)}`,
          `Highest Paying Customer: ${this.formatText(response.highestPayingCustomer)}`,
          `Overdue EMIs: ${this.formatCount(overdueEmis)}`
        ];

        if (this.role() === 'ADMIN') {
          highlightsByRole.push(`NPA Accounts: ${this.formatCount(npaAccounts)}`);
        }

        this.keyHighlights.set(highlightsByRole);
        this.activities.set([
          `Dashboard synced with latest portfolio totals (${this.formatCount(totalLoans)} loans).`,
          `Collections refreshed: ${this.formatCurrency(totalEmiCollected)} total EMI collected.`,
          `Overdue trend currently at ${this.formatCount(overdueEmis)} records.`
        ]);
        this.status.set({ loading: false, error: '' });
      },
      error: (error) => {
        this.status.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Something went wrong.'
        });
      }
    });
  }

  quickActions() {
    return this.quickActionsByRole[this.role()];
  }

  titleForRole(): string {
    if (this.role() === 'ADMIN') {
      return 'Enterprise Control Dashboard';
    }
    if (this.role() === 'MANAGER') {
      return 'Branch Operations Dashboard';
    }
    return 'Customer Self-Service Dashboard';
  }

  subtitleForRole(): string {
    if (this.role() === 'ADMIN') {
      return 'Risk and Governance Overview';
    }
    if (this.role() === 'MANAGER') {
      return 'Portfolio Monitoring and Team Actions';
    }
    return 'Payments, Products, and Personal Loan View';
  }

  quickActionTitle(): string {
    return this.role() === 'USER' ? 'My Quick Actions' : 'Operational Quick Actions';
  }

  canViewEmiInsights(): boolean {
    return this.role() !== 'USER';
  }

  retryEmiInsights(): void {
    if (!this.canViewEmiInsights()) {
      return;
    }
    this.loadEmiInsights();
  }

  private loadUserData(): void {
    this.metrics.set([
      { label: 'My Pending Requests', value: '0', icon: 'bi-hourglass-top', tone: 'warning' },
      { label: 'Under Review Requests', value: '0', icon: 'bi-search', tone: 'info' },
      { label: 'Approved Requests', value: '0', icon: 'bi-check2-circle', tone: 'success' },
      { label: 'Rejected Requests', value: '0', icon: 'bi-x-circle', tone: 'danger' }
    ]);
    this.collections.set([
      { title: 'Current Outstanding', value: 'N/A', growth: 'Data will appear once loans are assigned to your account.' },
      { title: 'Last EMI Paid', value: 'N/A', growth: 'No EMI payment available yet.' },
      { title: 'My Request Status', value: 'Loading...', growth: 'Fetching your latest request updates' }
    ]);
    this.keyHighlights.set([
      'Submit a product request to start your loan journey.',
      'Track all review decisions in My Requests.',
      'Payment and loan insights appear after loan activation.'
    ]);
    this.activities.set([
      'Welcome to your self-service dashboard.',
      'Use Loan Products to submit your first request.',
      'Activity feed updates after your first transaction.'
    ]);
    this.upcomingEmis.set([]);
    this.topDefaulters.set([]);
    this.status.set({ loading: false, error: '' });
    this.loadUserRequestSummary();
  }

  private loadUserRequestSummary(): void {
    this.loanService.getMyLoanApplications(0, 100).subscribe({
      next: (response) => {
        const rows = response.content;
        const pending = rows.filter((item) => this.normalizeApplicationStatus(item.applicationStatus) === 'PENDING').length;
        const underReview = rows.filter((item) => this.normalizeApplicationStatus(item.applicationStatus) === 'UNDER_REVIEW').length;
        const approved = rows.filter((item) => this.normalizeApplicationStatus(item.applicationStatus) === 'APPROVED').length;
        const rejected = rows.filter((item) => this.normalizeApplicationStatus(item.applicationStatus) === 'REJECTED').length;

        this.metrics.set([
          { label: 'My Pending Requests', value: String(pending), icon: 'bi-hourglass-top', tone: 'warning' },
          { label: 'Under Review Requests', value: String(underReview), icon: 'bi-search', tone: 'info' },
          { label: 'Approved Requests', value: String(approved), icon: 'bi-check2-circle', tone: 'success' },
          { label: 'Rejected Requests', value: String(rejected), icon: 'bi-x-circle', tone: 'danger' }
        ]);

        this.collections.set([
          this.collections()[0],
          this.collections()[1],
          {
            title: 'My Request Status',
            value: `Pending ${pending} | Review ${underReview}`,
            growth: `Approved ${approved} | Rejected ${rejected}`
          }
        ]);

        this.keyHighlights.set([
          ...this.keyHighlights().slice(0, 2),
          `My requests: Pending ${pending}, Under Review ${underReview}, Approved ${approved}, Rejected ${rejected}`
        ]);
      },
      error: () => {
        const summaryCards = [...this.collections()];
        summaryCards[2] = {
          title: 'My Request Status',
          value: 'Temporarily unavailable',
          growth: 'Open My Requests for detailed tracking'
        };
        this.collections.set(summaryCards);
      }
    });
  }

  private normalizeApplicationStatus(status: string): 'PENDING' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED' {
    const normalized = String(status || '').trim().toUpperCase();
    if (normalized === 'APPROVED') {
      return 'APPROVED';
    }
    if (normalized === 'REJECTED') {
      return 'REJECTED';
    }
    if (normalized === 'UNDER_REVIEW' || normalized === 'IN_REVIEW') {
      return 'UNDER_REVIEW';
    }
    return 'PENDING';
  }

  maxTrendValue(): number {
    return Math.max(...this.collectionTrend(), 1);
  }

  private loadEmiInsights(): void {
    this.emiInsightsStatus.set({ loading: true, error: '' });
    this.dashboardService.getEmiInsights().subscribe({
      next: (response) => {
        this.emiInsights.set(response);
        this.emiInsightsStatus.set({ loading: false, error: '' });
      },
      error: () => {
        this.emiInsights.set(null);
        this.emiInsightsStatus.set({
          loading: false,
          error: 'EMI insights are temporarily unavailable. Please try again in a moment.'
        });
      }
    });
  }

  private resolveRole(): 'USER' | 'MANAGER' | 'ADMIN' {
    const role = this.tokenStorage.getPrimaryRole();
    if (role === 'ADMIN' || role === 'MANAGER') {
      return role;
    }
    return 'USER';
  }

  private toNumber(value: unknown): number | null {
    if (typeof value === 'number' && Number.isFinite(value)) {
      return value;
    }
    return null;
  }

  private formatCount(value: number | null): string {
    return value === null ? 'N/A' : String(value);
  }

  private formatCurrency(value: number | null): string {
    return value === null ? 'N/A' : `INR ${value.toLocaleString('en-IN')}`;
  }

  private formatPercent(value: number | null): string {
    return value === null ? 'N/A' : `${value}%`;
  }

  private formatText(value: unknown): string {
    if (typeof value === 'string' && value.trim().length > 0) {
      return value;
    }
    return 'N/A';
  }
}

