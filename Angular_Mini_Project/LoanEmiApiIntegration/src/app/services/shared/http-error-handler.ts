import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

/**
 * Creates a reusable HTTP error handler for service methods.
 */
export function handleHttpError(context: string): (error: HttpErrorResponse) => Observable<never> {
  return (error: HttpErrorResponse): Observable<never> => {
    const message =
      typeof error.error === 'string'
        ? error.error
        : error.error?.message || error.message || 'Unexpected server error';

    return throwError(() => new Error(`${context} failed: ${message}`));
  };
}
