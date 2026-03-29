import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { orderItems } from '../models/order-items.model';
import { KafkaTopic } from '../models/kafka-topic.model';
import { KafkaStompService } from '../services/websocket.service';
import { OrderMessage } from '../models/order-message.model';
import { OrderStatusMessage } from '../models/order-status-message.model';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-manager',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './order-manager.html',
  styleUrl: './order-manager.css',
})
export class OrderManager {
  webSocketConnected = false;
  orderMessages: OrderMessage[] = [];
  orderStatusMessages: OrderStatusMessage[] = [];
  private sub!: Subscription;
  constructor(private wsService: KafkaStompService) { }
  private ordersMap = new Map<string, OrderView>();
  orders: OrderView[] = [];
  expandedOrderId: string | null = null;
  statusMap = new Map<string, OrderStatusMessage[]>();

  ngOnInit() {
    this.wsService.getConnectionState().subscribe(connected => {
      this.webSocketConnected = connected;
    });

    this.sub = this.wsService.getOrdersMessages().subscribe({
      next: (msg) => {
        this.orderMessages.push(msg);
        this.handleOrderMessage(msg);
      },
      error: (err) => console.error('WebSocket error', err),
    });

    this.sub = this.wsService.getOrderStatusMessages().subscribe({
      next: (msg) => {
        console.log('Received order status message:');
        console.log(msg);
        this.orderStatusMessages.push(msg);
        this.handleOrderStatusMessage(msg);
      },
      error: (err) => console.error('WebSocket error', err),
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.wsService.close();
  }

  toggleConnection() {
    if (this.webSocketConnected) {
      this.wsService.close();
    } else {
      this.wsService = new KafkaStompService();
      this.ngOnInit();
    }
  }

  messageForm: FormGroup = new FormGroup({
    message: new FormControl<string>('', Validators.maxLength(20))
  });

  sendMessage() {
    let orderItem = this.messageForm.value.message?.trim();
    if (!orderItem) {
      orderItem = orderItems[Math.floor(Math.random() * orderItems.length)];
    }
    console.log(`Sending "${orderItem}" to ${KafkaTopic.ORDERS}`);
    this.wsService.sendMessage(orderItem);
  };

  send20Messages() {
    for (let i = 0; i < 20; i++) {
      this.sendMessage();
    }
  }

  clearData() {
    this.ordersMap.clear();
    this.statusMap.clear();
    this.orders = [];
    this.orderMessages = [];
    this.orderStatusMessages = [];
    this.expandedOrderId = null;
  }

  handleOrderMessage(msg: OrderMessage) {
    const existing = this.ordersMap.get(msg.orderId);

    if (existing) {
      existing.item = msg.orderItem;
    } else {
      this.ordersMap.set(msg.orderId, {
        id: msg.orderId,
        item: msg.orderItem,
        status: undefined,
        createdAt: new Date(msg.dateTime),
        updatedAt: undefined
      });
    }

    this.refreshOrders();
  }

  handleOrderStatusMessage(msg: OrderStatusMessage) {
    const existing = this.ordersMap.get(msg.orderId);
    const statusExisting = this.statusMap.get(msg.orderId);

    if (existing) {
      existing.status = msg.status;
      existing.updatedAt = new Date(msg.updatedAt);
    } else {
      this.ordersMap.set(msg.orderId, {
        id: msg.orderId,
        item: msg.orderItem,
        status: msg.status,
        createdAt: undefined,
        updatedAt: new Date(msg.updatedAt)
      });
    }

    if (statusExisting) {
      statusExisting.push(msg);
    } else {
      this.statusMap.set(msg.orderId, [msg]);
    }

    this.refreshOrders();
  }

  getFilteredStatusOrders(orderId: string): OrderStatusMessage[] {
    return this.orderStatusMessages.filter(statusMsg => statusMsg.orderId === orderId);
  }

  toggleRow(orderId: string) {
    this.expandedOrderId =
      this.expandedOrderId === orderId ? null : orderId;
  }

  private refreshOrders() {
    this.orders = Array.from(this.ordersMap.values());
  }

}
