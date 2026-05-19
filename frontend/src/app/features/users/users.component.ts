import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatFormFieldModule } from '@angular/material/form-field';
import { UserService } from '../../core/services/user.service';
import { NotifyService } from '../../core/services/notify.service';
import { Role, UserResponse } from '../../core/models/models';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatSelectModule,
    MatSlideToggleModule, MatFormFieldModule
  ],
  templateUrl: './users.component.html'
})
export class UsersComponent {
  private service = inject(UserService);
  private notify = inject(NotifyService);

  readonly columns = ['fullName', 'email', 'role', 'department', 'enabled'];
  readonly roles: Role[] = ['ADMIN', 'IT_SPECIALIST', 'TEACHER', 'MANAGER'];
  data = signal<UserResponse[]>([]);

  constructor() { this.load(); }

  load(): void { this.service.list().subscribe((u) => this.data.set(u)); }

  changeRole(u: UserResponse, role: Role): void {
    this.service.updateRole(u.id, role).subscribe({
      next: () => { this.notify.success('Роль обновлена'); this.load(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }

  toggleStatus(u: UserResponse, enabled: boolean): void {
    this.service.updateStatus(u.id, enabled).subscribe({
      next: () => this.notify.success(enabled ? 'Пользователь включён' : 'Пользователь отключён'),
      error: (e) => { this.notify.error(e?.error?.message ?? 'Ошибка'); this.load(); }
    });
  }
}
