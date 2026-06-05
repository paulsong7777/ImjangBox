# imjangbox

Commercial-property field inspection and proposal ledger for Korean commercial real estate brokers.

## Stack

- Java 21
- Spring Boot 3.5.x
- Spring MVC and Thymeleaf
- Bootstrap 5.3 for future UI styling
- MyBatis with MySQL
- Gradle and JUnit 5

## Run

Use the wrapper from the repository root:

```bash
./gradlew bootRun
```

When the project is checked out under a WSL Windows mount such as `/mnt/c`, Gradle writes generated build output to `/tmp/imjangbox-build` by default because that mount can reject Unix permission changes during resource copying. Override it with `IMJANGBOX_BUILD_DIR` when needed.

The default `local` profile starts the Phase 0 application without requiring a database schema. When Phase 1 schema work starts, run with the database profile and environment variables:

```bash
IMJANGBOX_DB_URL=jdbc:mysql://localhost:3306/imjangbox \
IMJANGBOX_DB_USERNAME=imjangbox \
IMJANGBOX_DB_PASSWORD=change-me \
./gradlew bootRun --args='--spring.profiles.active=local-db'
```

## Test

```bash
./gradlew test
```

Phase 0 tests load the Spring application context and render the root MVC route without adding product domain behavior.
