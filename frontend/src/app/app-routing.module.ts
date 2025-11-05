import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';
import { ProductPurchaseComponent } from './components/product-purchase/product-purchase.component';


const routes: Routes = [
  { path: 'productos', component: ProductListComponent },
  { path: 'crear', component: ProductFormComponent },
  { path: 'editar/:id', component: ProductFormComponent },
  { path: 'comprar', component: ProductPurchaseComponent },
  { path: '**', redirectTo: 'productos' },
  { path: '', redirectTo: '/productos', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
