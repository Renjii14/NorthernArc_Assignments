export interface ProductRequestDto {
  name: string;
  brand: string;
  category: string;
  cost: number;
}

export interface ProductUpdateDto {
  name?: string;
  brand?: string;
  category?: string;
  cost?: number;
}

export interface ProductResponseDto {
  id: number;
  name: string;
  brand: string;
  category: string;
  cost: number;
}
