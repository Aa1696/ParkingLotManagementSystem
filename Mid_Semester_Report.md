# MID-SEMESTER PROGRESS EVALUATION SHEET

**ID No.** : (Insert Student ID)  
**NAME OF THE STUDENT** : (Insert Student Name)  
**EMAIL ADDRESS** : (Insert Email)  
**SUPERVISOR’S NAME** : (Insert Supervisor Name)  
**DISSERTATION TITLE** : Smart Parking Lot Management System: A Distributed Microservices Approach using Spring Boot, Kafka, and MySQL  
**Details of work done till date** : Report to be submitted.  
**Plan for work yet to be done** : Attach document, specifying the milestones & deliverables.

*(Note: Evaluation and Certificate pages omitted for brevity in markdown format)*

---

# Title Page
**Smart Parking Lot Management System: A Distributed Microservices Approach using Spring Boot, Kafka, and MySQL**

S1-25_SEMTZG628T DISSERTATION  
By: (Insert Student Name)  
Project Work carried out at: (Insert Location)  
**BIRLA INSTITUTE OF TECHNOLOGY AND SCIENCE**  
Pilani (Rajasthan) India

---

# ACKNOWLEDGEMENT
I would like to express my heartfelt gratitude to my Internal Guide for their invaluable guidance, constant encouragement, and insightful suggestions throughout the course of this project. I also extend my sincere thanks to my supervisor who has been a source of inspiration and provided timely advice during the development of this project.

---

# 1. Problem Statement
Traditional parking lot management systems often rely on manual ticketing and standalone, monolithic software architectures that cannot scale during peak hours. This leads to bottlenecks at entry/exit gates, lack of real-time capacity tracking, and unreliable payment processing. A highly scalable, distributed system is needed to automatically allocate slots, process payments dynamically, and handle high loads efficiently using microservices.

# 2. Objective
The objective of this project is to design and implement an enterprise-grade Smart Parking Lot Management System. The system utilizes a microservices architecture to provide real-time tracking of parking capacity, automated ticket generation, dynamic fee calculation, and asynchronous notifications. It aims to eliminate gate bottlenecks and provide a seamless parking experience.

# 3. Background
With the rapid increase in urban vehicles, centralized monolithic parking systems suffer from single points of failure and scalability issues. Modern cloud-native technologies such as Spring Boot for microservices, Apache Kafka for event-driven asynchronous messaging, and Service Discovery (Eureka) provide an opportunity to build robust, highly available systems that can independently scale specific components like payment processing without affecting the rest of the system.

# 4. Proposed Work
The proposed system will:
1. Provide an API Gateway for centralized routing and JWT authentication.
2. Manage parking entries, exits, and real-time capacity via a Parking Service.
3. Calculate dynamic pricing and process transactions via a Payment Service.
4. Asynchronously send digital receipts and notifications via a Notification Service connected to Kafka.

# System Workflow Diagram
Client Interface (Web App) → API Gateway (Routing/Auth) → Parking/Payment Services (Logic) → MySQL Database (Persistence) → Kafka Broker (Events) → Notification Service (Emails).

# 5. Hardware and Software Requirements
**Hardware Requirements**
- Processor: Intel i5 or higher
- RAM: 8 GB minimum
- Storage: 256 GB SSD minimum

**Software Requirements**
- Frontend: HTML, CSS, JavaScript (ReactJS/Vanilla)
- Backend: Java 17, Spring Boot, Spring Cloud (Gateway, Eureka)
- Database: MySQL (Azure Flexible Server)
- Message Broker: Apache Kafka (Azure Event Hubs)
- Build & Containerization: Maven, Docker, Docker Compose

# 6. Timeline
| Phase | Task | Duration |
|---|---|---|
| Phase 1 | Dissertation Outline | Week 1 |
| Phase 2 | System Design & Architecture | Week 2 – Week 4 |
| Phase 3 | Design & Development | Week 5 – Week 9 |

# 7. Work Progress and Implementation
The project has been organized into phases to ensure a structured development process. So far, the foundational tasks have been completed, including requirement gathering, system architecture design, and backend development of the core microservices.

---

# Phase 1: Dissertation Outline

**1. Objective**
The primary aim of Phase 1 is to establish a solid foundation for the system by clearly understanding the requirements of smart parking facilities and analyzing enterprise application design patterns. 

**2. Work Done**
- **Literature Review:** Conducted a comprehensive review of existing smart parking systems and microservices design patterns. Identified the limitations of monolithic architectures in high-traffic parking scenarios.
- **Project Proposal:** Prepared the project abstract and proposal highlighting the use of Spring Boot, Kafka, and MySQL.
- **Requirement Analysis:** Defined functional requirements (ticketing, billing, notifications) and non-functional requirements (high availability, sub-second response times).

---

# Phase 2: System Design & Architecture

**1. Objective**
To design a robust, scalable layered architecture that supports distributed microservices and asynchronous communication.

**2. Work Done**
- **System Architecture Design:** Designed the microservices layout, including the API Gateway, Eureka Naming Server, Parking Service, Payment Service, and Notification Service. 
- **Database Schema:** Designed the Entity-Relationship (ER) diagram for the MySQL database. Created normalized tables for `Tickets`, `Vehicles`, `ParkingSpots`, and `Floors`.
- **Module Identification:** Identified and documented the core modules required: Admin operations, User interactions, Parking Management logic, Payment calculations, and Event-driven Analytics.

---

# Phase 3: Design & Development

**1. Objective**
To implement the backend logic and REST APIs using Spring Boot, integrating them with the MySQL database and Kafka broker.

**2. Work Done**
- **Spring Boot Backend:** Initialized the microservices using Spring Initializr and configured `application.yml` for database and Kafka connectivity.
- **REST APIs:** Developed API endpoints for parking slot allocation (entry operations), capacity checking, ticket generation, and exit billing operations.
- **Database Integration:** Implemented JPA/Hibernate entities to map Java objects (e.g., `Ticket`, `Vehicle`) to MySQL database tables, ensuring ACID compliance for transactions.
- **Event-Driven Messaging:** Configured Kafka producers in the Payment Service to publish `PaymentCompletedEvent` messages, which are consumed by the Notification Service for asynchronous processing.
