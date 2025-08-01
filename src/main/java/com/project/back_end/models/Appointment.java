package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @ManyToOne
    @NotNull
    private Patient patient;

    @NotNull
    @Future(message = "O horário do agendamento deve ser no futuro")
    private LocalDateTime appointmentTime;

    @NotNull
    private int status; // 0 = Agendado, 1 = Concluído

    // Getters e Setters
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Métodos auxiliares
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1); // Término 1h depois
    }

    @Transient
    public String getAppointmentDate() {
        return appointmentTime.toLocalDate().toString();
    }

    @Transient
    public String getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime().toString();
    }
}