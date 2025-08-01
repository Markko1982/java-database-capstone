package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class DoctorAvailableTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "O campo day_of_week é obrigatório")
    private String dayOfWeek; // Ex: "Segunda", "Terça", etc.

    @NotNull(message = "O campo start_time é obrigatório")
    private String startTime; // Ex: "09:00"

    @NotNull(message = "O campo end_time é obrigatório")
    private String endTime;   // Ex: "12:00"

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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
