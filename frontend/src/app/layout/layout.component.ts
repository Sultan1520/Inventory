import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { AuthService } from '../core/services/auth.service';
import { Role } from '../core/models/models';

interface NavItem {
  label: string;
  icon: string;
  link: string;
  roles?: Role[];
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    CommonModule, RouterOutlet, RouterLink, RouterLinkActive,
    MatToolbarModule, MatSidenavModule, MatListModule, MatIconModule,
    MatButtonModule, MatMenuModule
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  private auth = inject(AuthService);
  private router = inject(Router);
  private bp = inject(BreakpointObserver);

  user = this.auth.user;
  opened = signal(true);

  isHandset = toSignal(
    this.bp.observe([Breakpoints.Handset]).pipe(map((r) => r.matches)),
    { initialValue: false }
  );

  private allNav: NavItem[] = [
    { label: 'Dashboard', icon: 'dashboard', link: '/dashboard' },
    { label: 'Equipment', icon: 'devices', link: '/equipment' },
    { label: 'Categories', icon: 'category', link: '/categories' },
    { label: 'Rooms', icon: 'meeting_room', link: '/rooms' },
    { label: 'Assignments', icon: 'assignment_ind', link: '/assignments' },
    { label: 'Repair Requests', icon: 'build', link: '/repair-requests' },
    { label: 'Users', icon: 'group', link: '/users', roles: ['ADMIN'] },
    { label: 'Reports', icon: 'bar_chart', link: '/reports', roles: ['ADMIN', 'MANAGER'] }
  ];

  nav = computed(() =>
    this.allNav.filter((n) => !n.roles || this.auth.hasAnyRole(...n.roles))
  );

  toggle(): void {
    this.opened.update((v) => !v);
  }

  closeIfHandset(): void {
    if (this.isHandset()) this.opened.set(false);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
