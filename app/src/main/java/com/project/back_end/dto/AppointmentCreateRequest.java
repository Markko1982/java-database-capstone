package com.project.back_end.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AppointmentCreateRequest {

    @NotNull(message = "doctorId é obrigatório.")
    private Long doctorId;

    @NotNull(message = "appointmentTime é obrigatório.")
    @FutureOrPresent(message = "appointmentTime deve ser no presente ou futuro.")
    private LocalDateTime appointmentTime;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
