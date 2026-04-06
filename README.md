# Finance Dashboard Project

A Spring Boot-based financial management system with role-based access for Admin, Viewer, and Analyst roles.

## Features
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
- `POST /auth/signup`: Register a new user with a specific role.

### Admin Controller (`/admin`)
- `POST /admin/roles`: Create a new role.
- `GET /admin/roles`: List all roles.
- `POST /admin/categories`: Create a new financial category.
- `POST /admin/records`: Add a new financial record.
- `GET /admin/summary`: Global financial summary (Net Balance, Income, Expenses).
- `GET /admin/records/expenses`: Fetch all non-deleted expense records globally.

### Viewer Controller (`/viewer`)
- `GET /viewer/records/{username}`: Fetch records for a specific user.
- `GET /viewer/summary/{username}`: Financial summary for a specific user.
- `POST /viewer/filter`: Filter records by date range, category, or type.

### Analyst Controller (`/analyst`)
- `GET /analyst/records/expenses`: Analyst-level access to global expense data (admin-level).
- Supports all `Viewer` methods for data analysis.

## Assumptions Made
1. **Security**: The project uses Spring Security, but custom RBAC filters are partially implemented. Endpoints assume basic auth or no-auth depending on the final security configuration.
2. **Database**: MySQL is assumed to be the persistence layer.
3. **Roles**: Standard roles expected are `ADMIN`, `VIEWER`, and `ANALYST`.

## Tradeoffs Considered
1. **Service Sharing**: The `AnalystController` directly uses `AdminService` and `ViewerService`. This avoids duplicate logic but tightly couples the services.
2. **DDL Auto-Update**: `spring.jpa.hibernate.ddl-auto=update` is used for development speed, though `validate` or `none` would be preferred for production.
3. **Database Creation**: Added `createDatabaseIfNotExist=true` to the JDBC URL to minimize manual setup steps for the user.
