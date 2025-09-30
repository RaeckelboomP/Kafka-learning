import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject, Observable } from 'rxjs';
import { KafkaMessage } from '../models/kafka-message.model';

@Injectable({
  providedIn: 'root'
})
export class KafkaStompService {
  private client: Client;
  private messageSubject = new Subject<KafkaMessage>();

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (str) => console.log(str),
      reconnectDelay: 5000
    });

    this.client.onConnect = () => {
      console.log('STOMP connected');

      this.client.subscribe('/topic/messages', (msg: IMessage) => {
        const body: KafkaMessage = JSON.parse(msg.body);
        this.messageSubject.next(body);
      });
    };

    this.client.activate();
  }

  getMessages(): Observable<KafkaMessage> {
    return this.messageSubject.asObservable();
  }

  sendMessage(msg: KafkaMessage) {
    this.client.publish({
      destination: '/app/sendMessage', // corresponds to @MessageMapping("/sendMessage")
      body: JSON.stringify(msg)
    });
  }

  close() {
    if (this.client && this.client.active) {
      this.client.deactivate();
      console.log('STOMP disconnected');
    }
  }
}
