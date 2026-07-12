import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { CustomerResponse, CustomerService, UpdateCustomerRequest } from '../../../services/customer/customer.service';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { StatusBadgeComponent } from '../../shared/badges/status-badge.component';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [FormsModule, PageHeaderComponent, StatusBadgeComponent],
  templateUrl: './customer-details.component.html',
  styleUrls: ['./customer-details.component.css'],
})
export class CustomerDetailsComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly customer = signal<CustomerResponse | null>(null);
  readonly editMode = signal(false);
  readonly status = signal({ loading: false, error: '', success: '' });

  form: UpdateCustomerRequest = {
    customerName: '',
    email: '',
    phoneNumber: '',
    city: '',
    creditScore: 0,
    role: 'USER',
    active: true
  };

  readonly roles = ['USER', 'MANAGER', 'ADMIN'];

  ngOnInit(): void {
    const mode = this.route.snapshot.queryParamMap.get('mode');
    this.editMode.set(mode === 'edit');

    this.loadData();
  }

  loadData(): void {
    const customerId = Number(this.route.snapshot.queryParamMap.get('id'));
    if (!Number.isFinite(customerId) || customerId <= 0) {
      this.status.set({
        loading: false,
        error: 'Customer ID is missing or invalid.',
        success: ''
      });
      return;
    }

    this.status.set({ loading: true, error: '', success: this.status().success });
    this.customerService.getCustomer(customerId).subscribe({
      next: (response) => {
        this.customer.set(response);
        this.form = {
          customerName: response.customerName,
          email: response.email,
          phoneNumber: response.phoneNumber,
          city: response.city,
          creditScore: response.creditScore,
          role: response.role,
          active: response.active
        };
        this.status.set({ loading: false, error: '', success: this.status().success });
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

  refresh(): void {
    this.loadData();
  }

  saveChanges(): void {
    const customer = this.customer();
    if (!customer) {
      return;
    }

    if (!this.form.customerName.trim() || !this.form.email.trim() || !this.form.phoneNumber.trim() || !this.form.city.trim()) {
      this.status.set({ loading: false, error: 'Name, email, phone, and city are required.', success: '' });
      return;
    }

    this.status.set({ loading: true, error: '', success: '' });
    this.customerService.updateCustomer(customer.customerId, this.form).subscribe({
      next: (response) => {
        this.customer.set(response);
        this.form = {
          customerName: response.customerName,
          email: response.email,
          phoneNumber: response.phoneNumber,
          city: response.city,
          creditScore: response.creditScore,
          role: response.role,
          active: response.active
        };
        this.status.set({
          loading: false,
          error: '',
          success: 'Customer details updated successfully.'
        });
      },
      error: (error) => {
        const rawMessage = String(error?.error?.message || error?.message || 'Failed to update customer.');
        const message = error?.status === 500
          ? 'Customer update API failed on server. Please verify backend update endpoint/validation contract for customer edits.'
          : rawMessage;
        this.status.set({
          loading: false,
          error: message,
          success: ''
        });
      }
    });
  }

  cancelEdit(): void {
    const customer = this.customer();
    if (customer) {
      this.form = {
        customerName: customer.customerName,
        email: customer.email,
        phoneNumber: customer.phoneNumber,
        city: customer.city,
        creditScore: customer.creditScore,
        role: customer.role,
        active: customer.active
      };
    }

    this.router.navigate(['/customers/details'], {
      queryParams: { id: customer?.customerId || this.route.snapshot.queryParamMap.get('id') }
    });
  }
}

