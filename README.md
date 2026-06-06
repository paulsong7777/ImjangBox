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

The default `local` profile starts the Phase 2 broker inspection ledger without requiring MySQL. It uses an in-memory inspection mapper, so inspection records, contact logs, and attachment metadata are kept only for the current process.

Broker routes require HTTP Basic authentication:

- username: `broker`
- password: `broker-password`

Open the broker create form at:

```text
http://localhost:8080/broker/inspections/new
```

The `local-db` profile uses MyBatis/MySQL and Flyway migrations instead of the in-memory local mapper:

```bash
IMJANGBOX_DB_URL=jdbc:mysql://localhost:3306/imjangbox \
IMJANGBOX_DB_USERNAME=imjangbox \
IMJANGBOX_DB_PASSWORD=change-me \
./gradlew bootRun --args='--spring.profiles.active=local-db'
```

Outside the `local` and `test` profiles, set `imjangbox.broker.password` to a non-empty secret.

## Attachments

Broker form attachments are stored through `FileStorage`. The local implementation writes files below:

```text
${java.io.tmpdir}/imjangbox-local-files
```

Override the local root with an application property:

```bash
./gradlew bootRun --args='--imjangbox.file-storage.local-root=/tmp/imjangbox-files'
```

Attachment metadata is internal ledger data. Storage keys and contact-log content are not part of public share DTOs or templates.

## Test

```bash
./gradlew test
```

Useful focused checks:

```bash
./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest
./gradlew test --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest
./gradlew test --tests com.imjangbox.file.LocalFileStorageTest
```

The full test suite covers broker authentication, create/edit form validation, append-only contact-log writes, attachment validation/storage metadata, MyBatis persistence shape, and public share privacy regressions.
