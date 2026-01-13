import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TooltipModule } from 'primeng/tooltip';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from 'src/app/core/services/auth.service';
import { SidebarService } from '../../services/sidebar.service';
import { IndexDBService } from 'src/app/core/services/index-db.service';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  imports: [
    RouterLink, 
    CommonModule, 
    ToastModule, 
    RouterModule, 
    ButtonModule, 
    RippleModule,
    TooltipModule,
    ConfirmDialogModule
  ],
  animations: [
    trigger('slideDown', [
      transition(':enter', [
        style({ height: '0', opacity: 0 }),
        animate('200ms ease-in-out', style({ height: '*', opacity: 1 }))
      ]),
      transition(':leave', [
        animate('200ms ease-in-out', style({ height: '0', opacity: 0 }))
      ])
    ])
  ],
  providers: [ConfirmationService, MessageService]
})
export class SidebarComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  userName: string = '';
  userRole: string = '';
  items: MenuItem[] = [];
  expandedItems: Record<string, boolean> = {};
  isMobile = false;

  constructor(
    private indexDBService: IndexDBService,
    private authService: AuthService,
    public sidebarService: SidebarService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private router: Router,
  ) {
    this.checkScreenSize();
  }

  onResize() {
    this.checkScreenSize();
  }

  async ngOnInit(): Promise<void> {
    await this.loadUserData();
    this.loadMenuItems();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private async loadUserData() {
    try {
      const authData = await this.indexDBService.loadDataAuthDB();
      if (authData?.user) {
        this.userName = authData.user.name || authData.user.email || 'Usuario';
        this.userRole = authData.user.role || 'Administrador';
      }
    } catch (error) {
      console.error('Error cargando datos del usuario:', error);
      this.userName = 'Usuario';
      this.userRole = 'Administrador';
    }
  }
  // Alternativamente, una versión más simple sería:
  private checkScreenSize(): void {
    this.isMobile = window.innerWidth <= 768;

    if (this.isMobile) {
      this.sidebarService.setCollapsed(false);
      this.sidebarService.setVisible(false);
    } else {
      this.sidebarService.setVisible(true);
    }
  }

  private loadMenuItems(): void {
    this.authService.getAuthResponse()
      .pipe(takeUntil(this.destroy$))
      .subscribe((auth) => {
        const permisos = auth?.user?.permisos ?? [];
        this.buildMenu();
      });
  }
private buildMenu(): void {
  // Menú base (elementos principales)
  this.items = [
    { 
      label: 'Tickets', 
      icon: 'pi pi-home', 
      route: '/tickets',
      exact: true 
    },
  ];

}


  toggleSidebar(): void {
    this.sidebarService.toggleSidebar();
  }

  closeSidebar(): void {
    if (this.isMobile) {
      this.sidebarService.closeSidebar();
    }
  }

  toggleExpanded(): void {
    this.sidebarService.toggleCollapsed();
    if (this.sidebarService.isCollapsed) {
      // Colapsar todos los submenús cuando se colapsa el sidebar
      this.expandedItems = {};
    }
  }

  toggleItem(label: string): void {
    if (this.sidebarService.isCollapsed) {
      this.sidebarService.toggleCollapsed();
    }
    this.expandedItems[label] = !this.expandedItems[label];
  }

  isItemExpanded(label: string): boolean {
    return !!this.expandedItems[label];
  }
logout() {
    this.confirmationService.confirm({
      message: '¿Está seguro que desea cerrar la sesión?',
      header: 'Confirmar cierre de sesión',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, cerrar sesión',
      rejectLabel: 'Cancelar',
      accept: async () => {
        try {
          // Limpiar datos de IndexedDB
          await this.indexDBService.deleteDataAuthDB();
          
          // Mostrar mensaje de éxito
          this.messageService.add({
            severity: 'success',
            summary: 'Sesión cerrada',
            detail: 'Ha cerrado sesión correctamente'
          });

          // Redirigir al login
          this.router.navigate(['/login']);
        } catch (error) {
          console.error('Error cerrando sesión:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Error al cerrar la sesión'
          });
        }
      }
    });
  }
}