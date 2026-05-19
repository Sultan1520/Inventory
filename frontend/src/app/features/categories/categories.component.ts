import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CategoryService } from '../../core/services/category.service';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';
import { Category } from '../../core/models/models';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog.component';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatTableModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatDialogModule
  ],
  templateUrl: './categories.component.html'
})
export class CategoriesComponent {
  private fb = inject(FormBuilder);
  private service = inject(CategoryService);
  private auth = inject(AuthService);
  private notify = inject(NotifyService);
  private dialog = inject(MatDialog);

  readonly columns = ['name', 'description', 'actions'];
  data = signal<Category[]>([]);
  editId: number | null = null;
  isAdmin = this.auth.hasAnyRole('ADMIN');

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    description: ['']
  });

  constructor() { this.load(); }

  load(): void {
    this.service.list().subscribe((c) => this.data.set(c));
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const body = this.form.getRawValue();
    const obs = this.editId
      ? this.service.update(this.editId, body)
      : this.service.create(body);
    obs.subscribe({
      next: () => {
        this.notify.success('Сохранено');
        this.reset();
        this.load();
      },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }

  edit(c: Category): void {
    this.editId = c.id;
    this.form.patchValue({ name: c.name, description: c.description ?? '' });
  }

  reset(): void {
    this.editId = null;
    this.form.reset({ name: '', description: '' });
  }

  remove(c: Category): void {
    this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Удалить категорию', message: `Удалить «${c.name}»?` }
    }).afterClosed().subscribe((ok) => {
      if (!ok) return;
      this.service.delete(c.id).subscribe({
        next: () => { this.notify.success('Удалено'); this.load(); },
        error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка удаления')
      });
    });
  }
}
