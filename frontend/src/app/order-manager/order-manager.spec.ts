import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrderManager } from './order-manager';
import { of } from 'rxjs';
import { WebSocketStompService } from '../services/websocket.service';

describe('OrderManager', () => {
  let component: OrderManager;
  let fixture: ComponentFixture<OrderManager>;

  beforeEach(async () => {
    const mockWsService = {
      connect: jasmine.createSpy('connect'),
      close: jasmine.createSpy('close'),
      getOrdersMessages: jasmine.createSpy('getOrdersMessages').and.returnValue(of([])),
      getOrderStatusMessages: jasmine.createSpy('getOrderStatusMessages').and.returnValue(of([])),
      getConnectionState: jasmine.createSpy('getConnectionState').and.returnValue(of(false)),
    };
    await TestBed.configureTestingModule({
      imports: [OrderManager],
      providers: [
        { provide: WebSocketStompService, useValue: mockWsService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OrderManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
