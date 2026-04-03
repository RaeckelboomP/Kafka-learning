import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import { OrderMessage } from '../models/order-message.model';
import { OrderStatusMessage } from '../models/order-status-message.model';

@Injectable({
  providedIn: 'root'
})
export class WebSocketStompService {
  private client: Client;
  private ordersSubject = new Subject<OrderMessage>();
  private orderStatusSubject = new Subject<OrderStatusMessage>();
  private connectionState = new BehaviorSubject<boolean>(false);
  private reconnectInterval: any;
  private isManuallyDisconnected = false;

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      debug: (str) => console.log(str),
      reconnectDelay: 0,
      onConnect: () => this.onConnect(),
      onDisconnect: () => this.onDisconnect(),
      onWebSocketClose: () => {
        this.connectionState.next(false);
        this.startReconnectLoop();
      },
      onWebSocketError: (event) => {
        this.connectionState.next(false);
        this.startReconnectLoop();
      }
    });
    this.client.activate();
  }

  private onDisconnect() {
    this.connectionState.next(false);
    this.startReconnectLoop();
  }

  private onConnect() {
    this.connectionState.next(true);
    this.stopReconnectLoop();
    // Orders topic
    this.client.subscribe('/topic/orders', (msg: IMessage) => {
      const body: OrderMessage = JSON.parse(msg.body);
      this.ordersSubject.next(body);
    });
    // Order status topic
    this.client.subscribe('/topic/order-status', (msg: IMessage) => {
      const body: OrderStatusMessage = JSON.parse(msg.body);
      this.orderStatusSubject.next(body);
    });
  }

  private startReconnectLoop() {
    if (this.reconnectInterval || this.isManuallyDisconnected) return;
    this.reconnectInterval = setInterval(() => {
      if (!this.client.connected) {
        this.client.deactivate().then(() => {
          this.client.activate();
        });
      }
    }, 3000);
  }

  private stopReconnectLoop() {
    if (this.reconnectInterval) {
      clearInterval(this.reconnectInterval);
      this.reconnectInterval = null;
    }
  }

  connect() {
    this.isManuallyDisconnected = false;
    this.stopReconnectLoop();
    this.client.deactivate().then(() => {
      this.client.activate();
    });
  }

  disconnect() {
    this.isManuallyDisconnected = true;
    this.stopReconnectLoop();
    this.client.deactivate();
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
