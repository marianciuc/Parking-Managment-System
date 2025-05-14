# Parking Management System - Engineering Team Project

## Team Members

- Vladimir Marianciuc (vladimir.marianciuc.work@gmail.com) - Backend Developer, Team Lead 
- Vitalii Natalevych - Frontend Developer
- Vladyslav Hurko - OKR Implementation Specialist
- Davyd Hrosh - Mobile Developer

## Project Overview

The Parking Management System is a comprehensive solution designed to streamline parking operations and enhance user
experience. It consists of multiple integrated components that serve different stakeholders in the parking ecosystem.

## Key Features

- **Parking Owner Portal**: Complete management of parking facilities, including capacity control, tariff setting, and
  revenue tracking
- **Mobile Application for Drivers**: Convenient parking discovery, payment, and account management
- **Web Payment Option**: Alternative payment method for non-app users
- **Administrative Dashboard**: Central system management and moderation tools
- **Parking Gate Controller**: Automated vehicle recognition and gate operation

## Target Audience

The system serves three primary user groups:

1. **Parking Owners/Operators**: Businesses or individuals who manage parking facilities
2. **Drivers/Parking Users**: Individuals seeking parking spaces and looking to manage their parking sessions
3. **System Administrators**: Personnel responsible for overseeing the entire system and supporting other users

Each component of the system is tailored to meet the specific needs of its target audience while maintaining seamless
integration with the other parts of the ecosystem.

## Technology Stack

### Backend

| Technology                           | Short Description                                                                                                                                     |
|--------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| Java                                 | A robust and widely-used object-oriented programming language known for its platform independence and extensive ecosystem.                            |
| Spring                               | A comprehensive application development framework for Java, providing infrastructure support for building enterprise-level applications.              |
| PostgreSQL                           | A powerful, open-source relational database system known for its reliability, feature richness, and extensibility.                                    |
| Kafka                                | A distributed streaming platform designed for building real-time data pipelines and streaming applications.                                           |
| Microservice Architecture            | An architectural style that structures an application as a collection of small, independent services, each addressing a specific business capability. |
| Kubernetes                           | A container orchestration platform for automating deployment, scaling, and management of containerized applications.                                  |
| Docker                               | A platform for developing, shipping, and running applications in isolated environments called containers.                                             |
| Spring Cloud                         | A framework built on top of Spring that provides tools for building distributed systems and microservices in the cloud.                               |
| Spring Security                      | A powerful and customizable authentication and authorization framework for Java-based applications.                                                   |
| Eureka                               | A service discovery server and client for locating services within a microservices architecture.                                                      |
| Config Service (Spring Cloud Config) | A centralized configuration management service for distributed applications, allowing externalization of application configurations.                  |
| Gateway (Spring Cloud Gateway)       | An intelligent and programmable router that acts as a single entry point for client requests in a microservices architecture.                         |
| JWT (JSON Web Tokens)                | A compact and self-contained way for securely transmitting information between parties as a JSON object.                                              |
| Zipkin                               | A distributed tracing system that helps in monitoring and troubleshooting latency issues in microservices environments.                               |

### Frontend

| Technology  | Short Description                                                                                                                                                                                                   |
|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| React       | A popular JavaScript library for building user interfaces or UI components.                                                                                                                                         |
| Typescript  | A superset of JavaScript that adds static typing to the language, improving code maintainability and reducing errors.                                                                                               |
| Vite        | A next-generation frontend tooling that provides an extremely fast development experience and bundles code efficiently.                                                                                             |
| TailwindCSS | A utility-first CSS framework that allows for rapid styling directly in HTML using pre-defined classes.                                                                                                             |
| shadcn/ui   | A collection of reusable UI components built using Radix UI and Tailwind CSS, focusing on accessibility and customization.                                                                                          |
| Axios HTTP  | A promise-based HTTP client for browsers and Node.js, making it easy to perform API requests.                                                                                                                       |
| Lucide      | A collection of beautiful and consistent open-source icons.                                                                                                                                                         |
| MobX        | A simple and scalable state management library that makes it easy to connect the data of your application with the UI.                                                                                              |
| Zod         | A TypeScript-first schema declaration and validation library that allows you to define and verify data structures.                                                                                                  |
| Next.js     | A popular React framework for building full-stack web applications with features like server-side rendering and routing.                                                                                            |
| HTML        | HyperText Markup Language, the standard markup language for creating web pages and web applications. It provides the structure and content of a webpage.                                                            |
| CSS         | Cascading Style Sheets, a stylesheet language used to describe the presentation of a document written in a markup language like HTML. It controls the layout, colors, fonts, and other visual aspects of a website. |

### Mobile Application

