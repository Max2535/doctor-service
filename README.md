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
