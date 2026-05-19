import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Assignment } from '../models/models';

export interface AssignmentRequest {
  equipmentId: number;
  assignedToUserId?: number | null;
  assignedToRoomId?: number | null;
  comment?: string;
}

@Injectable({ providedIn: 'root' })
export class AssignmentService {
  private readonly base = `${API_URL}/assignments`;
  constructor(private http: HttpClient) {}

  list(): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(this.base);
  }
  byEquipment(equipmentId: number): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.base}/equipment/${equipmentId}`);
  }
  assign(body: AssignmentRequest): Observable<Assignment> {
    return this.http.post<Assignment>(this.base, body);
  }
  return(id: number): Observable<Assignment> {
    return this.http.put<Assignment>(`${this.base}/${id}/return`, {});
  }
}
