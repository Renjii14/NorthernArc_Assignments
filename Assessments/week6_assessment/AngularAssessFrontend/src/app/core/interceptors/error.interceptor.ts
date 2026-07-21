import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

function toFriendlyErrorMessage(error: HttpErrorResponse): string {
  if (error.status === 0) {
    return 'Cannot reach backend server. Please verify it is running on http://localhost:8080.';
  }

  if (typeof error.error === 'string' && error.error.trim().length > 0) {
    return error.error;
  }

  if (error.error?.message) {
    return error.error.message;
  }

  switch (error.status) {
    case 400:
      return 'The request is invalid. Please check your input and try again.';
    case 404:
      return 'The requested scan was not found.';
    case 500:
      return 'The server encountered an error. Please try again later.';
    default:
      return 'Something went wrong while communicating with the server.';
  }
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const friendlyMessage = toFriendlyErrorMessage(error);
      return throwError(() => new Error(friendlyMessage));
    })
  );
};
