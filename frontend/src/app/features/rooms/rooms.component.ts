import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { RoomService } from '../../core/services/room.service';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';
import { Room } from '../../core/models/models';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog.component';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatTableModule, MatFormFieldModule,
    MatInputModule, MatButtonModule, MatIconModule, MatDialogModule
  ],
  templateUrl: './rooms.component.html'
})
export class RoomsComponent {
  private fb = inject(FormBuilder);
  private service = inject(RoomService);
  private auth = inject(AuthService);
  private notify = inject(NotifyService);
  private dialog = inject(MatDialog);

  readonly columns = ['name', 'building', 'floor', 'type', 'actions'];
  data = signal<Room[]>([]);
  editId: number | null = null;
  isAdmin = this.auth.hasAnyRole('ADMIN');

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    building: [''],
    floor: [null as number | null],
    type: [''],
    description: ['']
  });

  constructor() { this.load(); }

  load(): void { this.service.list().subscribe((r) => this.data.set(r)); }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const body = this.form.getRawValue();
    const obs = this.editId
      ? this.service.update(this.editId, body)
      : this.service.create(body);
    obs.subscribe({
      next: () => { this.notify.success('Сохранено'); this.reset(); this.load(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }

  edit(r: Room): void {
    this.editId = r.id;
    this.form.patchValue({
      name: r.name, building: r.building ?? '', floor: r.floor ?? null,
      type: r.type ?? '', description: r.description ?? ''
    });
  }

  reset(): void {
    this.editId = null;
    this.form.reset({ name: '', building: '', floor: null, type: '', description: '' });
  }

  remove(r: Room): void {
    this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Удалить аудиторию', message: `Удалить «${r.name}»?` }
    }).afterClosed().subscribe((ok) => {
      if (!ok) return;
      this.service.delete(r.id).subscribe({
        next: () => { this.notify.success('Удалено'); this.load(); },
        error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка удаления')
      });
    });
  }
}
