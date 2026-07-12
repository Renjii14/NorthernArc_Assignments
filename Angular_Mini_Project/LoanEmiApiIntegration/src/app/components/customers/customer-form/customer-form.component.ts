import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormActionButtonsComponent } from '../../shared/buttons/form-action-buttons.component';
import { SelectInputComponent } from '../../shared/forms/inputs/select-input.component';
import { TextInputComponent } from '../../shared/forms/inputs/text-input.component';
import { ValidationMessageComponent } from '../../shared/forms/validation/validation-message.component';
import { PageHeaderComponent } from '../../shared/page-header/page-header.component';
import { CreateCustomerRequest, CustomerService } from '../../../services/customer/customer.service';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [
    FormsModule,
    PageHeaderComponent,
    TextInputComponent,
    SelectInputComponent,
    FormActionButtonsComponent,
    ValidationMessageComponent
  ],
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.css'],
})
export class CustomerFormComponent {
  private readonly customerService = inject(CustomerService);

  readonly roles = ['USER', 'MANAGER', 'ADMIN'];

  customer: CreateCustomerRequest = {
    customerName: '',
    email: '',
    password: '',
    phoneNumber: '',
    city: '',
    creditScore: 0,
    role: 'USER'
  };

  readonly status = signal({
    loading: false,
    success: false,
    error: '',
    message: ''
  });

  submit(): void {
    this.status.set({ loading: true, success: false, error: '', message: '' });
    this.customerService.createCustomer(this.customer).subscribe({
      next: () => {
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Customer created successfully.'
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
    this.customer = {
      customerName: '',
      email: '',
      password: '',
      phoneNumber: '',
      city: '',
      creditScore: 0,
      role: 'USER'
    };
  }

  refresh(): void {
    // Reserved for list refresh orchestration in container flows.
  }
}

