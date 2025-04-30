## [APP-usermanagementtool]

---

## Introduction

This project is part of a broader **General Management Tool** (using the general/abstract interface ManagementToolClientFactory), designed to be modular and extensible. The current module focuses on **User management**, providing all necessary CRUD operations to handle user entities. It is built using **Java 21 with Spring Boot**.

The architecture follows a clean **Model-View-Controller (MVC)** structure and supports both **RESTful HTTP** and **gRPC** interfaces to meet different client communication needs. The API is defined using an **API-First** approach, ensuring consistency and contract clarity across teams.

---

## Quickstart

### Installation

- Maven 
- Java 21
- Docker


### Local execution

#### 1º Compilation

- Maven

  `mvn clean install`

Build success output:
[INFO] Results:
[INFO]
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[...]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:01 min
[INFO] Finished at: 2025-04-30T01:09:50+02:00
[INFO] ------------------------------------------------------------------------

Process finished with exit code 0

#### 2º Execution

(No certs needed in our JDK)

- Launch Docker (to run MongoDB image to test locally)

  `docker compose down`
  `docker compose up --build`

- Launch application

  `spring-boot:run`

After start our Docker container and launching our REST/GRPC application through Spring, we can consume every service inside the "CRUD Management Tool - User resource.postman_collection.json" attached in the main ZIP with I provided.
You can see in "CRUD Management Tool - User resource.postman_test_run.json" (attached too) a Run collection executed succesfully from Postman.

Highly recommended: 

  a) Postman automatic run --> Import the collection in Postman, click on the "More/Generate tests" and click on "Run collection" button. Please, set a delay minimum as 3000ms.
  b) Postman manually run --> Run each request manually (on the one hand "CRUD Management Tool - User resource/API REST" and on the other hand "CRUD Management Tool - User resource/GRPC")


#### Execution integration Tests

`mvn clean test`

It's a good idea to run the tests in order, as they gradually create user resources. mvn clean install itself runs these tests, as we saw earlier.

---

## Technology Stack

- **Language & Framework**: Java 21 with Spring Boot
- **Architecture**: MVC (Model-View-Controller)
- **Communication Interfaces**:
  - REST (API First generation using OpenAPI/Swagger)
  - gRPC (using Netty powered by Java Protobuf)
- **Database**: MongoDB
- **Containerization**: Docker for MongoDB instance

---

## Project structure

📦 usermanagementtool
┣ 📂 docker
┃ ┣ 📜 docker-compose.yml        # Docker entry point to start the application
┃ ┗ 📜 mongo-init.js             # Database init with 1 User 
┣ 📂 src
┃ ┣ 📂 main
┃ ┃ ┣ 📂 java/com/faceit/usermanagementtool
┃ ┃ ┃ ┣ 📂 config                # Spring Boot beans
┃ ┃ ┃ ┣ 📂 controller            # REST controller
┃ ┃ ┃ ┣ 📂 exception             # Rest + GRPC exception handler
┃ ┃ ┃ ┣ 📂 grpc                  # GRPC environment
┃ ┃ ┃ ┃ ┣ 📂 controller          # GRPC interface
┃ ┃ ┃ ┃ ┗ 📂 server              # GRPC service
┃ ┃ ┃ ┣ 📂 mapper                # Project auto-generated mappers
┃ ┃ ┃ ┣ 📂 repository            # MongoDB operations
┃ ┃ ┃ ┣ 📂 service               # User Management Tool service
┃ ┃ ┃ ┗ 📂 util                  # Project util
┃ ┃ ┣ 📂 proto
┃ ┃ ┃ ┗ 📜 managementtool.proto  # gRPC definition
┃ ┃ ┣ 📂 resources
┃ ┃ ┃ ┣ 📜 application.yml       # Spring Boot properties
┃ ┃ ┃ ┗ 📜 openapi.yaml          # OpenApi definition
┃ ┃ ┗ 📂 test/com/faceit/usermanagementtool
┃ ┃ ┃ ┣ 📂 controller            # Integration tests
┃ ┃ ┃ ┣ 📂 service               # Unit tests
┃ ┃ ┃ ┗ 📂 grpc                  # Unit tests
┗ 📜 pom.xml                     # Project dependencies

---

## Technical Decisions & Assumptions

### Key Decisions

- **Java 21 + Records**: Modern Java features like `record` improve data immutability and simplify DTO creation.
- **Java 21 + MapStruct**: Modern Java features like `@Mapper` for automatic generation between different entities.
- **Spring Boot**: Used for rapid setup and dependency management, allowing focus on business logic.
- **API-First Design**: OpenAPI specs define REST endpoints, ensuring frontend/backend contract clarity.
- **MongoDB**: Chosen for its flexibility and schema-less structure, ideal for evolving user data models.
- **Dockerized MongoDB**: MongoDB runs in a Docker container to simplify local development and CI setup.
- **gRPC with Netty**: Enables high-performance, low-latency communication, especially suited for internal microservice calls.
- **gRPC with Protobuf**: Protobuf definitions are used for auto-generate gRPC entities (through .proto files).


### Assumptions

