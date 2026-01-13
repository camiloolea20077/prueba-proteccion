import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { IFilterTable } from "src/app/shared/utils/models/filter-table";
import { ResponseTableModel } from "src/app/shared/utils/models/response-table.model";
import { IFilterPageTicketsModel } from "../models/tickets/filter-table.tickets.model";
import { PageTicketsModel } from "../models/tickets/page-tickets.model";
import { CreateTicketsModel } from "../models/tickets/create-tickets.model";
import { ResponseModel } from "src/app/shared/utils/models/responde.models";
import { TicketsModels } from "../models/tickets/tickets.model";

@Injectable({ providedIn: 'root' })
export class TicketsService {
    private apiUrl = environment.ticketsUrl;

    constructor(private http: HttpClient) { }

    pageCattle(
        iFilterTable: IFilterTable<IFilterPageTicketsModel>
    ): Observable<ResponseTableModel<PageTicketsModel>> {
        return this.http.post<ResponseTableModel<PageTicketsModel>>(
            `${this.apiUrl}/page`,
            iFilterTable
        );
    }
    createCattle(createDentalPieces: CreateTicketsModel): Observable<ResponseModel<TicketsModels>>{
        return this.http.post<ResponseModel<TicketsModels>>(
            `${this.apiUrl}/create`,
            createDentalPieces
        )
    }
}