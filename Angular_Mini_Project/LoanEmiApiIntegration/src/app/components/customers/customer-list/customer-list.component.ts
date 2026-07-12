import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerResponse, CustomerService } from '../../../services/customer/customer.service';
import { PageResponse } from '../../../services/shared/page-response';
import { TableAction, TableActionButtonsComponent } from '../../shared/buttons/table-action-buttons.component';
import { StatusBadgeComponent } from '../../shared/badges/status-badge.component';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { PaginationComponent } from '../../shared/pagination/pagination.component';
import { TableToolbarComponent } from '../../shared/search/table-toolbar/table-toolbar.component';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    StatusBadgeComponent,
    PageHeaderComponent,
    PaginationComponent,
    TableToolbarComponent,
    TableActionButtonsComponent
  ],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css'],
})
export class CustomerListComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly router = inject(Router);

  readonly customerRows = signal<CustomerResponse[]>([]);
  readonly allRows = signal<CustomerResponse[]>([]);
  readonly searchTerm = signal('');
  readonly roleFilter = signal('ALL');
  readonly statusFilter = signal('ALL');
  readonly cityFilter = signal('ALL');
  readonly sortBy = signal<'customerId' | 'customerName' | 'city' | 'creditScore'>('customerId');
  readonly sortDirection = signal<'asc' | 'desc'>('asc');
  readonly status = signal({ loading: false, error: '', success: '' });
  readonly page = signal(0);
  readonly size = signal(10);
  readonly totalElements = signal(0);
  readonly totalPages = signal(0);
  readonly pageSizeOptions = [10, 20, 50];

  readonly rowActions: TableAction[] = [
    { key: 'view', icon: 'bi-eye', className: 'btn btn-outline-primary', label: 'View', tooltip: 'View customer details' },
    { key: 'edit', icon: 'bi-pencil', className: 'btn btn-outline-secondary', label: 'Edit', tooltip: 'Edit customer details' },
    { key: 'deactivate', icon: 'bi-person-x', className: 'btn btn-outline-danger', label: 'Deactivate', tooltip: 'Deactivate customer' }
  ];

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.status.set({ loading: true, error: '', success: '' });
    const sortField = this.sortBy();
    const sortDir = this.sortDirection().toUpperCase();
    this.customerService.getAllCustomers(this.page(), this.size(), sortField, sortDir).subscribe({
      next: (response: PageResponse<CustomerResponse>) => {
        this.allRows.set(response.content);
        this.totalElements.set(response.totalElements);
        this.totalPages.set(response.totalPages);
        this.applyFilters();
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

  onSearch(): void {
    this.applyFilters();
  }

  onFilterChange(): void {
    this.page.set(0);
    this.applyFilters();
  }

  onSortChange(sortBy: 'customerId' | 'customerName' | 'city' | 'creditScore'): void {
    if (this.sortBy() === sortBy) {
      this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortBy.set(sortBy);
      this.sortDirection.set('asc');
    }
    this.loadData();
  }

  onAction(action: string, customerId: number): void {
    if (action === 'view') {
      this.view(customerId);
      return;
    }

    if (action === 'deactivate') {
      this.delete(customerId);
      return;
    }
    if (action === 'edit') {
      this.edit(customerId);
    }
  }

  view(customerId: number): void {
    this.router.navigate(['/customers/details'], { queryParams: { id: customerId } });
  }

  delete(customerId: number): void {
    const confirmed = window.confirm('Deactivate this customer?');
    if (!confirmed) {
      return;
    }
    this.status.set({ loading: true, error: '', success: '' });
    this.customerService.deactivateCustomer(customerId).subscribe({
      next: () => {
        this.status.set({ loading: false, error: '', success: 'Customer deactivated successfully.' });
        this.refresh();
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

  edit(customerId: number): void {
    this.router.navigate(['/customers/details'], { queryParams: { id: customerId, mode: 'edit' } });
  }

  openCustomer(customerId: number): void {
    this.view(customerId);
  }

  onPageChange(page: number): void {
    if (page === this.page()) {
      return;
    }
    this.page.set(page);
    this.loadData();
  }

  onPageSizeChange(size: number): void {
    this.size.set(size);
    this.page.set(0);
    this.loadData();
  }

  availableCities(): string[] {
    const values = this.allRows().map((item) => item.city).filter((city) => !!city);
    return Array.from(new Set(values)).sort((a, b) => a.localeCompare(b));
  }

  refresh(): void {
    this.loadData();
  }

  private applyFilters(): void {
    const term = this.searchTerm().trim().toLowerCase();
    const role = this.roleFilter();
    const status = this.statusFilter();
    const city = this.cityFilter();

    let filtered = this.allRows().filter((customer) => {
      const matchesTerm =
        !term ||
        customer.customerName.toLowerCase().includes(term) ||
        customer.email.toLowerCase().includes(term) ||
        customer.city.toLowerCase().includes(term) ||
        String(customer.customerId).includes(term);

      const matchesRole = role === 'ALL' || customer.role === role;
      const customerStatus = customer.active ? 'ACTIVE' : 'DEACTIVATED';
      const matchesStatus = status === 'ALL' || customerStatus === status;
      const matchesCity = city === 'ALL' || customer.city === city;
      return matchesTerm && matchesRole && matchesStatus && matchesCity;
    });

    filtered = this.sortRows(filtered);
    this.customerRows.set(filtered);
  }

  private sortRows(rows: CustomerResponse[]): CustomerResponse[] {
    const sorted = [...rows].sort((a, b) => {
      const key = this.sortBy();
      if (key === 'customerId' || key === 'creditScore') {
        return (a[key] as number) - (b[key] as number);
      }
      return String(a[key]).localeCompare(String(b[key]));
    });

    return this.sortDirection() === 'asc' ? sorted : sorted.reverse();
  }
}

