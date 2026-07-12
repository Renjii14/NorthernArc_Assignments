import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ProductRequestDto, ProductUpdateDto, ProductResponseDto } from '../dto/ProductDTO';
import { Observable } from 'rxjs/internal/Observable';
@Injectable({ providedIn: 'root' })
export class ProductService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/ecom/product';

  getAllProducts(): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(this.baseUrl);
  }

  getProductById(id: number): Observable<ProductResponseDto> {
    return this.http.get<ProductResponseDto>(`${this.baseUrl}/${id}`);
  }

  createProduct(dto: ProductRequestDto): Observable<ProductResponseDto> {
    return this.http.post<ProductResponseDto>(this.baseUrl, dto);
  }

  updateProduct(id: number, dto: ProductUpdateDto): Observable<ProductResponseDto> {
    return this.http.put<ProductResponseDto>(`${this.baseUrl}/${id}`, dto);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getProductsByCategory(category: string): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.baseUrl}/category/${encodeURIComponent(category)}`);
  }

  getProductsByBrand(brand: string): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.baseUrl}/brand/${encodeURIComponent(brand)}`);
  }

  getProductsByName(name: string): Observable<ProductResponseDto[]> {
    return this.http.get<ProductResponseDto[]>(`${this.baseUrl}/name/${encodeURIComponent(name)}`);
  }
  
}

