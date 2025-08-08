package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Representa a entidade 'Appointment' (Agendamento) no banco de dados.
 * Esta classe conecta as entidades Doctor e Patient e armazena informações
 * sobre a consulta.
 */
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O médico associado a este agendamento.
     * @ManyToOne: Muitos agendamentos podem pertencer a UM médico.
     * @JoinColumn: Especifica a coluna de chave estrangeira (doctor_id) na tabela de agendamentos.
     */
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "O médico é obrigatório")
    private Doctor doctor;

    /**
     * O paciente associado a este agendamento.
     * @ManyToOne: Muitos agendamentos podem pertencer a UM paciente.
     * @JoinColumn: Especifica a coluna de chave estrangeira (patient_id) na tabela de agendamentos.
     */
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "O paciente é obrigatório")
    private Patient patient;

    /**
     * A data e hora do agendamento.
     * @Future: Garante que a data e hora do agendamento devem estar no futuro.
     */
    @NotNull(message = "A data e hora do agendamento são obrigatórias")
    @Future(message = "A data do agendamento deve ser no futuro")
    private LocalDateTime appointmentTime;

    /**
     * O status do agendamento (ex: "Scheduled", "Completed", "Canceled").
     */
    @NotNull(message = "O status é obrigatório")
    private String status;

    // Construtor padrão exigido pelo JPA
    public Appointment() {
    }

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
     * @Transient: Informa ao JPA para ignorar este método durante o mapeamento do banco de dados.
     * @return A data do agendamento.
     */
    @Transient
    public LocalDate getAppointmentDate() {
        return (this.appointmentTime != null) ? this.appointmentTime.toLocalDate() : null;
    }

    /**
     * Retorna apenas a parte da HORA do agendamento.
     * @Transient: Informa ao JPA para ignorar este método.
     * @return A hora do agendamento.
     */
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return (this.appointmentTime != null) ? this.appointmentTime.toLocalTime() : null;
    }
}