# Kafka order processing demo
Real-time order processing system demonstrating event-driven architecture using Kafka, WebSocket, and hexagonal architecture.

Simulates how modern backend systems handle asynchronous workflows and real-time updates.

This project uses **Java 21**, **Angular 20**, **Kafka**, **DevContainer** and **Docker**.

## Why this project ?
This project demonstrates how a real-world system can:
- decouple services using Kafka
- process events asynchronously
- stream updates in real-time to clients
- remain maintainable using hexagonal architecture

## Key concepts demonstrated
- Event-driven architecture (Kafka)
- Asynchronous processing
- Real-time communication (WebSocket)
- Hexagonal architecture (ports & adapters)
- Fullstack integration (Spring + Angular)

## Quick demo 
<img alt="Quick demo" src="quick-demo.gif" width="900" style="display:block;margin-left:auto;margin-right:auto;">

## Architecture

- Java application (kafka producer and consumer)
- Kafka broker
- Angular application

### Backend
The backend follows a hexagonal architecture:
- Domain logic is isolated from infrastructure
- Kafka and WebSocket are implemented as adapters
- It makes the system easily testable and extensible

### Event flow
- User creates an order from the UI
- OrderEvent is sent to Kafka (`orders` topic)
- Consumer processes the event
- OrderStatusEvents are produced (`order-status` topic)
- Updates are streamed to the frontend via WebSocket

<img alt="Application's architecture and process" src="frontend/src/assets/images/kafka_websocket_architecture.png" width="900" style="display:block;margin-left:auto;margin-right:auto;">

## How to launch the application

With Docker you can launch the whole application with the following command:
```bash
docker compose up -d
```
Then go to localhost:4200 with your browser.

## How to use the application

### WebSocket connection
Open http://localhost:4200/ on your browser. The WebSocketService will try to connect to the backend, if nothing happens, check the backend.

### Orders handling
- Create one or multiple orders from the UI
- Status updates are displayed in real-time
- Each order can be expanded to see its history
- You can clear the array with the button "Clear data"

## How to test the application
### Java backend
- Unit tests using JUnit and Mockito  
- Isolation of business logic through hexagonal architecture (ports & adapters)
- Validation of Kafka consumers, application services, and domain services

### Frontend
- Component testing with Angular TestBed and Jasmine  
- Mocking of WebSocket (STOMP) services
- Validation of UI rendering and reactive data flows (RxJS)

### Tooling
Maven (`mvn test`) and Angular CLI (`ng test`)

## Development setup (optional)

You can either use the [Dev container extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) to get an environment able to work on and run the application, or install the required tools :

- Java 21
- Maven 3.8.7
- Angular 20.3.2
- npm 10.8.2

### Kafka

- Open a terminal at the root of the project
- ```
  docker compose up -f docker-compose-kafka.yml -d
### Java backend

- Open a terminal
- ```
  cd backend-java
  mvn spring-boot:run
### Frontend

- Open a new terminal
- ```
  cd frontend
  ng serve --host 0.0.0.0 --port 4200 // or npm start

## Notes on the configuration for devcontainer usage (optional)

In the docker-compose for kafka :
```KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://host.docker.internal:9092```
KAFKA_ADVERTISED_LISTENERS is the address advertised by kafka to the listeners.
We use ```host.docker.internal:9092``` to reach the port 9092 within the docker internal network.
Because I'm working in a devcontainer, the two containers are in different networks, so I must use the internal docker network to reach kafka.

In the java application.properties :
```spring.kafka.bootstrap-servers=host.docker.internal:9092```
We simply tell the address of kafka. We cannot use kafka's DNS "broker" because it is not known inside the devcontainer.
