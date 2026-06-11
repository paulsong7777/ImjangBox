# Operations Guide

This guide reflects the current MVP codebase. It covers local development, test execution, the `local-db` MySQL profile, and a practical small-server deployment path.

## Profiles

| Profile | Purpose | Persistence | Notes |
| --- | --- | --- | --- |
| `local` | Default developer run profile | In-memory inspection/share/facility mappers plus local file storage | Selected by `spring.profiles.default=local`; does not require MySQL. |
| `test` | Main automated test profile | In-memory app wiring | Excludes JDBC/MyBatis autoconfiguration and keeps tests deterministic. |
| `mybatis-integration` | Mapper integration tests only | H2 in MySQL compatibility mode | Uses `src/test/resources/db/mybatis-integration`; not a runtime deployment profile. |
| `local-db` | MySQL-backed local or small MVP server run | MySQL, MyBatis XML, Flyway migrations, local file storage | Current production-like profile. Secrets must be supplied outside source control. |

There is not a separate `prod` profile yet. For a small MVP server, run with `--spring.profiles.active=local-db` and production-grade environment variables, filesystem permissions, TLS termination, and backup automation.

## Local Configuration

Default local run:

```bash
./gradlew bootRun
```

The app starts on the `local` profile unless another profile is selected. The local profile:

- excludes JDBC and MyBatis autoconfiguration
- uses in-memory mappers for inspections, facilities, share snapshots, and audit rows
- disables Thymeleaf template caching
- provides sample facility templates through `imjangbox.facility-templates.items[...]`
- keeps broker credentials at `broker` / `broker-password`
- stores uploaded files below `imjangbox.file-storage.local-root`

The default file root is:

```text
${java.io.tmpdir}/imjangbox-local-files
```

Override it with a non-shared local path when testing attachments:

```bash
./gradlew bootRun --args='--imjangbox.file-storage.local-root=/tmp/imjangbox-files'
```

The local profile is not durable. Restarting the app loses in-memory inspection, facility-answer, share-card, and audit data. Files already written to the configured file root may remain on disk, but metadata that points to them is lost.

## Test Configuration

Run the full suite:

```bash
./gradlew test
```

The normal `test` profile does not require MySQL. SQL-backed mapper behavior is covered by the dedicated MyBatis integration test profile, which uses H2 in MySQL mode and real MyBatis mapper XML.

Useful focused checks:

```bash
./gradlew test --tests com.imjangbox.inspection.persistence.MyBatisPersistenceIntegrationTest
./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest
./gradlew test --tests com.imjangbox.file.LocalFileStorageTest
```

On WSL Windows mounts such as `/mnt/c`, Gradle build output defaults to `/tmp/imjangbox-build` to avoid mount-level permission failures. Override with `IMJANGBOX_BUILD_DIR` when needed.

## MySQL Configuration

Use `local-db` for MySQL-backed runs:

```bash
export IMJANGBOX_DB_URL='jdbc:mysql://<db-host>:3306/<database-name>'
export IMJANGBOX_DB_USERNAME='<database-user>'
export IMJANGBOX_DB_PASSWORD='<database-password>'
export IMJANGBOX_BROKER_PASSWORD='<broker-password>'

./gradlew bootRun --args='--spring.profiles.active=local-db'
```

Required database properties in the current code:

- `spring.datasource.url=${IMJANGBOX_DB_URL}`
- `spring.datasource.username=${IMJANGBOX_DB_USERNAME}`
- `spring.datasource.password=${IMJANGBOX_DB_PASSWORD}`
- `spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`
- `spring.sql.init.mode=never`
- `mybatis.mapper-locations=classpath:/mappers/**/*.xml`

Flyway migrations are loaded from `src/main/resources/db/migration` by Spring Boot's Flyway autoconfiguration. Do not edit migrations that have already been applied to a shared database. Add a new `V...__description.sql` migration instead.

## Required Secrets And Environment Variables

Keep secrets out of Git, shell history where possible, process logs, screenshots, and shared docs.

Required outside `local` and `test`:

- `IMJANGBOX_DB_URL`: JDBC URL for the MySQL database when using `local-db`
- `IMJANGBOX_DB_USERNAME`: MySQL user
- `IMJANGBOX_DB_PASSWORD`: MySQL password
- `IMJANGBOX_BROKER_PASSWORD`: broker HTTP Basic password, mapped by Spring relaxed binding to `imjangbox.broker.password`

Optional:

- `IMJANGBOX_BROKER_USERNAME`: broker HTTP Basic username, mapped to `imjangbox.broker.username`; defaults to `broker`
- `KAKAO_REST_API_KEY`: backend Kakao Local REST API key, mapped to `imjangbox.geocoding.kakao.rest-api-key`
- `KAKAO_MAP_JAVASCRIPT_KEY`: browser Kakao Maps JavaScript key, mapped to `imjangbox.maps.kakao.javascript-api-key`
- `IMJANGBOX_BUILD_DIR`: Gradle output directory override

Operational properties that may be passed as command-line args or environment-backed config:

- `imjangbox.file-storage.local-root`
- `imjangbox.geocoding.kakao.enabled`
- `imjangbox.geocoding.kakao.base-url`
- `imjangbox.maps.kakao.enabled`
- `imjangbox.maps.kakao.sdk-base-url`
- `imjangbox.maps.kakao.default-latitude`
- `imjangbox.maps.kakao.default-longitude`
- `imjangbox.maps.kakao.default-level`

