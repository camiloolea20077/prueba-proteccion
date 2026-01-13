import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {
    FormBuilder,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { lastValueFrom } from 'rxjs';
import { CreateTicketsModel } from 'src/app/core/models/tickets/create-tickets.model';
import { TicketsService } from 'src/app/core/services/tickets.service';
import { ResponseModel } from 'src/app/shared/utils/models/responde.models';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { ButtonModule } from 'primeng/button';
import { DialogModule } from "primeng/dialog";
import { AlertService } from 'src/app/shared/pipes/alert.service';
import { TicketsModels } from 'src/app/core/models/tickets/tickets.model';

@Component({
    selector: 'app-form-ticket',
    standalone: true,
    templateUrl: './form-tickets.component.html',
    styleUrls: ['./form-tickets.component.scss'],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        DropdownModule,
        RouterModule,
        CalendarModule,
        InputTextModule,
        ToastModule,
        ConfirmDialogModule,
        ButtonModule,
        DialogModule
    ],
    providers: [MessageService, ConfirmationService],
})
export class FormTicketComponent implements OnInit {
    @Input() displayModal: boolean = false;
    @Input() ticketId: number | null = null;
    @Input() slug: string = 'create';
    @Output() modalClosed = new EventEmitter<void>();
    @Output() ticketSaved = new EventEmitter<TicketsModels>();

    public frmTicket!: FormGroup;
    public isEditMode: boolean = false;
    public isSubmitting: boolean = false;
    public today: Date = new Date();

    priorityOptions = [
        { label: 'Baja', value: 1 },
        { label: 'Media', value: 2 },
        { label: 'Alta', value: 3 },
        { label: 'Crítica', value: 4 }
    ];

    constructor(
        private readonly ticketsService: TicketsService,
        private readonly messageService: MessageService,
        private readonly _alertService: AlertService,
        private readonly formBuilder: FormBuilder
    ) {}

    ngOnInit() {
        this.loadForm();
    }

    ngOnChanges() {
    }

    loadForm() {
        this.frmTicket = this.formBuilder.group({
            id: [null],
            type: [null, Validators.required],
            priority: [null, Validators.required],
            date_creation: [new Date(), Validators.required]
        });
    }

    loadTicketData(ticket: TicketsModels) {
        this.frmTicket.patchValue({
            id: ticket.id,
            type: ticket.type,
            priority: ticket.priority,
            date_creation: new Date(ticket.date_creation)
        });
    }

    resetForm() {
        this.frmTicket.reset({
            date_creation: new Date()
        });
    }

    async buildDataTicket(): Promise<CreateTicketsModel> {
        const formValue = this.frmTicket.value;
        
        return {
            type: formValue.type,
            priority: formValue.priority,
            date_creation: formValue.date_creation,
            user_id: 1 // Este valor debería venir del usuario logueado
        };
    }

    async buildsaveTicket(): Promise<void> {
        const msgSystem = 'Alerta del sistema';
        const msgText = 'Complete el formulario correctamente';

        if (this.isFormInvalid()) {
            this.markFormAsTouched();
            this._alertService.showError(msgSystem, msgText);
            return;
        }

        this.isSubmitting = true;
        const data: CreateTicketsModel = await this.buildDataTicket();
        
        try {
            const response = await this.saveTicket(data);
            if (response) {
                this.handleResponse(response);
            }
        } catch (error: any) {
            const msg = 'Error al guardar el ticket';
            this.showErrorMessage(msg);
        } finally {
            this.isSubmitting = false;
        }
    }

    isFormInvalid(): boolean {
        return this.frmTicket.invalid;
    }

    markFormAsTouched(): void {
        this.frmTicket.markAllAsTouched();
    }

    private handleResponse(response: ResponseModel<TicketsModels | boolean>): void {
        if (response?.status === 200 || response?.status === 201) {
            const message = this.isEditMode
                ? 'Ticket actualizado correctamente'
                : 'Ticket creado correctamente';
            
            this.messageService.add({
                severity: 'success',
                summary: 'Operación exitosa',
                detail: message,
                life: 5000,
            });

            // Emitir evento de ticket guardado
            if (response.data && typeof response.data === 'object') {
                this.ticketSaved.emit(response.data as TicketsModels);
            }

            this.closeModal();
        }
    }

    private async saveTicket(
        data: CreateTicketsModel
    ): Promise<ResponseModel<boolean | TicketsModels> | void> {
        return lastValueFrom(
            this.ticketsService.createCattle(data)
        ).catch((error) => {
            this.showErrorMessage(error.message);
        });
    }


    private showErrorMessage(message: string): void {
        const msgSystem = 'Alerta del sistema';
        this._alertService.showError(msgSystem, message ?? 'Error desconocido');
    }

    closeModal() {
        this.displayModal = false;
        this.resetForm();
        this.modalClosed.emit();
    }

    onModalHide() {
        this.closeModal();
    }
}