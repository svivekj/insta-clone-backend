# 📸 Instagram Clone - Spring Boot Backend

A robust, modular REST API built with Spring Boot 3 for an Instagram clone. It powers user management, paginated home feeds, post creation, like/unlike toggling, and comment threads backed by a PostgreSQL database.

---

## 🚀 Tech Stack & Requirements

* **Java 17+**
* **Spring Boot 3** (Web, Data JPA, Validation)
* **PostgreSQL 16** (Relational Database)
* **Docker & Docker Compose** (Containerized Database)
* **Maven** (Build & Dependency Management)

---

## 🌐 Infrastructure & Ports

* **Spring Boot Server Port:** `9090` (`http://localhost:9090`)
* **PostgreSQL Database Port:** `5432` (`localhost:5432`)
* **Base API Endpoint Prefix:** `/api/posts`
* **CORS Origin Allowed:** `http://localhost:5173` (React Frontend)

---

## 🛠️ Step-by-Step Setup & Running Instructions

### Step 1: Start PostgreSQL via Docker Compose

Create a `docker-compose.yml` file in the root directory of your backend project with the following configuration:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16-alpine
    container_name: insta_postgres
    environment:
      POSTGRES_DB: insta1db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

Spin up the PostgreSQL container in the background by running:

```bash
docker-compose up -d
```

Verify that the container is running successfully:

```bash
docker ps
```

### Step 2: Configure Application Properties

Ensure your `src/main/resources/application.yml` is configured to connect to the Docker PostgreSQL container:

```yaml
server:
  port: 9090

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/insta1db
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  jackson:
    serialization:
      write-dates-as-timestamps: false
```

### Step 3: Run the Spring Boot Application

Build and launch the backend server using the Maven wrapper:

```bash
# On macOS / Linux
./mvnw spring-boot:run

# On Windows (Command Prompt or PowerShell)
mvnw.cmd spring-boot:run
```

The backend will start and listen on `http://localhost:9090`.

---

## 🔌 Complete REST API Endpoints Reference

All endpoints are hosted relative to `http://localhost:9090/api/posts`.

| Method | Endpoint | Description | Parameters / JSON Body |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/posts/feed` | Fetches paginated home feed posts for current user and followed users | **Query:** `currentUserId` (Long), `page` (int, default 0), `size` (int, default 10) |
| **POST** | `/api/posts` | Creates a new image post | **Body:** `{ "imageUrl": "...", "caption": "...", "authorId": 1 }` |
| **GET** | `/api/posts/{postId}` | Retrieves a single post by its ID | **Path:** `postId` — **Query:** `currentUserId` (optional) |
| **GET** | `/api/posts/user/{authorId}` | Retrieves all posts authored by a specific user | **Path:** `authorId` — **Query:** `currentUserId` (optional) |
| **POST** | `/api/posts/{postId}/like` | Toggles like/unlike status for a user on a post | **Path:** `postId` — **Query:** `userId` (Long) |
| **GET** | `/api/posts/{postId}/comments` | Retrieves all comments associated with a specific post | **Path:** `postId` |
| **POST** | `/api/posts/{postId}/comments` | Adds a new comment to a post | **Path:** `postId` — **Body:** `{ "text": "...", "userId": 1 }` |