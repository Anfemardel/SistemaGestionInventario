import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  productForm!: FormGroup;
  submitted = false;
  successMessage: string = '';
  productId?: number; // Para saber si es actualización
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: [null, [Validators.required, Validators.min(0.01)]], // valor mayor a 0
      stock: [null, [Validators.required, Validators.min(2)]]      // stock mayor a 1
    });
    // Verifica si hay id en la ruta para actualización
  this.productId = this.route.snapshot.params['id'];
  if (this.productId) {
    this.productService.getById(this.productId).subscribe(product => {
      this.productForm.patchValue(product);
    });
  }
  }
  get f() {
    return this.productForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.productForm.invalid) {
      return;
    }

    const product: Product = this.productForm.value;
    if (this.productId) {
    // Actualización
    this.productService.update(this.productId, product).subscribe({
      next: () => {
        this.successMessage = 'Producto actualizado correctamente ✅';
        // Redirigir a la lista después de 2 segundos
        setTimeout(() => this.router.navigate(['/productos']), 2000);
      },
      error: (err) => console.error(err)
    });
  }else{
    this.productService.create(product).subscribe({
      next: (res) => {
        this.successMessage = 'Producto guardado correctamente ✅';
        this.productForm.reset();
        this.submitted = false;
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => console.error(err)
    });
  }}
}
