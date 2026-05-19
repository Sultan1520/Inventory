import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Room } from '../models/models';

export interface RoomPayload {
  name: string;
  building?: string;
  floor?: number | null;
  type?: string;
  description?: string;
}

@Injectable({ providedIn: 'root' })
export class RoomService {
  private readonly base = `${API_URL}/rooms`;
  constructor(private http: HttpClient) {}

  list(): Observable<Room[]> {
    return this.http.get<Room[]>(this.base);
  }
  create(body: RoomPayload): Observable<Room> {
    return this.http.post<Room>(this.base, body);
  }
  update(id: number, body: RoomPayload): Observable<Room> {
    return this.http.put<Room>(`${this.base}/${id}`, body);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
