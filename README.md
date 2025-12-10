# doctor-service

Example usage for the `doctor-service` REST API (endpoints for `DoctorController` and `PatientController`).

Start the application (run from project root):

```bash
./mvnw -DskipTests spring-boot:run
```

By default the app listens on `http://localhost:8080`.

**DoctorController examples**

- GET all doctors

```bash
curl http://localhost:8080/api/doctors
```

- GET doctor by id

```bash
curl http://localhost:8080/api/doctors/1
```

- POST create a new doctor

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","specialization":"Cardiology","email":"john.doe@example.com","phone":"0812345678"}' \
  http://localhost:8080/api/doctors
```

- PUT replace an existing doctor

```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Smith","specialization":"Dermatology","email":"john.smith@example.com","phone":"0890001111"}' \
  http://localhost:8080/api/doctors/1
```

- PATCH partially update a doctor

```bash
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"phone":"0899998888"}' \
  http://localhost:8080/api/doctors/1
```

- DELETE a doctor

```bash
curl -X DELETE http://localhost:8080/api/doctors/1
```

---

**PatientController examples**

The Patient endpoints use DTOs and return responses wrapped in `ApiResponse<T>`.

- GET all patients

```bash
curl http://localhost:8080/api/patients
```

- GET patient by id

```bash
curl http://localhost:8080/api/patients/1
```

- POST create a patient (use ISO date `yyyy-MM-dd` for `dateOfBirth`)

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","dateOfBirth":"1990-05-01","gender":"F","phone":"0812345678","email":"jane.doe@example.com","address":"Bangkok, Thailand"}' \
  http://localhost:8080/api/patients
```

- PUT replace a patient

```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Smith","dateOfBirth":"1990-05-01"}' \
  http://localhost:8080/api/patients/1
```

- PATCH partially update a patient

```bash
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"phone":"0820001111"}' \
  http://localhost:8080/api/patients/1
```

- DELETE a patient

```bash
curl -X DELETE http://localhost:8080/api/patients/1
```

---

Example `ApiResponse` payload returned by Patient endpoints:

```json
{
  "success": true,
  "message": "Patient created",
  "data": {
    "id": 3,
    "firstName": "Jane",
    "lastName": "Doe",
    "dateOfBirth": "1990-05-01",
    "gender": "F",
    "phone": "0812345678",
    "email": "jane.doe@example.com",
    "address": "Bangkok, Thailand"
  },
  "timestamp": "2025-12-10T05:00:00"
}
```

If you want, I can also add examples for `httpie`, a Postman collection, or example responses for the `DoctorController`.

## Filtering, Pagination and Sorting

The `GET /api/doctors` endpoint supports filtering, pagination and sorting via query parameters:

- `specialization` (optional) — filter by specialization (case-insensitive exact match)
- `active` (optional) — filter by active status (`true` or `false`)
- `search` (optional) — case-insensitive contains search across `firstName`, `lastName`, `email`, and `specialization`
- `page` (optional, default `0`) — zero-based page index
- `size` (optional, default `10`) — page size
- `sortBy` (optional, default `id`) — field to sort by (`id`, `firstName`, `lastName`, `specialization`, `email`, `joinedDate`, `active`)
- `sortDir` (optional, default `asc`) — `asc` or `desc`

Examples:

Get active cardiologists, page 0 size 5, sorted by `joinedDate` descending:

```bash
curl "http://localhost:8080/api/doctors?specialization=Cardiology&active=true&page=0&size=5&sortBy=joinedDate&sortDir=desc"
```

Simple search and sort by last name ascending:

```bash
curl "http://localhost:8080/api/doctors?search=smith&sortBy=lastName&sortDir=asc"
```

The `GET /api/patients` endpoint also supports pagination and sorting with the same `page`, `size`, `sortBy`, and `sortDir` parameters. The response returns a paged `ApiResponse` containing a `Page<PatientDTO>` in the `data` field.

## Notes

- Filtering, sorting and pagination are currently applied in-memory by the repository/service layer because this project uses an in-memory repository. When moving to a database (e.g., Spring Data JPA), push these operations down to the database for better performance.
- Dates use ISO-8601 (e.g., `"2020-01-15"`) for `LocalDate` fields.

If you want, I can add Postman examples, integrate Swagger/OpenAPI, or update `DoctorV2Controller`/`DoctorV3Controller` to expose identical query parameters.
