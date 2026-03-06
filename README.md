# CV Platform — Backend

Personal CV web platform with role-based access, LLM chat assistant, and admin panel.
Built with Spring Boot, PostgreSQL, and JWT authentication.

## Tech Stack

- **Java 17** + Spring Boot 3.4
- **Spring Security** — JWT authentication with HttpOnly cookies
- **PostgreSQL** — JPA/Hibernate with Spring Data
- **Groq API** — LLM chat assistant (Llama 3)
- **JavaMail** — contact form via Gmail SMTP
- **Testcontainers** — integration tests with real PostgreSQL
- **Docker** — containerized deployment
- **GitHub Actions** — CI pipeline

## Architecture
```
controller/   REST endpoints
service/      Business logic
repository/   Spring Data JPA
security/     JWT filter + UserDetailsService
dto/          Request/Response objects
model/        JPA entities
config/       Security, CORS, Swagger
exception/    Global error handling
```

## Roles

| Role | Access |
|------|--------|
| `ROLE_ADMIN` | Full CRUD on all CV content |
| `ROLE_VIEWER` | Read-only access (for recruiters) |

## API Endpoints

| Method | Endpoint | Auth |
|--------|----------|------|
| POST | `/api/auth/login` | Public |
| POST | `/api/auth/logout` | Authenticated |
| GET | `/api/profile` | VIEWER + ADMIN |
| PUT | `/api/profile` | ADMIN |
| GET/POST/PUT/DELETE | `/api/experiences` | VIEWER / ADMIN |
| GET/POST/PUT/DELETE | `/api/projects` | VIEWER / ADMIN |
| GET/POST/PUT/DELETE | `/api/skills` | VIEWER / ADMIN |
| GET/POST/PUT/DELETE | `/api/education` | VIEWER / ADMIN |
| GET/POST/PUT/DELETE | `/api/languages` | VIEWER / ADMIN |
| GET | `/api/cv/download` | VIEWER + ADMIN |
| POST | `/api/chat` | VIEWER + ADMIN |
| POST | `/api/contact` | VIEWER + ADMIN |

## Local Setup

**Prerequisites:** Java 17, PostgreSQL, Maven
```bash
git clone https://github.com/NuriaOlivares/cv-backend
cd cv-backend
```

Create `.env`:
```
DB_PASSWORD=your_postgres_password
ADMIN_PASSWORD=your_admin_password
JWT_SECRET=your_secret_min_32_chars
GROQ_API_KEY=your_groq_key
GMAIL_USERNAME=your@gmail.com
GMAIL_APP_PASSWORD=your_app_password
```

Run:
```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`
Swagger UI at `http://localhost:8080/swagger-ui.html`

## Tests
```bash
./mvnw test
```

Covers: JWT security, auth business logic, CRUD services, role-based controller access, and full integration tests with Testcontainers.

## Deployment

Deployed on [Railway](https://railway.com/) with Docker.
Environment variables configured via Railway dashboard.