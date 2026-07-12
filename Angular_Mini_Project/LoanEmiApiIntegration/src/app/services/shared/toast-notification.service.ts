import { Injectable, signal } from '@angular/core';

export type ToastVariant = 'success' | 'danger' | 'warning' | 'info';

export interface ToastMessage {
  id: number;
  message: string;
  variant: ToastVariant;
}

@Injectable({
  providedIn: 'root'
})
export class ToastNotificationService {
  readonly messages = signal<ToastMessage[]>([]);
  private nextId = 1;

  show(message: string, variant: ToastVariant = 'info', timeoutMs = 5000): void {
    const id = this.nextId++;
    this.messages.update((messages) => [...messages, { id, message, variant }]);

    if (timeoutMs > 0) {
      setTimeout(() => this.dismiss(id), timeoutMs);
    }
  }

  dismiss(id: number): void {
    this.messages.update((messages) => messages.filter((message) => message.id !== id));
  }

  clear(): void {
    this.messages.set([]);
  }
}
