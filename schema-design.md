# Design de Banco de Dados MySQL

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- email: VARCHAR(100), Unique, Not Null
- phone: VARCHAR(15)
- date_of_birth: DATE

### Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- email: VARCHAR(100), Unique, Not Null
- specialty: VARCHAR(100), Not Null
- phone: VARCHAR(15)

### Table: appointments
- id: INT, Primary Key, Auto Increment  
- doctor_id: INT, Foreign Key → doctors(id)  
- patient_id: INT, Foreign Key → patients(id)  
- appointment_time: DATETIME, Not Null  
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, Primary Key, Auto Increment  
- username: VARCHAR(50), Unique, Not Null  
- password_hash: VARCHAR(255), Not Null  
- role: VARCHAR(50)

---

# Design de Coleção MongoDB


### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Tome 1 comprimido a cada 6 horas.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}

{
  "patient_id": "patient_123",
  "medical_history": [
    {
      "date": "2023-09-15",
      "notes": "Dor nas costas persistente.",
      "doctor_id": "doctor_456"
    }
  ],
  "prescriptions": [
    {
      "medication": "Ibuprofeno",
      "dosage": "200mg",
      "duration": "5 dias"
    }
  ]
}

{
  "doctor_id": "doctor_456",
  "bio": "Especialista em ortopedia com 10 anos de experiência.",
  "photo_url": "https://clinic.com/photos/doctor_456.jpg",
  "documents": [
    { "type": "CRM", "url": "https://clinic.com/docs/crm.pdf" }
  ]
}

{
  "timestamp": "2023-10-01T14:22:00Z",
  "user_id": "admin_01",
  "action": "login_success",
  "metadata": {
    "ip": "192.168.1.100",
    "browser": "Chrome"
  }
}

