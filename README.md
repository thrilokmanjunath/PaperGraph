# PaperMate AI - Research Intelligence Platform

PaperMate is an enterprise-grade AI-powered Research Intelligence Platform designed to help researchers, students, and organizations discover, manage, and visualize academic papers through intelligent citation graphs.

## Architecture

This project uses a modern, cloud-native architecture:

- **Frontend**: Next.js 15 (React), Tailwind CSS, Lucide Icons.
- **Backend**: Java 21, Spring Boot 3, Spring Data JPA, Spring Security.
- **Database**: PostgreSQL (Relational Data) & Redis (Caching).
- **Deployment**: Docker Compose ready.

## Features

- **Interactive Knowledge Graph**: Visualize citation networks and discover foundational papers.
- **AI Research Assistant**: Automatically generate literature reviews and paper summaries (via API integrations).
- **Modern Dashboard**: A highly polished, responsive, dark-mode first interface.
- **REST APIs**: Full CRUD operations and graph traversal endpoints exposed via Spring Boot.

## Getting Started

### Prerequisites
- Java 21
- Node.js 18+
- Docker & Docker Compose

### 1. Start the Databases
Navigate to the root directory and start the PostgreSQL and Redis containers:
```bash
docker-compose up -d
```

### 2. Start the Backend
Navigate to the `backend` directory and run the Spring Boot application:
```bash
cd backend
./mvnw spring-boot:run
```
*The backend will be available at `http://localhost:8080`. Swagger documentation is available at `http://localhost:8080/swagger-ui.html`.*

### 3. Start the Frontend
Navigate to the `frontend` directory, install dependencies, and start the development server:
```bash
cd frontend
npm install
npm run dev
```
*The frontend will be available at `http://localhost:3000`.*

## Deployment

The application is structured for easy deployment to modern cloud providers:
- **Frontend**: Connect the `frontend` directory to **Vercel** for automatic deployments.
- **Backend**: Connect the `backend` directory to **Render**, **Railway**, or **Google Cloud Run**.
- **Database**: Use a managed PostgreSQL provider like **Neon** or **Supabase**.

## License
MIT
