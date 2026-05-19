import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ReportService } from '../../core/services/report.service';
import { NotifyService } from '../../core/services/notify.service';
import { Assignment, Equipment, RepairRequest } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule, MatTabsModule, MatTableModule, MatButtonModule,
    MatIconModule, StatusChipComponent
  ],
  templateUrl: './reports.component.html'
})
export class ReportsComponent {
  private service = inject(ReportService);
  private notify = inject(NotifyService);

  byRoom = signal<{ room: string; items: Equipment[] }[]>([]);
  inRepair = signal<Equipment[]>([]);
  writtenOff = signal<Equipment[]>([]);
  assigned = signal<Assignment[]>([]);
  history = signal<RepairRequest[]>([]);

  readonly eqCols = ['name', 'inventoryNumber', 'status', 'condition'];
  readonly asgCols = ['equipmentName', 'target', 'assignedDate', 'status'];
  readonly histCols = ['title', 'equipmentName', 'status', 'createdAt'];

  constructor() {
    this.service.equipmentByRoom().subscribe((m) =>
      this.byRoom.set(Object.entries(m).map(([room, items]) => ({ room, items }))));
    this.service.equipmentInRepair().subscribe((d) => this.inRepair.set(d));
    this.service.writtenOff().subscribe((d) => this.writtenOff.set(d));
    this.service.assignedEquipment().subscribe((d) => this.assigned.set(d));
    this.service.repairHistory().subscribe((d) => this.history.set(d));
  }

  exportCsv(): void {
    this.service.exportEquipmentCsv().subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'equipment.csv';
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => this.notify.error('Не удалось скачать CSV')
    });
  }
}
