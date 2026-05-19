import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import {
  EquipmentCondition, Priority, RepairComment, RepairRequest, RequestStatus
} from '../models/models';

export interface RepairCreateRequest {
  title: string;
  description?: string;
  equipmentId?: number | null;
  priority?: Priority;
  markEquipmentInRepair: boolean;
}

@Injectable({ providedIn: 'root' })
export class RepairService {
  private readonly base = `${API_URL}/repair-requests`;
  constructor(private http: HttpClient) {}

  list(): Observable<RepairRequest[]> {
    return this.http.get<RepairRequest[]>(this.base);
  }
  get(id: number): Observable<RepairRequest> {
    return this.http.get<RepairRequest>(`${this.base}/${id}`);
  }
  create(body: RepairCreateRequest): Observable<RepairRequest> {
    return this.http.post<RepairRequest>(this.base, body);
  }
  updateStatus(id: number, status: RequestStatus, resultingCondition?: EquipmentCondition): Observable<RepairRequest> {
    return this.http.put<RepairRequest>(`${this.base}/${id}/status`, { status, resultingCondition });
  }
  assign(id: number, assignedToUserId: number): Observable<RepairRequest> {
    return this.http.put<RepairRequest>(`${this.base}/${id}/assign`, { assignedToUserId });
  }
  comments(id: number): Observable<RepairComment[]> {
    return this.http.get<RepairComment[]>(`${this.base}/${id}/comments`);
  }
  addComment(id: number, text: string): Observable<RepairComment> {
    return this.http.post<RepairComment>(`${this.base}/${id}/comments`, { text });
  }
}
