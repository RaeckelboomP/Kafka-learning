import { Component, signal } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { KafkaMessage } from './models/kafka-message.model';
import { KafkaStompService } from './services/websocket.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('kafka-frontend');
  webSocketConnected = true;
  messages: KafkaMessage[] = [];
  private sub!: Subscription;

  constructor(private wsService: KafkaStompService) { }

  ngOnInit() {
    this.sub = this.wsService.getMessages().subscribe({
      next: (msg) => this.messages.push(msg),
      error: (err) => console.error('WebSocket error', err),
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.wsService.close();
  }

  get topic1Messages() {
    return this.messages.filter(i => i.topic === 'topic-1');
  }
  get topic2Messages() {
    return this.messages.filter(i => i.topic === 'topic-2');
  }

  messageForm: FormGroup = new FormGroup({
    message: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] })
  });

  sendMessage(topic: string) {
    const message = this.messageForm.value.message?.trim();
    if (!message) return;
    console.log(`Sending "${message}" to ${topic}`);
    this.wsService.sendMessage({ topic, content: message });
  };
}
