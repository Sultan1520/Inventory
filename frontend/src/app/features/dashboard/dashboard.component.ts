import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardSummary } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, MatProgressBarModule, MatIconModule, StatusChipComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  private service = inject(DashboardService);
  loading = signal(true);
  data = signal<DashboardSummary | null>(null);

  constructor() {
    this.service.summary().subscribe({
      next: (d) => { this.data.set(d); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
  }

  categoryEntries(d: DashboardSummary): { name: string; count: number }[] {
    return Object.entries(d.equipmentByCategory).map(([name, count]) => ({ name, count }));
  }
}
