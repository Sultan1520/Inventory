import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RepairService } from '../../core/services/repair.service';
import { AuthService } from '../../core/services/auth.service';
import { NotifyService } from '../../core/services/notify.service';
import {
  EquipmentCondition, RepairComment, RepairRequest, RequestStatus
} from '../../core/models/models';
import { StatusChipComponent } from '../../shared/status-chip.component';

@Component({
  selector: 'app-repair-detail',
  standalone: true,
  imports: [
    CommonModule, RouterLink, FormsModule, MatButtonModule, MatIconModule,
    MatFormFieldModule, MatInputModule, MatSelectModule, MatProgressBarModule,
    StatusChipComponent
  ],
  templateUrl: './repair-detail.component.html'
})
export class RepairDetailComponent {
  private service = inject(RepairService);
  private auth = inject(AuthService);
  private notify = inject(NotifyService);
  private route = inject(ActivatedRoute);

  readonly statuses: RequestStatus[] = ['NEW', 'IN_PROGRESS', 'DONE', 'REJECTED'];
  readonly conditions: EquipmentCondition[] = ['NEW', 'GOOD', 'NEEDS_REPAIR', 'BROKEN'];

  id = Number(this.route.snapshot.paramMap.get('id'));
  loading = signal(true);
  item = signal<RepairRequest | null>(null);
  comments = signal<RepairComment[]>([]);

  newComment = '';
  newStatus: RequestStatus = 'IN_PROGRESS';
  resultingCondition: EquipmentCondition | null = null;

  canManage = this.auth.hasAnyRole('ADMIN', 'IT_SPECIALIST');

  constructor() {
    this.reload();
    this.loadComments();
  }

  reload(): void {
    this.service.get(this.id).subscribe({
      next: (r) => {
        this.item.set(r);
        this.newStatus = r.status;
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  loadComments(): void {
    this.service.comments(this.id).subscribe((c) => this.comments.set(c));
  }

  addComment(): void {
    if (!this.newComment.trim()) return;
    this.service.addComment(this.id, this.newComment.trim()).subscribe({
      next: () => { this.newComment = ''; this.notify.success('Комментарий добавлен'); this.loadComments(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }

  takeInWork(): void {
    const uid = this.auth.user()?.id;
    if (!uid) return;
    this.service.assign(this.id, uid).subscribe({
      next: () => { this.notify.success('Заявка взята в работу'); this.reload(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }

  updateStatus(): void {
    this.service.updateStatus(this.id, this.newStatus, this.resultingCondition ?? undefined).subscribe({
      next: () => { this.notify.success('Статус обновлён'); this.reload(); },
      error: (e) => this.notify.error(e?.error?.message ?? 'Ошибка')
    });
  }
}
