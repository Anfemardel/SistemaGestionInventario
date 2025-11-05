import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private apiUrl = '/api/inventories';

  constructor(private http: HttpClient) { }
  getQuantity(productId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${productId}`);
  }

  purchase(productId: number, quantity: number): Observable<any> {
  return this.http.put(`${this.apiUrl}/${productId}/purchase`, { quantity });
}

}
