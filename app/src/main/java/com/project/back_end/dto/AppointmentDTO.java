package com.project.back_end.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de Consulta para trafegar dados entre backend e frontend.
 * Não contém anotações de persistência.
 */
public class AppointmentDTO {

    // Campos principais
    private final Long id;
    private final Long doctorId;
    private final String doctorName;
    private final Long patientId;
    private final String patientName;
    private final String patientEmail;
    private final String patientPhone;
    private final String patientAddress;
    private final LocalDateTime appointmentTime;
    private final int status;

    // Campos derivados
    private final LocalDate appointmentDate;     // extraído de appointmentTime
    private final LocalTime appointmentTimeOnly; // extraído de appointmentTime
    private final LocalDateTime endTime;         // appointmentTime + 1 hora

    /**
     * Construtor completo que calcula os campos derivados.
     */
    public AppointmentDTO(
            Long id,
            Long doctorId,
            String doctorName,
            Long patientId,
            String patientName,
            String patientEmail,
            String patientPhone,
            String patientAddress,
            LocalDateTime appointmentTime,
            int status
    ) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Derivados
        this.appointmentDate = (appointmentTime != null) ? appointmentTime.toLocalDate() : null;
        this.appointmentTimeOnly = (appointmentTime != null) ? appointmentTime.toLocalTime() : null;
        this.endTime = (appointmentTime != null) ? appointmentTime.plusHours(1) : null;
    }

    // Construtor sem argumentos (útil para desserialização, se necessário)
    public AppointmentDTO() {
        this(null, null, null, null, null, null, null, null, null, 0);
    }

    // Getters (necessários para serialização em respostas da API)
    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
