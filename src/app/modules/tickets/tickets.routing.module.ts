import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

const routes: Routes = [
    {
        path: '',
        loadComponent: () =>import('./UI/page/index-cattle/index-cattle.component').then(
            (c) => c.IndexCattleComponent
        )
    },
    {
        path: 'create',
        data:{
            slug: 'create',
            title: 'Nuevo Ticket',
        },
        loadComponent: () =>import('./UI/components/form/form-tickets.component').then(
            (c) => c.FormTicketComponent
        )
    },
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TicketsRoutingModule {}