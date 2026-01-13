export interface CreateTicketsModel {
  type: string;
  priority: number;
  date_creation: Date;
  user_id: number;
}