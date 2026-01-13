export interface PageTicketsModel {
    id: number;
    type: string;
    priority: number;
    date_creation: Date;
    user_id: number;
    username: string;
    priority_final: number;
}