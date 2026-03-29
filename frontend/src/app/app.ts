import { Component, signal } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrderCreationMessage } from './models/order-creation-message.model';
import { KafkaStompService } from './services/websocket.service';
import { Subscription } from 'rxjs';
import { KafkaTopic } from './models/kafka-topic.model';
import { orderItems } from './models/order-items.model';
import { OrderMessage } from './models/order-message.model';
import { OrderStatusMessage } from './models/order-status-message.model';
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
