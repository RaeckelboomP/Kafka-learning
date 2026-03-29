export interface OrderStatusMessage {
  status: "PROCESSING" | "SHIPPED" | "IN_TRANSIT" | "DELIVERED" | "ERROR";
  orderItem: string;
  updatedAt: string;
  orderId: string;
}