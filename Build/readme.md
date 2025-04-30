# Build

The Intelligent Parking System is an enterprise-grade solution comprising 12 Java Spring microservices designed to provide a comprehensive and scalable parking management platform.
## Architecture Components
### Client Applications
- **Mobile Application**: Built with React Native and Expo for cross-platform compatibility
- **Administration Portal**: Accessible at `http://localhost:5175`
- **Parking Management Portal**: Accessible at `http://localhost:5500`

### Infrastructure
- **Containerization**: All services are containerized and published to Docker Hub
- **Deployment**: Simplified deployment through Docker Compose configuration

## Technical Requirements
### Prerequisites
- Docker Engine (latest stable version recommended)
- Docker Compose
- Minimum system specifications:
    - 12GB RAM
    - 20GB available disk space

## Configuration & Deployment
### Environment Configuration
The system requires the following environment variables:
``` env
POSTGRES_USER=
POSTGRES_PASSWORD=
REDIS_PASSWORD=
ALLOWED_ORIGINS=http://localhost:5500,http://localhost:5175,https://localhost:5173,https://marianciuc.works,http://localhost:3000
GATEWAY_HOST=http://localhost:8222
SPRING_JWT_PRIVATE_KEY={"p":"1ZohVplIXHLokrHTX_9bgklOJw41aSqd5r...
SPRING_JWT_PUBLIC_KEY={"kty":"RSA","e":"AQAB","kid":"Outl4hhj-Ne-2J...
STRIPE_WEBHOOK=
STRIPE_SECRET_KEY=
STRIPE_PUBLIC_KEY=
EXCHANGE_APP_ID=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```
### Deployment Instructions
To deploy the entire system:
``` bash
docker compose up -d
```
This command initializes all services in detached mode, allowing them to run in the background.
## Accessing the System
Once successfully deployed, the system portals can be accessed at:
- **Administration Portal**: [http://localhost:5050](http://localhost:5050)
- **Parking Management Portal**: [http://localhost:5300](http://localhost:5300)

API Gateway services are available at `http://localhost:8222`.
## Security Considerations
The system implements JWT-based authentication with separate access and refresh tokens. API requests are automatically configured with the appropriate Authorization headers through the client API interceptor.
