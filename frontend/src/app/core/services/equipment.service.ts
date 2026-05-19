import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Equipment, EquipmentRequest, EquipmentStatus, PageResponse } from '../models/models';

export interface EquipmentQuery {
  search?: string;
  categoryId?: number | null;
  roomId?: number | null;
  status?: EquipmentStatus | null;
  page?: number;
  size?: number;
  sort?: string;
}

@Injectable({ providedIn: 'root' })
export class EquipmentService {
  private readonly base = `${API_URL}/equipment`;
  constructor(private http: HttpClient) {}

  list(q: EquipmentQuery): Observable<PageResponse<Equipment>> {
    let p = new HttpParams();
    if (q.search) p = p.set('search', q.search);
    if (q.categoryId) p = p.set('categoryId', q.categoryId);
    if (q.roomId) p = p.set('roomId', q.roomId);
    if (q.status) p = p.set('status', q.status);
    p = p.set('page', q.page ?? 0).set('size', q.size ?? 10);
    if (q.sort) p = p.set('sort', q.sort);
    return this.http.get<PageResponse<Equipment>>(this.base, { params: p });
  }

  get(id: number): Observable<Equipment> {
    return this.http.get<Equipment>(`${this.base}/${id}`);
  }

  create(body: EquipmentRequest): Observable<Equipment> {
    return this.http.post<Equipment>(this.base, body);
  }

  update(id: number, body: EquipmentRequest): Observable<Equipment> {
    return this.http.put<Equipment>(`${this.base}/${id}`, body);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
