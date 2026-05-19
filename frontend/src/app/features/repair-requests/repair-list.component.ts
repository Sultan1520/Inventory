import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { RepairService } from '../../core/services/repair.service';
import { EquipmentService } from '../../core/services/equipment.service';
import { NotifyService } from '../../core/services/notify.service';
import { Equipment, Priority, RepairRequest } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-repair-list',
  standalone: true,
  imports: [
    CommonModule, RouterLink, ReactiveFormsModule, MatTableModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule,
    MatIconModule, MatCheckboxModule, StatusChipComponent
  ],
  templateUrl: './repair-list.component.html'
})
export class RepairListComponent {
  private fb = inject(FormBuilder);
  private service = inject(RepairService);
  private equipmentService = inject(EquipmentService);
  private notify = inject(NotifyService);

  readonly columns = ['title', 'equipment', 'createdBy', 'priority', 'status', 'actions'];
  readonly priorities: Priority[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
  data = signal<RepairRequest[]>([]);
  equipment = signal<Equipment[]>([]);
  showForm = signal(false);

  form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    description: [''],
    equipmentId: [null as number | null],
    priority: ['MEDIUM' as Priority],
    markEquipmentInRepair: [false]
  });

  constructor() {
    this.load();
    this.equipmentService.list({ size: 200 }).subscribe((p) => this.equipment.set(p.content));
  }

  load(): void { this.service.list().subscribe((r) => this.data.set(r)); }

  create(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.service.create(this.form.getRawValue()).subscribe({
      next: () => {
        this.notify.success('Заявка создана');
        this.form.reset({ title: '', description: '', equipmentId: null, priority: 'MEDIUM', markEquipmentInRepair: false });
        this.showForm.set(false);
        this.load();
      },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }
}
