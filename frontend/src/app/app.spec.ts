import { TestBed } from '@angular/core/testing';
import { App } from './app';
import { of } from 'rxjs';
import { WebSocketStompService } from './services/websocket.service';

describe('App', () => {
  beforeEach(async () => {
      const mockWsService = {
        connect: jasmine.createSpy('connect'),
        close: jasmine.createSpy('close'),
        getOrdersMessages: jasmine.createSpy('getOrdersMessages').and.returnValue(of([])),
        getOrderStatusMessages: jasmine.createSpy('getOrderStatusMessages').and.returnValue(of([])),
        getConnectionState: jasmine.createSpy('getConnectionState').and.returnValue(of(false)),
      };
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        { provide: WebSocketStompService, useValue: mockWsService }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Kafka order tracker');
  });
});
