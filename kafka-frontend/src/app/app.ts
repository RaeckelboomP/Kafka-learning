import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [FormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('kafka-frontend');
  webSocketConnected = true;
  messageInput = '';
  items = [
  {message: 'Sword'},
  {message: 'Potion'},
  {message: 'Shield'},
  ];
}
