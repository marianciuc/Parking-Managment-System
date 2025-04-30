# Car Management Microservice

The Car Management microservice handles all vehicle-related operations including registration, ownership management, and
vehicle data updates. It's designed to work in conjunction with other services in the parking application architecture.

### Technology Stack

- **Java 21**
- **Spring Boot 3.2.4**
- **Spring Cloud** (Eureka, Config Server)
- **PostgreSQL** database
- **Flyway** for database migrations
- **Kafka** for event messaging
- **Zipkin** for distributed tracing

## API Reference

### Vehicle Endpoints

| Endpoint                         | Method | Description                                                                           |
|----------------------------------|--------|---------------------------------------------------------------------------------------|
| `/api/v1/vehicles/search`        | GET    | Search vehicles with filters (page, size, id, plateNumber, brand, model, color, etc.) |
| `/api/v1/vehicles/find-by-plate` | GET    | Find vehicle by plate number                                                          |
| `/api/v1/vehicles/{vehicleId}`   | GET    | Get vehicle by ID                                                                     |
| `/api/v1/vehicles/{vehicleId}`   | PUT    | Update vehicle information                                                            |
| `/api/v1/vehicles`               | POST   | Create new vehicle (param: assignOwnership)                                           |

### Ownership Endpoints

| Endpoint                                                    | Method | Description                                              |
|-------------------------------------------------------------|--------|----------------------------------------------------------|
| `/api/v1/vehicles/{vehicleId}/ownership/transfer/request`   | POST   | Request ownership transfer                               |
| `/api/v1/vehicles/{vehicleId}/ownership/transfer`           | POST   | Admin transfer ownership (params: newOwnerId, ownerType) |
| `/api/v1/vehicles/{vehicleId}/ownership/{requestId}/accept` | PUT    | Accept ownership request                                 |
| `/api/v1/vehicles/{vehicleId}/ownership/{requestId}/reject` | PUT    | Reject ownership request                                 |

## Testing

``` bash
mvn test
```

## Data Models

- **Vehicle**: Stores vehicle information including plate number, brand, model
- **Ownership**: Manages vehicle-owner relationships
- **OwnershipChangeRequest**: Tracks ownership transfer requests

## Deployment
The service is containerized with Docker and configured to work within the microservices ecosystem.
``` 
docker build -t vehicle-service .
docker run -p 8080:8080 vehicle-service
```
## Service Dependencies
This microservice is part of a larger ecosystem and depends on:
- Config Server for configuration
- Discovery Server for service discovery
- Security MS for authentication
