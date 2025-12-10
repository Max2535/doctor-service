# doctor-service

ตัวอย่างการเรียกใช้งาน REST API สำหรับโปรเจค `doctor-service` (ตัวอย่างสำหรับ `DoctorController` และ `PatientController`)

เริ่มต้นแอป (จากโฟลเดอร์โปรเจค):

```bash
./mvnw -DskipTests spring-boot:run
```

ค่าเริ่มต้น: แอปจะรันที่ `http://localhost:8080`

**ตัวอย่างการเรียกใช้งาน `DoctorController`**

- GET ทั้งหมด

```bash
curl http://localhost:8080/api/doctors
```

- GET ตาม `id`

```bash
curl http://localhost:8080/api/doctors/1
```

- POST สร้าง doctor

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","specialization":"Cardiology","email":"john.doe@example.com","phone":"0812345678"}' \
  http://localhost:8080/api/doctors
```

- PUT อัปเดต doctor ทั้งหมด (replace)

```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Smith","specialization":"Dermatology","email":"john.smith@example.com","phone":"0890001111"}' \
  http://localhost:8080/api/doctors/1
```

- PATCH แก้บางฟิลด์

```bash
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"phone":"0899998888"}' \
  http://localhost:8080/api/doctors/1
```

- DELETE

```bash
curl -X DELETE http://localhost:8080/api/doctors/1
```

---

**ตัวอย่างการเรียกใช้งาน `PatientController`**

Controller ของ `Patient` ใช้ DTO + `ApiResponse<T>` wrapper ในการตอบผล ตัวอย่าง

- GET ทั้งหมด

```bash
curl http://localhost:8080/api/patients
```

- GET ตาม `id`

```bash
curl http://localhost:8080/api/patients/1
```

- POST สร้าง patient (dateOfBirth เป็นรูปแบบ ISO `yyyy-MM-dd`)

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","dateOfBirth":"1990-05-01","gender":"F","phone":"0812345678","email":"jane.doe@example.com","address":"Bangkok, Thailand"}' \
  http://localhost:8080/api/patients
```

- PUT อัปเดต (replace)

```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Smith","dateOfBirth":"1990-05-01"}' \
  http://localhost:8080/api/patients/1
```

- PATCH แก้บางฟิลด์

```bash
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"phone":"0820001111"}' \
  http://localhost:8080/api/patients/1
```

- DELETE

```bash
curl -X DELETE http://localhost:8080/api/patients/1
```

---

ตัวอย่างการตอบกลับสำหรับ `Patient` จะถูกห่อด้วย `ApiResponse<T>` เช่น:

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

ถ้าต้องการ ผมสามารถเพิ่มตัวอย่างการเรียกผ่าน `httpie`, Postman collection, หรือเพิ่มตัวอย่าง response ของ `DoctorController` ได้ด้วยครับ