## Kakao Configuration

Kakao backend geocoding and browser map display are separate.

Backend geocoding:

```bash
export KAKAO_REST_API_KEY='<kakao-rest-api-key>'
./gradlew bootRun --args='--imjangbox.geocoding.kakao.enabled=true'
```

Browser map display:

```bash
export KAKAO_MAP_JAVASCRIPT_KEY='<kakao-javascript-key>'
./gradlew bootRun --args='--imjangbox.maps.kakao.enabled=true'
```

Do not reuse the REST API key as the browser JavaScript key. Public broker pages may include the JavaScript SDK URL when maps are enabled, so only the browser-scoped JavaScript key belongs there. The REST key must stay server-side.

Both integrations are disabled by default:

- `imjangbox.geocoding.kakao.enabled=false`
- `imjangbox.maps.kakao.enabled=false`

## File Storage And Backups

The current file backend is `LocalFileStorage`. It stores files under:

```text
${imjangbox.file-storage.local-root}/inspection-files/{inspectionId}/{uuid}.{extension}
```

Current upload policy:

- at most 5 non-empty attachments per request
- at most 10 MiB per attachment
- allowed attachment types: `application/pdf`, `image/jpeg`, `image/png`, `text/plain`
- public share image streaming is limited to `image/jpeg` and `image/png`

For an MVP server:

- set `imjangbox.file-storage.local-root` to a durable path outside the application checkout, such as `/var/lib/imjangbox/files`
- make that directory readable and writable only by the application user and backup user
- include this directory in backups with preserved paths and permissions
- keep file backups coordinated with database backups, because database rows contain the storage keys

Example backup command for files:

```bash
rsync -a --delete /var/lib/imjangbox/files/ /backup/imjangbox/files/
```

Do not expose the file-storage root through a static web server. Public images must continue to go through share-scoped routes like `/share/{shareId}/images/{displayOrder}`.

## Database Backup And Restore

Back up MySQL before every deployment and on a regular schedule. Keep backups encrypted or stored in access-controlled infrastructure.

Example logical backup:

```bash
MYSQL_PWD="$IMJANGBOX_DB_PASSWORD" \
mysqldump --single-transaction --routines --triggers \
  -h <db-host> -u "$IMJANGBOX_DB_USERNAME" <database-name> \
  > /backup/imjangbox/mysql/imjangbox-$(date +%Y%m%d%H%M%S).sql
```

Restore into a verified empty or intentionally replaced database:

```bash
MYSQL_PWD="$IMJANGBOX_DB_PASSWORD" \
mysql -h <db-host> -u "$IMJANGBOX_DB_USERNAME" <database-name> \
  < /backup/imjangbox/mysql/<backup-file>.sql
```

Restore checklist:

- stop the application first
- restore the database dump
- restore the matching file-storage backup
- start the application with the same profile and file root
- run the smoke tests below
- verify at least one broker inspection edit page and one public share card with images

## Deployment Checklist For A Small MVP Server

Before deploying:

- install Java 21
- provision MySQL and create the target database/user
- configure backups for MySQL and `imjangbox.file-storage.local-root`
- create a dedicated OS user for the app
- set `IMJANGBOX_DB_URL`, `IMJANGBOX_DB_USERNAME`, `IMJANGBOX_DB_PASSWORD`, and `IMJANGBOX_BROKER_PASSWORD`
- optionally set `IMJANGBOX_BROKER_USERNAME`
- optionally set Kakao keys and enable the matching feature flags
- set `imjangbox.file-storage.local-root` to a durable server path
- run `./gradlew test`
- take a database and file backup before replacing the running version

Start command example:

```bash
./gradlew bootRun --args='--spring.profiles.active=local-db --server.port=8080 --imjangbox.file-storage.local-root=/var/lib/imjangbox/files'
```

For a real server, run the app behind a TLS-terminating reverse proxy and restrict direct access to the app port. Broker routes use HTTP Basic today, so TLS is required whenever credentials cross a network.

## Smoke Tests

After deployment:

```bash
curl -i http://<host>/
curl -i -u "${IMJANGBOX_BROKER_USERNAME:-broker}:$IMJANGBOX_BROKER_PASSWORD" \
  http://<host>/broker/inspections/new
curl -i http://<host>/not-a-route
```

Expected:

- `/` returns `200`
- `/broker/inspections/new` returns `401` without credentials and `200` with valid broker credentials
- `/not-a-route` returns `404`

Manual browser smoke:

- create an inspection with public and private marker values
- upload a small valid PNG or JPEG
- generate a share card from the broker edit page
- open the public share URL without authentication
- confirm public values and image render
- confirm private memo, internal risk memo, internal address, contact-log content, storage keys, local paths, and original filenames do not render

## Rollback

Rollback depends on whether a deployment applied new Flyway migrations.

No migration applied:

- stop the new app version
- redeploy the previous app version
- start it with the same environment variables and file root
- run smoke tests

Migration applied:

- stop the app
- restore the pre-deploy MySQL backup
- restore the matching file-storage backup if files changed during the failed deployment
- redeploy the previous app version
- start it with the same environment variables and file root
- run smoke tests

Do not manually delete rows from Flyway metadata as a rollback substitute. Treat database rollback as restore-from-backup unless a forward fix migration has been designed and tested.
