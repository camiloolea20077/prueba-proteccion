import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { GlobalInterceptor } from './modules/auth/auth.interceptor';
import { MessageService } from 'primeng/api';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    SidebarComponent,
    NavbarComponent,
    LayoutComponent,
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: GlobalInterceptor, multi: true },MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
