import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Role, UserResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly base = `${API_URL}/users`;
  constructor(private http: HttpClient) {}

  list(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.base);
  }
  get(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.base}/${id}`);
  }
  updateRole(id: number, role: Role): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.base}/${id}/role`, { role });
  }
  updateStatus(id: number, enabled: boolean): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.base}/${id}/status`, { enabled });
  }
}
