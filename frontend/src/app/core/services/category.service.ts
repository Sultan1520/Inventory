import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config';
import { Category } from '../models/models';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private readonly base = `${API_URL}/categories`;
  constructor(private http: HttpClient) {}

  list(): Observable<Category[]> {
    return this.http.get<Category[]>(this.base);
  }
  create(body: Partial<Category>): Observable<Category> {
    return this.http.post<Category>(this.base, body);
  }
  update(id: number, body: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`${this.base}/${id}`, body);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
