# RestHMS: Hospital Management REST API

[![CI](https://github.com/abhiramkamarajugadda/RestHMS/actions/workflows/ci.yml/badge.svg)](https://github.com/abhiramkamarajugadda/RestHMS/actions/workflows/ci.yml)

A Spring Boot REST API that manages doctors and patients, with a one-to-many Doctor to Patient relationship in MySQL and Swagger UI documentation.

## Live Demo

**Live link:** [abhi-resthms.onrender.com](https://abhi-resthms.onrender.com) (opens Swagger UI, where every endpoint can be tried in the browser)

> Note: the free server sleeps when idle, so the first visit may take about a minute to wake up.

The demo runs on an in-memory H2 database seeded with 4 sample doctors and 8 sample patients (fictional). Anything you add or delete resets when the server restarts.

## Features

- Full CRUD for doctors and for patients (10 endpoints)
- Patients are assigned to a doctor through a `@ManyToOne` foreign key (`doctor_id`)
- `@JsonIgnore` on the doctor's patient list to avoid recursive JSON
- REST status codes: 201 Created with a `Location` header, 200 OK, 204 No Content on delete, 404 Not Found for unknown ids, 400 for an update without an id
- `@WebMvcTest` controller tests with Mockito, run by GitHub Actions on every push
- Layered design: controllers, services and repositories
- Interactive API documentation through Swagger UI (springdoc-openapi) at `/swagger-ui.html`; the root URL redirects there
- `demo` profile: in-memory H2 with sample data, so the API runs with no database setup

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1, Spring Web MVC (`@RestController`) |
| Persistence | Spring Data JPA, Hibernate, MySQL (`mysql-connector-j`) |
| API docs | springdoc-openapi 3.1.0 (Swagger UI) |
| Demo database | H2 in-memory (`demo` profile) |
| CI/CD | GitHub Actions (`mvn -B verify` on JDK 21, Docker image build) |
| Containers | Multi-stage Dockerfile (JDK 21 build, JRE 21 runtime) |
| Build | Maven (Maven Wrapper included) |

## Project Structure

```text
HospitalManagementSystem/
  pom.xml
  src/main/java/com/hms/
    HospitalManagementSystemApplication.java
    controller/   DoctorController, PatientController
    services/     DoctorService, PatientService
    repository/   DoctorRepository, PatientRepository (JpaRepository)
    model/        Doctor, Patient
  src/main/resources/application.properties
```

## REST API

| Method | Endpoint | Description | Status |
|---|---|---|---|
| POST | `/api/hms/doctors` | Add a doctor | 201 Created |
| GET | `/api/hms/doctors` | List all doctors | 200 OK |
| GET | `/api/hms/doctors/{id}` | Get a doctor by id | 200 OK, 404 Not Found |
| PUT | `/api/hms/doctors` | Update a doctor (JSON body with `id`) | 200 OK, 400 without `id`, 404 unknown `id` |
| DELETE | `/api/hms/doctors/{id}` | Delete a doctor | 204 No Content, 404 Not Found |
| POST | `/api/hms/patients` | Add a patient | 201 Created |
| GET | `/api/hms/patients` | List all patients | 200 OK |
| GET | `/api/hms/patients/{id}` | Get a patient by id | 200 OK, 404 Not Found |
| PUT | `/api/hms/patients` | Update a patient (JSON body with `id`) | 200 OK, 400 without `id`, 404 unknown `id` |
| DELETE | `/api/hms/patients/{id}` | Delete a patient | 204 No Content, 404 Not Found |

Sample patient body:

```json
{
  "name": "Sita",
  "age": 34,
  "weight": 58.5,
  "gender": "Female",
  "disease": "Fever",
  "doctor": { "id": 1 }
}
```

## Data Model

| Entity | Fields | Relationship |
|---|---|---|
| `Doctor` | id, name, gender, specialization, salary, age | One doctor has many patients (`cascade = ALL`) |
| `Patient` | id, name, age, weight, gender, disease | Many patients to one doctor (`doctor_id`) |

## Getting Started

**Prerequisites:** JDK 21 and Maven (or the included Maven Wrapper: `sh mvnw spring-boot:run`). MySQL 8 is only needed for the default profile.

```bash
git clone https://github.com/abhiramkamarajugadda/RestHMS.git
cd RestHMS/HospitalManagementSystem
```

**Quick start, no database (demo profile):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

**With MySQL (default profile):** the `boothms` database is created on first run.
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
mvn spring-boot:run
```

API base: `http://localhost:7000/api/hms`
Swagger UI: `http://localhost:7000/swagger-ui.html`

## Running with Docker

```bash
docker build -t resthms .
docker run -p 7000:7000 resthms
```

The image starts in the `demo` profile. To use MySQL instead, pass `-e SPRING_PROFILES_ACTIVE= -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=...`.

## Deploy to Render (free)

No database account or secrets are needed.

1. On [Render](https://render.com) choose **New > Blueprint** and pick this repository. `render.yaml` builds the `Dockerfile` on the free plan.
2. The only environment variable is `SPRING_PROFILES_ACTIVE=demo` (already in `render.yaml`). Render sets `PORT`.
3. Deploy, then open `https://<service>.onrender.com/swagger-ui.html`.

## Tests

```bash
cd HospitalManagementSystem
mvn -B verify
```

Runs 13 tests (controller tests with `@WebMvcTest` and `@MockitoBean`, a context test, and a `demo` profile test that checks the seed data and the OpenAPI docs), all on in-memory H2, so no MySQL is needed. GitHub Actions runs the same command on JDK 21 and builds the Docker image on every push.

## Author

**Abhiram Kamarajugadda**
GitHub: [abhiramkamarajugadda](https://github.com/abhiramkamarajugadda) | Portfolio: [abhiramkamarajugadda.github.io](https://abhiramkamarajugadda.github.io)
