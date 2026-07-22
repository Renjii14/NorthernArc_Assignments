import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      const message =
        typeof error.error === 'string'
          ? error.error
          : error.error?.message || 'Something went wrong while calling the API.';

      return throwError(() => new Error(message));
    })
  );
};