| Technology   | Short Description                                                                                                                                                                                             |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| React Native | A framework for building native mobile applications using JavaScript and React. It allows developers to write code once and deploy it on both iOS and Android.                                                |
| Typescript   | A superset of JavaScript that adds static typing to the language, improving code maintainability and reducing errors, and is often used with React Native for mobile development.                             |
| Expo         | A framework and platform built on top of React Native that simplifies the development, building, deployment, and updating of native iOS and Android applications.                                             |
| Kotlin       | A modern, statically-typed programming language for the JVM, Android, and the browser, known for its conciseness and interoperability with Java. It is a primary language for Android development.            |
| Swift        | A powerful and intuitive programming language developed by Apple for building apps across all Apple platforms (iOS, macOS, watchOS, tvOS, and beyond). It is the primary language for native iOS development. |
| Axios        | A promise-based HTTP client for browsers and Node.js, commonly used in mobile development with frameworks like React Native to make API requests.                                                             |

### Parking Gate Controller

| Technology                | Short Description                                                                                                                                                                                   |
|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Python                    | A versatile and widely-used high-level programming language known for its readability and extensive libraries, making it popular in areas like data science, web development, and machine learning. |
| OpenCV                    | An open-source computer vision library providing a wide range of algorithms for image and video processing, analysis, and computer vision tasks.                                                    |
| PaddleOCR                 | An open-source Optical Character Recognition (OCR) system developed by Baidu, supporting multiple languages and providing tools for text detection and recognition in images.                       |
| YOLO (You Only Look Once) | A real-time object detection system known for its speed and efficiency in identifying and localizing multiple objects within an image or video frame.                                               |
| FastAPI                   | A modern, high-performance web framework for building APIs with Python, based on standard Python type hints, which makes it easy to use and highly efficient.                                       |

### Integrations

| Technology      | Short Description                                                                                                                                                                                                                             |
|-----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Stripe          | A technology company that builds economic infrastructure for the internet. It provides APIs for payments, billing, subscriptions, and more.                                                                                                   |
| Google Maps API | A suite of APIs and SDKs that allow developers to embed Google Maps into web and mobile applications, adding features like maps, places, and routing.                                                                                         |
| Exchange API    | Generally refers to an Application Programming Interface that allows programmatic access to a trading exchange for fetching market data, placing orders, and managing accounts (this could be for financial instruments or cryptocurrencies). |
| Mapbox          | A location data platform that provides tools and APIs for creating custom maps, navigation, and location-based services for web and mobile applications.                                                                                      |

## Requirements Specification

### General Requirements

- **Platform Compatibility**:
    - Parking Owner Dashboard and Admin Panel: Web-based applications accessible through standard browsers
    - Driver Application: Cross-platform mobile application compatible with both Android and iOS devices
    - Backend Infrastructure: Kubernetes-hosted Java microservices architecture
    - Parking Gate Controller: Embedded software compatible with microcomputers (e.g., Raspberry Pi)

- **System Integration**:
    - All components must communicate seamlessly through defined APIs
    - Real-time data synchronization across platform components
    - Consistent user experience across all interfaces

### Functional Requirements

#### Parking Owner Dashboard

- **Parking management functionality**:
    - Add and configure new parking facilities
    - Update parking capacity, operating hours, and categorization tags
    - Configure and adjust tariff structures and pricing
    - See parking sessions and details about this sessions

- **Financial management capabilities**:
    - View comprehensive session statistics and analytics
    - Monitor revenue streams from parking operations
    - Request and track payouts to designated bank accounts
    - Access transaction history and financial reports

- **Facility operations**:
    - Manage parking gate controls and access points
    - Monitor customer reviews and feedback

#### Driver Mobile Application

- **Parking discovery and management**:
    - Interactive map-based parking lot search functionality
    - Vehicle profile management for multiple vehicles
    - Session initiation, monitoring, and payment processing

- **Account management**:
    - Balance management with top-up capability
    - Historical session tracking and reporting
    - User profile configuration

- **Alternative web-based payment option for non-app users**

#### Administrative Dashboard

- **System maintenance capabilities**:
    - Generate and manage API keys for parking facilities
    - Review and moderate parking facility information
    - Tag management for parking categorization
    - User account oversight and moderation
    - Review management and moderation

#### Parking Gate Controller

- Vehicle identification through license plate recognition
- Automated gate operation (opening/closing) based on session validity
- Communication with central system for access validation

### Non-functional Requirements

- **Performance**:
    - Mobile application response time under 2 seconds for all operations
    - Web interfaces to load within 3 seconds under normal network conditions
    - Gate controller to process vehicle identification within 5 seconds
    - System capable of handling concurrent users across all parking facilities

- **Security**:
    - Secure user authentication and authorization
    - Encrypted data transmission between all components
    - Payment information security compliant with industry standards
    - Protection against common web vulnerabilities
    - Secure API authentication mechanisms

- **Reliability**:
    - 99.9% uptime for all critical system components
    - Graceful degradation during partial system failures
    - Data backup and recovery mechanisms
    - Fault tolerance for gate controllers to prevent vehicle lockout

- **Scalability**:
    - Architecture capable of accommodating growing number of parking facilities
    - Support for increasing user base without performance degradation
    - Dynamic resource allocation in cloud infrastructure

- **Usability**:
    - Intuitive interfaces requiring minimal training
    - Responsive design for various screen sizes and devices
    - Accessibility compliance for web components
    - Clear error messaging and recovery paths
