import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-chip',
  standalone: true,
  template: `<span class="status-chip" [class]="cssClass">{{ label }}</span>`,
  styles: [`
    .status-chip {
      display: inline-block;
      padding: 2px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;
      line-height: 20px;
      white-space: nowrap;
    }
    .c-green  { background:#e6f4ea; color:#1e7e34; }
    .c-blue   { background:#e8f0fe; color:#1967d2; }
    .c-orange { background:#fef3e0; color:#c25e00; }
    .c-red    { background:#fce8e6; color:#c5221f; }
    .c-grey   { background:#eceff1; color:#546e7a; }
    .c-purple { background:#f3e8fd; color:#7b1fa2; }
  `]
})
export class StatusChipComponent {
  @Input({ required: true }) value!: string;

  private static readonly MAP: Record<string, string> = {
    AVAILABLE: 'c-green',
    ASSIGNED: 'c-blue',
    IN_REPAIR: 'c-orange',
    LOST: 'c-red',
    WRITTEN_OFF: 'c-grey',
    NEW: 'c-blue',
    IN_PROGRESS: 'c-orange',
    DONE: 'c-green',
    REJECTED: 'c-red',
    ACTIVE: 'c-green',
    RETURNED: 'c-grey',
    CANCELLED: 'c-red',
    LOW: 'c-grey',
    MEDIUM: 'c-blue',
    HIGH: 'c-orange',
    CRITICAL: 'c-red',
    NEW_C: 'c-green',
    GOOD: 'c-green',
    NEEDS_REPAIR: 'c-orange',
    BROKEN: 'c-red'
  };

  get cssClass(): string {
    return StatusChipComponent.MAP[this.value] ?? 'c-purple';
  }
  get label(): string {
    return this.value ? this.value.replace(/_/g, ' ') : '';
  }
}
