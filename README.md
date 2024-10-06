# Ticket Management API

## Description
RESTful API for a ticket management system built with Spring Boot. It allows users to create, read, update, and delete tickets, as well as manage users and assign tickets to them.

## Features
- CRUD operations for tickets
- User management
- Ticket assignment

## Technologies
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2
- Swagger UI
- MapStruct
- JUnit, Mockito & AsserJ
- Maven

## Getting Started

### Prerequisites
- JDK 21 or later
- Maven 3.6+

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/maxwell-balla/ennov.io-test-java.git
   ```
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8282`

## API Endpoints

| Method | Endpoint                          | Description |
|--------|-----------------------------------|-------------|
| GET    | /api/v1/tickets                   | Get all tickets |
| GET    | /api/v1/tickets/{id}              | Get a specific ticket |
| POST   | /api/v1/tickets                   | Create a new ticket |
| PUT    | /api/v1/tickets/{id}              | Update a ticket |
| DELETE | /api/v1/tickets/{id}              | Delete a ticket |

For a full list of endpoints and their usage, please refer to the API documentation at http://localhost:8282/api/v1/swagger-ui/index.html
## Testing
To run the tests, execute:
```
mvn test
```

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests.

## LinkedIn
Maxwell Balla : https://www.linkedin.com/in/maxwell-balla/

## Contact
Maxwell - ballamaxwell7@gmail.com

Project Link: https://github.com/maxwell-balla/ennov.io-test-java
