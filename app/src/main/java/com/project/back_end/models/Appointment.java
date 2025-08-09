package com.project.back_end.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private String status;

    // Getters e Setters para os campos persistidos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Métodos auxiliares (não persistidos no banco)

    /**
     * Retorna apenas a parte da DATA do agendamento.
     */
    @Transient
    public LocalDate getAppointmentDate() {
        return (this.appointmentTime != null) ? this.appointmentTime.toLocalDate() : null;
    }

    /**
     * Retorna apenas a parte da HORA do agendamento.
     */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return (this.appointmentTime != null) ? this.appointmentTime.toLocalTime() : null;
    }

    /**
     * Retorna o ID do médico associado a este agendamento.
     */
    @Transient
    public Long getDoctorId() {
        return (this.doctor != null) ? this.doctor.getId() : null;
    }

    /**
     * Retorna o ID do paciente associado a este agendamento.
     */
    @Transient
    public Long getPatientId() {
        return (this.patient != null) ? this.patient.getId() : null;
    }
}
