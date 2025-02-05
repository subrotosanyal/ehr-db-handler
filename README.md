# EHR Database Handler

This project implements a secure and efficient database handling layer for an Electronic Health Record (EHR) system using Java 21, Spring Boot, and PostgreSQL, following FHIR standards.

## Features

- FHIR-compliant REST API
- Secure handling of sensitive patient data
- Comprehensive patient and medical history management
- Swagger/OpenAPI documentation
- Docker support for easy deployment
- Integration tests with TestContainers

## Prerequisites

- Java 21
- PostgreSQL 15+
- Docker and Docker Compose (for containerized deployment)
- Gradle 8.5+

## Project Structure

```
net.sanyal.ehr
├── controller    # REST API controllers
├── service      # Business logic layer
├── repository   # Database access layer
├── model        # Domain models
├── config       # Configuration classes
├── utils        # Utility classes
└── tests        # Unit and integration tests
```

## Getting Started

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/yourusername/ehr-db-handler.git
cd ehr-db-handler
```

2. Configure environment variables:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/ehr_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export DB_POOL_SIZE=10
```

3. Build the project:
```bash
./gradlew build
```

4. Run the tests:
```bash
./gradlew test
```

### Docker Deployment

1. Build and run using Docker Compose:
```bash
docker-compose up --build
```

The application will be available at http://localhost:8080

## API Documentation

Once the application is running, access the Swagger UI at:
http://localhost:8080/swagger-ui.html

### Available Endpoints

- `POST /fhir/Patient` - Create a new patient
- `GET /fhir/Patient/{id}` - Get patient by ID
- `GET /fhir/Patient?name={name}` - Search patients by name
- `PUT /fhir/Patient/{id}` - Update patient
- `DELETE /fhir/Patient/{id}` - Delete patient

## Database Schema

The application uses PostgreSQL with the following main tables:
- `patient`
- `contact_details`
- `insurance_details`
- `medical_history`
- `medications`
- `immunizations`
- `substance_consumption`
- `menstruation_pregnancy`

Database migration scripts are located in `src/main/resources/db/migration/`.

## Security

- Sensitive data (SSN, medical records) is encrypted at rest using AES-256
- All API endpoints use HTTPS
- Database passwords and sensitive configuration are managed through environment variables

## Testing

- Unit tests: `./gradlew test`
- Integration tests: `./gradlew integrationTest`

Integration tests use TestContainers to spin up a PostgreSQL instance automatically.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
