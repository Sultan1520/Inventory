import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EquipmentService } from '../../core/services/equipment.service';
import { CategoryService } from '../../core/services/category.service';
import { RoomService } from '../../core/services/room.service';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';
import { Category, Equipment, EquipmentStatus, Room } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog.component';

@Component({
  selector: 'app-equipment-list',
  standalone: true,
  imports: [
    CommonModule, RouterLink, FormsModule, MatTableModule, MatPaginatorModule,
    MatSortModule, MatFormFieldModule, MatInputModule, MatSelectModule,
    MatButtonModule, MatIconModule, MatProgressBarModule, MatDialogModule,
    StatusChipComponent
  ],
  templateUrl: './equipment-list.component.html'
})
export class EquipmentListComponent {
  private service = inject(EquipmentService);
  private categoryService = inject(CategoryService);
  private roomService = inject(RoomService);
  private auth = inject(AuthService);
  private notify = inject(NotifyService);
  private dialog = inject(MatDialog);
  private router = inject(Router);

  readonly statuses: EquipmentStatus[] =
    ['AVAILABLE', 'ASSIGNED', 'IN_REPAIR', 'LOST', 'WRITTEN_OFF'];
  readonly columns = ['name', 'inventoryNumber', 'category', 'room', 'status', 'condition', 'actions'];

  loading = signal(false);
  data = signal<Equipment[]>([]);
  total = signal(0);
  categories = signal<Category[]>([]);
  rooms = signal<Room[]>([]);

  search = '';
  categoryId: number | null = null;
  roomId: number | null = null;
  status: EquipmentStatus | null = null;
  page = 0;
  size = 10;
  sort = 'id,asc';

  isAdmin = this.auth.hasAnyRole('ADMIN');

  constructor() {
    this.categoryService.list().subscribe((c) => this.categories.set(c));
    this.roomService.list().subscribe((r) => this.rooms.set(r));
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.service.list({
      search: this.search, categoryId: this.categoryId, roomId: this.roomId,
      status: this.status, page: this.page, size: this.size, sort: this.sort
    }).subscribe({
      next: (p) => { this.data.set(p.content); this.total.set(p.totalElements); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  applyFilters(): void { this.page = 0; this.load(); }

  onPage(e: PageEvent): void {
    this.page = e.pageIndex;
    this.size = e.pageSize;
    this.load();
  }

  onSort(s: Sort): void {
    this.sort = s.direction ? `${s.active},${s.direction}` : 'id,asc';
    this.load();
  }

  edit(e: Equipment): void {
    this.router.navigate(['/equipment/create'], { queryParams: { id: e.id } });
  }

  remove(e: Equipment): void {
    this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Удалить оборудование', message: `Удалить «${e.name}»?` }
    }).afterClosed().subscribe((ok) => {
      if (!ok) return;
      this.service.delete(e.id).subscribe({
        next: () => { this.notify.success('Удалено'); this.load(); },
        error: (err) => this.notify.error(err?.error?.message ?? 'Ошибка удаления')
      });
    });
  }
}
