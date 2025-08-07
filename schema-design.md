# Database Schema Design for Smart Clinic

## 1. MySQL Database Design

Aqui vamos desenhar as tabelas para os dados estruturados.

### Table: patients
### Table: doctors
### Table: appointments
### Table: admins
### Business Logic and Constraints

### Table: patients
- **id**: INT, Primary Key, Auto Increment
- **full_name**: VARCHAR(255), Not Null
- **email**: VARCHAR(255), Not Null, Unique
- **password**: VARCHAR(255), Not Null  // Senha será armazenada como hash
- **phone_number**: VARCHAR(20)
- **created_at**: TIMESTAMP, Default CURRENT_TIMESTAMP

### Table: doctors
- **id**: INT, Primary Key, Auto Increment
- **full_name**: VARCHAR(255), Not Null
- **email**: VARCHAR(255), Not Null, Unique
- **password**: VARCHAR(255), Not Null
- **specialty**: VARCHAR(100)
- **created_at**: TIMESTAMP, Default CURRENT_TIMESTAMP

### Table: appointments
- **id**: INT, Primary Key, Auto Increment
- **patient_id**: INT, Not Null, Foreign Key -> patients(id)
- **doctor_id**: INT, Not Null, Foreign Key -> doctors(id)
- **appointment_time**: DATETIME, Not Null
- **status**: VARCHAR(20), Default 'Scheduled' // Ex: Scheduled, Completed, Canceled
- **created_at**: TIMESTAMP, Default CURRENT_TIMESTAMP

### Table: admins
- **id**: INT, Primary Key, Auto Increment
- **username**: VARCHAR(100), Not Null, Unique
- **password**: VARCHAR(255), Not Null
---

## 2. MongoDB Collection Design

Aqui vamos desenhar as coleções para os dados flexíveis.

### Collection: prescriptions
### Collection: doctor_notes
### Collection: prescriptions

Esta coleção armazena prescrições médicas. É ideal para MongoDB porque uma única prescrição pode ter múltiplos medicamentos (um array), notas opcionais e outros metadados que podem variar.

**Exemplo de Documento:**
```json
{
  "_id": "ObjectId('some_random_id')",
  "appointment_id": "INT", // Referência ao ID da consulta na tabela MySQL appointments
  "patient_id": "INT", // Referência ao ID do paciente na tabela MySQL patients
  "doctor_id": "INT", // Referência ao ID do médico na tabela MySQL doctors
  "issued_date": "ISODate('2025-08-07')",
  "medications": [
    {
      "name": "Amoxicilina",
      "dosage": "500mg",
      "frequency": "a cada 8 horas",
      "duration": "7 dias"
    },
    {
      "name": "Ibuprofeno",
      "dosage": "200mg",
      "frequency": "quando necessário para dor",
      "duration": "5 dias"
    }
  ],
  "notes": "Paciente alérgico a penicilina. Monitorar reações.",
  "metadata": {
    "created_at": "ISODate('2025-08-07T20:30:00Z')",
    "updated_at": "ISODate('2025-08-07T20:30:00Z')"
  }
}

{
    "_id": "ObjectId('some_other_id')",
    "appointment_id": "INT", // Referência à consulta
    "doctor_id": "INT",
    "notes_text": "Paciente relata dor de cabeça persistente há 3 dias. Pressão arterial 12/8. Recomendei descanso e monitoramento. Sem sinais de alarme no momento.",
    "created_at": "ISODate('2025-08-07T20:35:00Z')"
}

### Business Logic and Constraints

- **O que acontece se um paciente for excluído?**
  - **Decisão:** As consultas associadas ao paciente **não** serão excluídas do banco de dados para manter o histórico da clínica. Em vez disso, a conta do paciente pode ser "desativada" (soft delete). Se uma exclusão real for necessária, as consultas relacionadas devem ser anonimizadas ou arquivadas para manter a integridade dos dados históricos do médico.

- **Um médico deve ter permissão para ter consultas sobrepostas?**
  - **Decisão:** Não. O sistema deve ter uma lógica de validação para impedir que um médico seja agendado para duas consultas no mesmo horário. Isso garante a integridade da agenda do médico.


