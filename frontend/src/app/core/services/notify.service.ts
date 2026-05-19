import { Injectable, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class NotifyService {
  private snack = inject(MatSnackBar);

  success(msg: string): void {
    this.snack.open(msg, 'OK', { duration: 3000, panelClass: 'snack-success' });
  }
  error(msg: string): void {
    this.snack.open(msg, 'OK', { duration: 5000, panelClass: 'snack-error' });
  }
}
