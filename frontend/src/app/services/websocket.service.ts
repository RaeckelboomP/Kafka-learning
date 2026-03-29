import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import { OrderCreationMessage } from '../models/order-creation-message.model';
import { OrderMessage } from '../models/order-message.model';
import { OrderStatusMessage } from '../models/order-status-message.model';

@Injectable({
  providedIn: 'root'
})
export class KafkaStompService {
  private client: Client;
  private ordersSubject = new Subject<OrderMessage>();
  private orderStatusSubject = new Subject<OrderStatusMessage>();
  private connectionState = new BehaviorSubject<boolean>(false);

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: () => this.onConnect(),
      onDisconnect: () => this.onDisconnect()
    });

    this.client.activate();
  }

  private onDisconnect() {
    console.log('STOMP disconnected');
    this.connectionState.next(false);
  }

  private onConnect() {
    console.log('STOMP connected');
    this.connectionState.next(true);
    // Orders topic
    this.client.subscribe('/topic/orders', (msg: IMessage) => {
      console.log(msg);
      const body: OrderMessage = JSON.parse(msg.body);
      this.ordersSubject.next(body);
    });
    // Order status topic
    this.client.subscribe('/topic/order-status', (msg: IMessage) => {
      const body: OrderStatusMessage = JSON.parse(msg.body);
      this.orderStatusSubject.next(body);
    });

  }

  getConnectionState(): Observable<boolean> {
    return this.connectionState.asObservable();
  }

  getOrdersMessages(): Observable<OrderMessage> {
    return this.ordersSubject.asObservable();
  }

  getOrderStatusMessages(): Observable<OrderStatusMessage> {
    return this.orderStatusSubject.asObservable();
  }

  sendMessage(msg: string) {
    this.client.publish({
      destination: '/app/sendMessage',
      body: msg
    });
  }

  close() {
    if (this.client && this.client.active) {
      this.client.deactivate();
    }
  }
}
