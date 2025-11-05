import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { ProductService } from '../../services/product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  loading = true;
  error = '';

  constructor(private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.loadProducts();
  }
  loadProducts(): void {
    this.loading = true;
    this.productService.getAll().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Error al cargar productos';
        this.loading = false;
      }
    });
  }
  deleteProduct(id: number | undefined) {
    if (!id) return;

    const confirmed = confirm('¿Estás seguro de que deseas borrar este producto?');
    if (!confirmed) return;

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        // Actualiza la lista local para reflejar el borrado
        this.products = this.products.filter(p => p.id !== id);
      },
      error: (err) => {
        console.error(err);
        alert('Ocurrió un error al borrar el producto.');
      }
    });
  }
  editProduct(id: number | undefined) {
    if (!id) return;
    // Redirige al formulario de edición
    this.router.navigate(['editar', id]);
  }

}
