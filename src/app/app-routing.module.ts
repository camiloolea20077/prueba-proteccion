import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './modules/auth/auth.guard';
import { DummyComponent } from './shared/components/dummy-component/dummy.component';

const routes: Routes = [
    {
    path: '',
    redirectTo: 'redirector',
    pathMatch: 'full',
  },
  {
    path: '',
    loadComponent: () =>
      import('./shared/components/layout/layout.component').then(
        (c) => c.LayoutComponent
      ),
    children: [
      {
        path: 'tickets',
        loadChildren: () =>
          import('./modules/tickets/tickets.module').then(
            (m) => m.TicketsModule
          ),
          canActivate: [AuthGuard],
      },
    ],
  },
  {
    path: 'redirector',
    canActivate: [AuthGuard],
    component: DummyComponent,
  },
    {
    path: 'login',
    loadComponent: () =>
      import('./shared/components/login/login.component').then(
        (c) => c.LoginComponent
      ),
    canActivate: [AuthGuard],
  },
  {
  path: '**',
  loadComponent: () =>
    import('./shared/components/not-found/not-found.component').then(
      (m) => m.NotFoundComponent
    ),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
