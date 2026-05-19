import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, RouterLink, MatCardModule,
    MatFormFieldModule, MatInputModule, MatButtonModule, MatProgressBarModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './auth.css'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private notify = inject(NotifyService);

  loading = signal(false);

  form = this.fb.nonNullable.group({
    email: ['admin@test.com', [Validators.required, Validators.email]],
    password: ['admin123', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.notify.success('Вход выполнен');
        this.router.navigate(['/dashboard']);
      },
      error: (e) => {
        this.loading.set(false);
        this.notify.error(e?.error?.message ?? 'Не удалось войти');
      }
    });
  }
}
