import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import { KafkaMessage } from '../models/kafka-message.model';

@Injectable({
  providedIn: 'root'
})
export class KafkaStompService {
  private client: Client;
  private messageSubject = new Subject<KafkaMessage>();

  // BehaviorSubject holds the latest connection state
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
    this.client.subscribe('/topic/messages', (msg: IMessage) => {
      const body: KafkaMessage = JSON.parse(msg.body);
      this.messageSubject.next(body);
    });
  }

  getConnectionState(): Observable<boolean> {
    return this.connectionState.asObservable();
  }

  getMessages(): Observable<KafkaMessage> {
    return this.messageSubject.asObservable();
  }

  sendMessage(msg: KafkaMessage) {
    this.client.publish({
      destination: '/app/sendMessage',
      body: JSON.stringify(msg)
    });
  }

  close() {
    if (this.client && this.client.active) {
      this.client.deactivate();
    }
  }
}
