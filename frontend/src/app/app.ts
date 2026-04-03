import { Component, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrderManager } from "./order-manager/order-manager";

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, CommonModule, OrderManager],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('kafka-frontend');

}
