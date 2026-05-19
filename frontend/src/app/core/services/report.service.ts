import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Assignment, Equipment, RepairRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly base = `${API_URL}/reports`;
  constructor(private http: HttpClient) {}

  equipmentByRoom(): Observable<Record<string, Equipment[]>> {
    return this.http.get<Record<string, Equipment[]>>(`${this.base}/equipment-by-room`);
  }
  equipmentInRepair(): Observable<Equipment[]> {
    return this.http.get<Equipment[]>(`${this.base}/equipment-in-repair`);
  }
  assignedEquipment(): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.base}/assigned-equipment`);
  }
  writtenOff(): Observable<Equipment[]> {
    return this.http.get<Equipment[]>(`${this.base}/written-off-equipment`);
  }
  repairHistory(): Observable<RepairRequest[]> {
    return this.http.get<RepairRequest[]>(`${this.base}/repair-history`);
  }
  exportEquipmentCsv(): Observable<Blob> {
    return this.http.get(`${this.base}/export/equipment.csv`, { responseType: 'blob' });
  }
}
