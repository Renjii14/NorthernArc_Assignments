import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { TokenStorageService } from '../../services/auth/token-storage.service';
import { ToastNotificationService } from '../../services/shared/toast-notification.service';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const tokenStorage = inject(TokenStorageService);
  const toastService = inject(ToastNotificationService);
  const isLoginRequest = req.url.includes('/auth/login');

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      switch (error.status) {
        case 400:
          toastService.show(getMessage(error, 'Invalid request. Please check the input.'), 'warning');
          break;
        case 401:
          if (isLoginRequest) {
            toastService.show(getMessage(error, 'Invalid credentials. Please try again.'), 'warning');
          } else {
            tokenStorage.clearSession();
            toastService.show('Your session has expired. Please sign in again.', 'warning');
            router.navigate(['/login']);
          }
          break;
        case 403:
          toastService.show('Access denied. You do not have permission for this action.', 'danger');
          router.navigate(['/access-denied']);
          break;
        case 404:
          toastService.show(getMessage(error, 'Requested resource was not found.'), 'info');
          break;
        case 500:
          toastService.show('Server error occurred. Please try again later.', 'danger');
          break;
        default:
          toastService.show(getMessage(error, 'Unexpected error occurred.'), 'danger');
          break;
      }

      return throwError(() => error);
    })
  );
};

function getMessage(error: HttpErrorResponse, fallback: string): string {
  if (typeof error.error === 'string') {
    return error.error;
  }

  return error.error?.message || error.message || fallback;
}
