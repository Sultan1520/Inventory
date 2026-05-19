import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/layout.component').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
      },
      {
        path: 'equipment',
        loadComponent: () =>
          import('./features/equipment/equipment-list.component').then((m) => m.EquipmentListComponent)
      },
      {
        path: 'equipment/create',
        loadComponent: () =>
          import('./features/equipment/equipment-form.component').then((m) => m.EquipmentFormComponent)
      },
      {
        path: 'equipment/:id',
        loadComponent: () =>
          import('./features/equipment/equipment-detail.component').then((m) => m.EquipmentDetailComponent)
      },
      {
        path: 'categories',
        loadComponent: () =>
          import('./features/categories/categories.component').then((m) => m.CategoriesComponent)
      },
      {
        path: 'rooms',
        loadComponent: () =>
          import('./features/rooms/rooms.component').then((m) => m.RoomsComponent)
      },
      {
        path: 'assignments',
        loadComponent: () =>
          import('./features/assignments/assignments.component').then((m) => m.AssignmentsComponent)
      },
      {
        path: 'repair-requests',
        loadComponent: () =>
          import('./features/repair-requests/repair-list.component').then((m) => m.RepairListComponent)
      },
      {
        path: 'repair-requests/:id',
        loadComponent: () =>
          import('./features/repair-requests/repair-detail.component').then((m) => m.RepairDetailComponent)
      },
      {
        path: 'users',
        canActivate: [roleGuard('ADMIN')],
        loadComponent: () =>
          import('./features/users/users.component').then((m) => m.UsersComponent)
      },
      {
        path: 'reports',
        canActivate: [roleGuard('ADMIN', 'MANAGER')],
        loadComponent: () =>
          import('./features/reports/reports.component').then((m) => m.ReportsComponent)
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
