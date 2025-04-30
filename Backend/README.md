# Setup Backend Manually

This guide explains how to set up the microservices architecture for the Parking Application on your local development
environment. The application consists of multiple interconnected services that collectively provide a comprehensive
parking management solution.

## Repository Configuration

To use our core library hosted in Azure Artifacts, you need to configure your Maven settings. The settings file is
located at `~/.m2/settings.xml` (Unix/macOS) or `C:\Users\<username>\.m2\settings.xml` (Windows).
Create or modify this file with the following content:

```xml

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>

        <server>
            <id>security-lib</id>
            <username>fabiansprycha</username>
            <password>${TOKEN}</password>
        </server>

    </servers>
</settings>
```

> **Important**: Contact us at vladimir.marianciuc.work@gmail.com to receive proper authentication credentials for accessing our repositories. Never share or commit these credentials to version control systems.

## **Starting the Application**

### **Step 1: Start External Services**

To start supporting services (PostgreSQL, Kafka, Zookeeper, Redis, etc.), execute the following command:

```Bash
docker compose -f ./docker-compose-manual.yml up -d 
```

### **Step 2: Configure Environment Variables**

Use the following environment variables to configure the application. Replace placeholder values (`<value>`) with your
actual settings.
**Example Environment Variables**:

``` yaml
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRIG_GATEWAY_HOST: http://localhost:8222
SPRING_OAUTH2_GOOGLE_CLIENT_ID: <Google_Client_ID>
GOOGLE_CLIENT_SECRET: <Google_Client_Secret>
SPRING_JWT_PRIVATE_KEY: |
  { "p": "private-key-part", "kty": "RSA", "n": "public-key-part" }
SPRING_JWT_PUBLIC_KEY: |
  { "kty": "RSA", "e": "AQAB", "n": "public-key-part" }
```

**How to set environment variables**:

- **Unix/macOS**: Add them to your shell environment:

``` bash
export SPRING_DB_USERNAME=root
export SPRING_DB_PASSWORD=root
# Add other variables here
```

- **Windows** (Command Prompt):

``` bash
  set SPRING_DB_USERNAME=root
  set SPRING_DB_PASSWORD=root
```

Alternatively, create a `.env` file in the project root with the above variables.

### Service Dependencies

The services must be started in the correct order due to their dependencies:

1. **Config Server** - Central configuration repository
2. **Discovery Service** - Service registry for microservices communication
3. **Security Service** - Authentication and authorization
4. **Gateway** - API Gateway for routing requests
5. All other services - Can be started in any order after the core services

### Detailed Setup Instructions

### Config Service

This is the backbone of the microservices architecture, providing centralized configuration for all services.
**Prerequisites**: Ensure port `8888` is available.

```Bash
cd Config_Server
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

**Verification**: Navigate to `http://localhost:8888/actuator/health` to verify the service is running correctly.

### Discovery Service

Eureka service registry that allows microservices to discover and communicate with each other.

``` bash
cd Discovery_Service
mvn install
cd target 
java -jar discovery-service-0.0.1-SNAPSHOT.jar
```

**Verification**: Access the Eureka dashboard at `http://localhost:8761` to see registered services.

### Security Service

Handles authentication and authorization for the entire application.

``` bash
cd Security_Service
mvn install
cd target 
java -jar security-service-0.0.1-SNAPSHOT.jar
```

**Environment Variables**:

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: auth-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRIG_GATEWAY_HOST: http://localhost:8222
SPRING_OAUTH2_GOOGLE_CLIENT_ID: <Google_Client_ID>
GOOGLE_CLIENT_SECRET: <Google_Client_Secret>
SPRING_JWT_PRIVATE_KEY: |
  { "p": "private-key-part", ... }
SPRING_JWT_PUBLIC_KEY: |
  { "kty": "RSA", "e": "AQAB", "n": "public-key-part" }
```

### **Generating RSA Keys for JWT**

To generate the private/public key pair for JWT authentication:

1. Visit a trusted online JWK generator like:
    - [MkJose](https://mkjwk.org/)
    - [Connect2id](https://connect2id.com/products/nimbus-jose-jwt/generator)

2. Use the following settings:
    - **Key Type**: RSA
    - **Key Size**: 2048 bits or higher
    - **Key Use**: Signature
    - **Algorithm**: RS256
    - **Key ID**: Generate a random key ID (optional).

3. Generate the key pair.
4. Copy the private and public keys in **JWK** format to the respective environment variables.

### Gateway

API Gateway that routes client requests to appropriate microservices.

``` bash
cd Gateway
mvn install
cd target 
java -jar gateway-0.0.1-SNAPSHOT.jar
```

**Environment Variables**:

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: gateway-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
ALLOWED_ORIGINS: http://localhost:5500,http://localhost:5173,https://localhost:5173,https://marianciuc.works
```

**Verification**: Check `http://localhost:8222/actuator/routes` to see available routes.

### Administration Service

```Bash
cd Administration_Service
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

**Environment Variables**:

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: admin-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
```

### Notification Service

Handles notifications via email, SMS, and push notifications.

```Bash
cd Notification_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_PORT: 8022
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: notification-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
MAIL_HOST: localhost # Connected to mailhog by default
MAIL_PORT: 1025 # Connected to mailhog by default
USE_AUTH: false # Mailhog is not require any authentication
ENABLE_SSL: false
```

### Parking Owner Service

Manages parking lot owner accounts and their properties.

```Bash
cd Parking_Owner_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: owners-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
```

### Parking Service

Core service for parking lot management and operations.

```Bash
cd Parking_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: parking-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
```

### Payment Service

Processes payment transactions for parking sessions.

```Bash
cd Payments_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: payment-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
STRIPE_SECRET_KEY: sk_...
EXCHANGE_APP_ID: app_id.. # openexchangerates.org app id
STRIPE_PUBLIC_KEY: pk_..
STRIPE_WEBHOOK: webhook
SPRING_PORT: 8064
```

### Reviews Service

Handles user reviews and ratings for parking facilities.

```Bash
cd Reviews_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: reviews-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRING_PORT: 8064
```

### Session Manager Service

Tracks active parking sessions and vehicle entry/exit.

```Bash
cd Session_Manager_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: session-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRING_PORT: 8064
```

### Tariffs Manager Service

Manages pricing strategies and tariff calculations.

```Bash
cd Tariffs_Manager_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: tariffs-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRING_PORT: 8064
```

### Car Manager Service

The Car Management microservice handles all vehicle-related operations including registration, ownership management, and
vehicle data updates. It's designed to work in conjunction with other services in the parking application architecture.

```Bash
cd Car_Manager_MS
maven install
cd target 
java -jar config-server-0.0.1-SNAPSHOT.jar
```

```yaml
SPRING_REDIS_HOST: localhost
SPRING_APPLICATION_NAME: vehicle-service
SPRING_REDIS_PORT: 6379
ZIPKIN_URL: http://localhost
ZIPKIN_PORT: 9411
SPRING_EUREKA_SERVICE_HOST: localhost
SPRING_EUREKA_SERVICE_PORT: 8761
SPRING_DB_USERNAME: root
SPRING_DB_PASSWORD: root
SPRING_KAFKA_HOST: localhost
SPRING_KAFKA_PORT: 9082
SPRING_DB_HOST: localhost
SPRING_DB_PORT: 6432  # Connected to PgBouncer
SPRING_PORT: 8064
```