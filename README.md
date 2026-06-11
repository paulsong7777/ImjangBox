# ImjangBox

> **Status:** `v0.1.0-alpha`  
> **Project:** ImjangBox — commercial-property field inspection and proposal ledger  
> **Focus:** broker-side inspection records, facility checks, contact logs, attachment handling, and privacy-safe customer share cards  
> **Stack:** Java 21, Spring Boot 3.5.x, Thymeleaf, Bootstrap 5.3, MyBatis, MySQL  
> **CI:** GitHub Actions test workflow passing  
> **Development:** AI-assisted development workflow with Codex

Latest pre-release: [`v0.1.0-alpha`](https://github.com/paulsong7777/ImjangBox/releases/tag/v0.1.0-alpha)

## 한국어 개요

**ImjangBox(임장박스)**는 상가 전문 공인중개사를 위한 현장 기록·확인·고객 제안 장부입니다.

상가 중개 업무에서는 주소, 사진, 보증금, 월세, 권리금, 시설 상태, 임대인·임차인 연락 내용, 내부 메모가 여러 곳에 흩어지는 경우가 많습니다.
ImjangBox는 이런 정보를 한 곳에 기록하고, 나중에 다시 확인하거나 고객에게 제안할 수 있는 형태로 정리하는 것을 목표로 합니다.

이 프로젝트는 대형 부동산 CRM이나 광고 자동화 도구가 아니라, 현장에서 본 상가 정보를 빠르게 저장하고, 확인 상태를 구분하며, 고객에게 공유 가능한 정보만 안전하게 분리하는 실무형 MVP입니다.

### 주요 대상 사용자

* 상가 전문 1인 공인중개사
* 소규모 상가 중개사무소
* 상가·사무실·점포 매물을 현장에서 직접 확인하는 중개사
* 고객에게 매물 제안 자료를 자주 보내는 현장형 중개사

### 핵심 흐름

```text
Record → Verify → Propose
```

* **Record:** 상가 주소, 가격 조건, 권리금, 사진, 메모, 연락 이력을 기록합니다.
* **Verify:** 덕트, 급배수, 가스, 전기, 화장실, 주차, 간판 등 상가 실사 항목과 확인 상태를 정리합니다.
* **Propose:** 내부 메모와 연락처를 제외하고, 고객에게 보여줄 수 있는 정보만 공유 카드로 정리하는 구조를 지향합니다.

### 현재 MVP 상태

현재 `v0.1.0-alpha` 단계에서는 브로커 측 임장 기록 장부와 고객 공유 카드 MVP 기능을 중심으로 개발하고 있습니다.

현재 초점은 다음과 같습니다.

* 상가 임장 기록 생성 및 수정
* 연락 이력 기록
* 첨부파일 메타데이터 및 업로드 검증
* 상가 시설 체크 템플릿
* 내부정보와 고객공유정보 분리 원칙
* 고객 공유 카드 snapshot 생성 및 공개 화면
* 공유 카드용 이미지 스트리밍
* 공유 카드 생성 감사 기록
* Codex 기반 AI-assisted development workflow

고객 공유 카드는 내부 임장 기록을 직접 노출하지 않고, 생성 시점의 공개용 snapshot 구조에서 렌더링합니다.

### 개인정보 및 내부정보 보호 원칙

고객에게 공개되는 화면에는 아래 정보가 노출되지 않아야 합니다.

* 내부 메모
* 내부 가격 메모
* 임대인 연락처
* 임차인 연락처
* 공동중개 연락처
* 통화 기록
* 첨부파일 저장 경로
* 협상 메모
* 수수료 관련 메모


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
IMJANGBOX_DB_URL='jdbc:mysql://<db-host>:3306/<database-name>' \
IMJANGBOX_DB_USERNAME='<database-user>' \
IMJANGBOX_DB_PASSWORD='<database-password>' \
IMJANGBOX_BROKER_PASSWORD='<broker-password>' \
./gradlew bootRun --args='--spring.profiles.active=local-db'
```

Outside the `local` and `test` profiles, set `imjangbox.broker.password` to a non-empty secret.

## Operations

See [docs/operations.md](docs/operations.md) for the current deployment, configuration, Kakao key, file-storage, backup/restore, smoke-test, and rollback checklist.

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
./gradlew test --tests com.imjangbox.inspection.persistence.MyBatisPersistenceIntegrationTest
./gradlew test --tests com.imjangbox.file.LocalFileStorageTest
```

The full test suite covers broker authentication, create/edit form validation, append-only contact-log writes, attachment validation/storage metadata, MyBatis persistence shape, SQL-backed mapper integration behavior, and public share privacy regressions.
