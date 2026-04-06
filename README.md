# Finance Dashboard Project

A Spring Boot-based financial management system with role-based access for Admin, Viewer, and Analyst roles.

## Features
- **Security**: JWT-based authentication and role-based access control (RBAC).
- **Admin**: Full control over roles, categories, and financial records.
- **Viewer**: Read-only access to specific user records and summaries.
- **Analyst**: Access to global expense data and all viewer-level functionalities.

## Setup Instructions

### Prerequisites
- **Java 17** or higher.
- **MySQL Server** running on `127.0.0.1:3306`.
- **Maven** (included via `./mvnw`).

### Database Configuration
The application is configured to connect to a MySQL database named `dashboard`.
- Edit `src/main/resources/application.properties` to update your credentials:
  ```properties
  spring.datasource.username=root
  spring.datasource.password=YourPassword
  ```
- The application will automatically create the database if it doesn't exist (`createDatabaseIfNotExist=true`).
- Tables are automatically managed via Hibernate (`ddl-auto=update`).

### Running the Application
1. Navigate to the project root:
   ```bash
   cd Finanace-DashBoard
   ```
2. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Explanation

### Authentication
- `POST /auth/signup`: Register a new user.
- `POST /auth/login`: Login with username and password to receive a JWT.

> [!IMPORTANT]
> All protected endpoints (`/admin/**`, `/viewer/**`, `/analyst/**`) require an `Authorization` header with a Bearer token:
> `Authorization: Bearer <your_jwt_token>`

### Admin Controller (`/admin`)
- **Requires ROLE_ADMIN**
- `POST /admin/roles`: Create a new role.
- `GET /admin/roles`: List all roles.
- `POST /admin/categories`: Create a new financial category.
- `POST /admin/records`: Add a new financial record.
- `GET /admin/summary`: Global financial summary.
- `GET /admin/records/expenses`: Fetch all non-deleted expense records globally.

### Viewer Controller (`/viewer`)
- **Requires ROLE_VIEWER**
- `GET /viewer/records/{username}`: Fetch records for a specific user.
- `GET /viewer/summary/{username}`: Financial summary for a specific user.
- `POST /viewer/filter`: Filter records by date range, category, or type.

### Analyst Controller (`/analyst`)
- **Requires ROLE_ANALYST**
- `GET /analyst/records/expenses`: Analyst-level access to global expense data.
- Supports all `Viewer` methods for data analysis.

## Assumptions Made
1. **Security**: The application uses stateless JWT authentication. Tokens expire after 24 hours by default.
2. **Database**: MySQL is the persistent store for users, roles, categories, and records.
3. **Roles**: Roles must be prefixed with `ROLE_` in the database or handled via the security configuration (e.g., `ROLE_ADMIN`, `ROLE_VIEWER`, `ROLE_ANALYST`).

## Tradeoffs Considered
1. **Stateless Auth**: Using JWTs allows for a stateless backend, which is scalable but requires careful token management (e.g., secret rotation).
2. **Global Secret**: The JWT secret is currently stored in `application.properties`. In production, this should be moved to an environment variable or secret manager.
3. **Analyst Permissions**: The `AnalystController` duplicates `Viewer` functionality for ease of use by analysts, relying on service-level reuse.
