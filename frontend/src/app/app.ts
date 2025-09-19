import { Component, signal } from '@angular/core';
import { FormGroup, ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('kafka-frontend');
  webSocketConnected = true;
  items = [
  {message: 'Sword', topic: 'topic-1'},
  {message: 'Potion', topic: 'topic-1'},
  {message: 'Shield', topic: 'topic-2'},
  ];

  get topic1Items() {
  return this.items.filter(i => i.topic === 'topic-1');
}
  get topic2Items() {
  return this.items.filter(i => i.topic === 'topic-2');
}

  messageForm: FormGroup = new FormGroup({
    message: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] })
  });

  sendMessage(topic: string) {
    const message = this.messageForm.value.message?.trim();
    if (!message) return;
    console.log(`Sending "${message}" to ${topic}`);
    this.items.push({message, topic});
    this.messageForm.setValue({ message: '' });
  };
}
