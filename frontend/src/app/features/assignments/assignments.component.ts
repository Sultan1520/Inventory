import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AssignmentService } from '../../core/services/assignment.service';
import { EquipmentService } from '../../core/services/equipment.service';
import { RoomService } from '../../core/services/room.service';
import { UserService } from '../../core/services/user.service';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';
import { Assignment, Equipment, Room, UserResponse } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-assignments',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatTableModule, MatFormFieldModule,
    MatInputModule, MatSelectModule, MatButtonModule, MatIconModule,
    StatusChipComponent
  ],
  templateUrl: './assignments.component.html'
})
export class AssignmentsComponent {
  private fb = inject(FormBuilder);
  private service = inject(AssignmentService);
  private equipmentService = inject(EquipmentService);
  private roomService = inject(RoomService);
  private userService = inject(UserService);
  private auth = inject(AuthService);
  private notify = inject(NotifyService);

  readonly columns =
    ['equipment', 'target', 'assignedBy', 'assignedDate', 'status', 'actions'];
  data = signal<Assignment[]>([]);
  equipment = signal<Equipment[]>([]);
  rooms = signal<Room[]>([]);
  users = signal<UserResponse[]>([]);

  canManage = this.auth.hasAnyRole('ADMIN', 'IT_SPECIALIST');
  isAdmin = this.auth.hasAnyRole('ADMIN');

  form = this.fb.nonNullable.group({
    equipmentId: [null as number | null, Validators.required],
    assignedToUserId: [null as number | null],
    assignedToRoomId: [null as number | null],
    comment: ['']
  });

  constructor() {
    this.load();
    if (this.canManage) {
      this.equipmentService.list({ size: 200 }).subscribe((p) => this.equipment.set(p.content));
      this.roomService.list().subscribe((r) => this.rooms.set(r));
      if (this.isAdmin) {
        this.userService.list().subscribe((u) => this.users.set(u));
      }
    }
  }

  load(): void { this.service.list().subscribe((a) => this.data.set(a)); }

  assign(): void {
    const v = this.form.getRawValue();
    if (!v.equipmentId || (!v.assignedToUserId && !v.assignedToRoomId)) {
      this.notify.error('Выберите оборудование и пользователя или аудиторию');
      return;
    }
    this.service.assign({
      equipmentId: v.equipmentId,
      assignedToUserId: v.assignedToUserId,
      assignedToRoomId: v.assignedToRoomId,
      comment: v.comment
    }).subscribe({
      next: () => {
        this.notify.success('Оборудование закреплено');
        this.form.reset({ equipmentId: null, assignedToUserId: null, assignedToRoomId: null, comment: '' });
        this.load();
      },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка закрепления')
    });
  }

  returnItem(a: Assignment): void {
    this.service.return(a.id).subscribe({
      next: () => { this.notify.success('Оборудование возвращено'); this.load(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка возврата')
    });
  }
}
