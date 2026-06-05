# Architecture Constraints

## Required Stack

- Java 21
- Spring Boot 3.x
- Spring MVC
- Thymeleaf
- Bootstrap 5.3
- MyBatis
- MySQL
- JUnit 5
- Gradle

## Required Boundaries

- `GeocodingGateway` owns geocoding behavior and failure handling.
- `FileStorage` owns file persistence and retrieval behavior.
- Public share snapshots are separate from internal records.
- MyBatis mappers return persistence shapes that are adapted before public output.

## Forbidden Stack

- React
- Vue
- Next.js
- JPA

## Forbidden Product Features

- Automatic ad-copy generation.
- Automatic business-type legal judgment.
- Automatic commercial district scoring.
- External listing platform auto-upload.

## Initial Module Direction

- `property`: commercial property records and address structures.
- `inspection`: broker inspection workflow.
- `facility`: dynamic templates and answers.
- `share`: public snapshot generation and rendering.
- `map`: Kakao Maps and geocoding boundaries.
- `file`: storage boundary and attachment metadata.
- `common`: shared validation, identifiers, and errors.
