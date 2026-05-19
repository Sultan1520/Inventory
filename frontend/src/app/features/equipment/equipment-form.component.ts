import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { EquipmentService } from '../../core/services/equipment.service';
import { CategoryService } from '../../core/services/category.service';
import { RoomService } from '../../core/services/room.service';
import { NotifyService } from '../../core/services/notify.service';
import {
  Category, EquipmentCondition, EquipmentRequest, EquipmentStatus, Room
} from '../../core/models/models';

@Component({
  selector: 'app-equipment-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatDatepickerModule,
    MatNativeDateModule, MatProgressBarModule
  ],
  templateUrl: './equipment-form.component.html'
})
export class EquipmentFormComponent {
  private fb = inject(FormBuilder);
  private service = inject(EquipmentService);
  private categoryService = inject(CategoryService);
  private roomService = inject(RoomService);
  private notify = inject(NotifyService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  readonly statuses: EquipmentStatus[] =
    ['AVAILABLE', 'ASSIGNED', 'IN_REPAIR', 'LOST', 'WRITTEN_OFF'];
  readonly conditions: EquipmentCondition[] = ['NEW', 'GOOD', 'NEEDS_REPAIR', 'BROKEN'];

  categories = signal<Category[]>([]);
  rooms = signal<Room[]>([]);
  loading = signal(false);
  saving = signal(false);
  editId: number | null = null;

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    inventoryNumber: ['', Validators.required],
    serialNumber: [''],
    categoryId: [null as number | null],
    status: ['AVAILABLE' as EquipmentStatus, Validators.required],
    condition: ['NEW' as EquipmentCondition, Validators.required],
    roomId: [null as number | null],
    responsibleUserId: [null as number | null],
    purchaseDate: [null as Date | null],
    price: [null as number | null],
    description: ['']
  });

  constructor() {
    this.categoryService.list().subscribe((c) => this.categories.set(c));
    this.roomService.list().subscribe((r) => this.rooms.set(r));
    const id = this.route.snapshot.queryParamMap.get('id');
    if (id) {
      this.editId = Number(id);
      this.loading.set(true);
      this.service.get(this.editId).subscribe({
        next: (e) => {
          this.form.patchValue({
            name: e.name, inventoryNumber: e.inventoryNumber,
            serialNumber: e.serialNumber ?? '', categoryId: e.categoryId ?? null,
            status: e.status, condition: e.condition, roomId: e.roomId ?? null,
            responsibleUserId: e.responsibleUserId ?? null,
            purchaseDate: e.purchaseDate ? new Date(e.purchaseDate) : null,
            price: e.price ?? null, description: e.description ?? ''
          });
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    }
  }

  get title(): string {
    return this.editId ? 'Редактирование оборудования' : 'Новое оборудование';
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const v = this.form.getRawValue();
    const body: EquipmentRequest = {
      ...v,
      purchaseDate: v.purchaseDate ? v.purchaseDate.toISOString().substring(0, 10) : null
    };
    this.saving.set(true);
    const obs = this.editId
      ? this.service.update(this.editId, body)
      : this.service.create(body);
    obs.subscribe({
      next: () => {
        this.notify.success('Сохранено');
        this.router.navigate(['/equipment']);
      },
      error: (e) => {
        this.saving.set(false);
        this.notify.error(e?.error?.message ?? 'Ошибка сохранения');
      }
    });
  }

  cancel(): void { this.router.navigate(['/equipment']); }
}
