import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { InventoryService } from '../../services/inventory.service';
import { Product } from '../../models/product';
import { forkJoin } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-purchase',
  templateUrl: './product-purchase.component.html',
  styleUrls: ['./product-purchase.component.css']
})
export class ProductPurchaseComponent implements OnInit {

  products: Product[] = [];
  compraForm!: FormGroup;
  successMessage = '';
  submitted = false;
  total = 0;
  

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private inventoryService: InventoryService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.productService.getAll().subscribe(data => {
      this.products = data;
      this.initForm();
      this.loadStocks();
      this.listenToChanges();
    });
  }
  initForm() {
    this.compraForm = this.fb.group({
      items: this.fb.array(this.products.map(p => this.fb.group({
        productId: [p.id],
        name: [p.name],
        price: [p.price],
        quantity: [0, [Validators.min(1)]],
        available: [null]
      })))
    });
  }
  // Cargar los stocks reales de cada producto
  loadStocks() {
    const stockObservables = this.products.map(p => this.inventoryService.getQuantity(p.id!));
    forkJoin(stockObservables).subscribe(stocks => {
      stocks.forEach((qty, index) => {
        this.items.at(index).patchValue({ available: qty });
      });
    });
  }
  get items(): FormArray {
    return this.compraForm.get('items') as FormArray;
  }
  // 👇 Escucha cambios en el formulario para actualizar el total automáticamente
  listenToChanges() {
    this.compraForm.valueChanges.subscribe(() => {
      this.total = this.getTotal();
    });
  }

  getTotal(): number {
    return this.items.controls.reduce((acc, item) => {
      const qty = item.get('quantity')?.value || 0;
      const price = item.get('price')?.value || 0;
      return acc + qty * price;
    }, 0);
  }
  onSubmit() {
    this.submitted = true;

  const selectedItems = this.items.controls
    .map(c => ({
      productId: c.get('productId')?.value,
      quantity: c.get('quantity')?.value
    }))
    .filter(i => i.quantity > 0);

  if (selectedItems.length === 0) {
    alert('Debes seleccionar al menos un producto con cantidad mayor a 0');
    return;
  }

  let completed = 0;

  selectedItems.forEach(item => {
    this.inventoryService.purchase(item.productId, item.quantity).subscribe({
      next: () => {
        completed++;
        console.log(`Compra realizada del producto ${item.productId}`);

        // Si ya se procesaron todas las compras
        if (completed === selectedItems.length) {
          alert('Compra realizada correctamente ✅');
          this.router.navigate(['/productos']); // ajusta la ruta de tu listado
        }
      },
      error: (err) => {
        console.error('Error al realizar la compra:', err);
        alert(`Error al comprar el producto ${item.productId}`);
      }
    });
  });
  }
}