- We start from a parameterised interface to scale the application not only to users, but to any entity (e.g. Inventory, Traders, Product, Billing).
- At all times we take into account SOLID design principles to create a system with low coupling and high cohesion.
- We apply clean code principles, so that each line/functionality can be easily understood by an external person and the code is easy to read.
- The CRUD operations implemented in the tool are self-generated from an OpenAPI definition (in the case of the RESTFul service) or a .proto format (in the case of gRPC). Clients will validate against either OpenAPI or Protobuf definitions, depending on protocol.
- The available CRUD operations have a defined business logic:
  - Creation: A new entity is created, protecting the password by means of an encryption protocol and setting the creation and modification date with the date on which the service receives the request and processes it.
  - Read: Given a series of parameters, an AND search is performed with them.
  - Update: In our case, we allow the update by means of a PATCH, therefore all the required data of the resource we want to modify must be passed. This operation checks if the ID already exists, and if it does, it updates and modifies its modification date by means of business logic.
  - Delete: We delete an existing record in the database.
- Not all requests have a JSON response, depending on the operation the HTTPStatus itself that is returned when processing the request indicates the state in which the server ended up.
- We organise the responsibilities of each class according to the nature of each entity (a controller calls the service logic, this logic calls the data layer, etc).
- We use the java record feature of Java 21 to take advantage of immutability in objects that are passed between application layers (and save lines of code as well).
- MongoDB runs locally in development as a single-node container. Production will use a replica set or cluster.
- At the starting point of the application (remember that we ‘mix’ RESTFul and gRPC), the main() method, we must first load the Spring context, and since gRPC is not integrated with it, we must load the Spring context services that this (autogenerated) service implementation processes on the server.


---

## Communication Interfaces

### REST API

- **Exposed via**: Spring Web / OpenAPI (Swagger)
- **Purpose**: External and internal services that consume HTTP-based APIs
- **Features**:
  - Full CRUD for `User`
  - OpenAPI/Swagger UI for exploration and testing
  - Versioned endpoints for forward compatibility

### gRPC API

- **Exposed via**: Spring Boot with Netty + Protobuf
- **Purpose**: Efficient internal communication between microservices
- **Features**:
  - Same business logic as REST
  - High-performance, bi-directional communication
  - Defined via `.proto` files

---

## Potential Enhancements & Production Considerations

To prepare this service for production and broader adoption, the following improvements are recommended:

### Architecture

Given my experience implementing services, I find it a fast way to delivery APIs using MVC architecture. 
In our case, having 2 interface types using different frameworks underneath, it would be very interesting to isolate this technology from the business logic. 
This is known as hexagonal architecture and (although it may be more focused on larger projects where Domain-Driven Design can be applied with the help of business specialists, 
thus isolating the domain from other external layers) it could be very interesting for the future.

### Concurrence

For operations that involve multiple entities, thread concurrency might be beneficial. In our case, using Java 21, we could use Virtual Threads, as they are very efficient for input and output operations, achieve higher performance, and consume fewer OS resources.
Another alternative for implementing asynchronous tasks could be Reactive programming, using WebFlux, for example. It's a Spring framework, so integration is easy, although in this case I would opt for Virtual Threads to take advantage of the technology already offered by our JDK version 21.

### Scalability & Deployment

- **Microservice Deployment**: Define all the cloud resources which we need to deploy our application in external server.
  Having a cloud and an embedding of the project in some ecosystem would make for a greater focus on pure microservices architecture. Each functionality (database, implementation, interface, monitoring,...) could be defined as an atomic microservice, where communication between them would be done through REST APIs. 
  These APIs could be defined in the Kubernetes ecosystem by creating a Service with its corresponding Endpoint in order to expose the service we need.
  For example, using Openshift (Kubernetes), create the different properties in the server through ConfigMaps, define Service and endpoints to consume another microservices or third party libraries, Volumes to persistent data, Deployment to define how our application will be deployed in a Pod resource. 
- **Database Scaling**: Switch to MongoDB replica sets or sharded clusters.


### Observability

- **Centralized Logging**: Integrate with Kibana/Dynatrace.
- **Metrics**: Monitor gRPC and REST endpoints using Prometheus + Grafana.
- **Distributed Tracing**: Add Spring Sleuth for tracing across services.

### DevOps & CI/CD

- **Automated Pipelines**: Only localhost run (based on my experience, we could use Jenkins). This pipeline must be integrated with Sonar, which gives us in every deploy our code coverage (and quality).
- **Infrastructure as Code**: Use Ansible to define, manage and automate environments/deployments.
- **Zero-Downtime Deployments**: Adopt blue/green or canary deployments with rollback support (very interesting to deploy in PRO, where you can create a private service URL parallel to the raised one before finishing the deployment).

### Security

- **Authentication**: No required
- **Validation**: Crypt funcionality on the User's password before insert in the database
- **Secret Management**: Use tools like Vault, Kubernetes or CyberArk secrets to manage credentials securely.

### Testing and code coverage

We previously mentioned the use of Sonar for code coverage (integrated, for example, into Jenkins). This version of the project does NOT include tests for all of our classes (perhaps due to development time). Looking ahead, the ideal would be to have one XXX_Test class for each class in the project code. In this version, as we mentioned, we have an example of how the REST API integration tests would be performed and how we would perform the unit tests for the main REST API service.
Alternatively, we could use Karate to create a suite of automated tests, which can be integrated into the Jenkinsfile we use to deploy our app to a given environment. Having a specific failure rate in these tests would be a blocking factor (and thus avoid pushing bugs).

---

## Contributing

Contributions are welcome! Here the GitHub repo https://github.com/RVCK/management-tool

---