import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { EquipmentService } from '../../core/services/equipment.service';
import { AssignmentService } from '../../core/services/assignment.service';
import { AuthService } from '../../core/services/auth.service';
import { Assignment, Equipment } from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-equipment-detail',
  standalone: true,
  imports: [
    CommonModule, RouterLink, MatButtonModule, MatIconModule,
    MatProgressBarModule, StatusChipComponent
  ],
  templateUrl: './equipment-detail.component.html'
})
export class EquipmentDetailComponent {
  private service = inject(EquipmentService);
  private assignmentService = inject(AssignmentService);
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  loading = signal(true);
  item = signal<Equipment | null>(null);
  history = signal<Assignment[]>([]);
  isAdmin = this.auth.hasAnyRole('ADMIN');

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.service.get(id).subscribe({
      next: (e) => { this.item.set(e); this.loading.set(false); },
      error: () => this.loading.set(false)
    });
    this.assignmentService.byEquipment(id).subscribe((h) => this.history.set(h));
  }

  edit(): void {
    this.router.navigate(['/equipment/create'], { queryParams: { id: this.item()?.id } });
  }
}
