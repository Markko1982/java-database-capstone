package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.dto.AppointmentCreateRequest;
import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;

import jakarta.validation.Valid;

import com.project.back_end.mappers.AppointmentMapper;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    // GET /appointments/{date}/{patientName} (Authorization: Bearer <token>)
    @GetMapping("/{date}/{patientName}")
    public ResponseEntity<?> getAppointmentsBearer(@PathVariable String date,
            @PathVariable String patientName,
            @RequestAttribute("token") String token) {

        LocalDate targetDate = LocalDate.parse(date);
        Map<String, Object> data = appointmentService.getAppointmentOrThrow(patientName, targetDate, token);
        return ResponseEntity.ok(data);
    }

    // POST /appointments (Authorization: Bearer <token>)
    @PostMapping
    public ResponseEntity<Void> bookAppointmentBearer(
            @RequestAttribute("token") String token,
            @Valid @RequestBody AppointmentCreateRequest request) {

        Appointment appointment = appointmentMapper.fromCreateRequest(request);

        appointmentService.bookAppointmentOrThrow(appointment, token, request.getDoctorId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // PUT /appointments (Authorization: Bearer <token>)
    @PutMapping
    public ResponseEntity<Void> updateAppointmentBearer(
            @RequestAttribute("token") String token,
            @RequestBody Appointment appointment) {

        appointmentService.updateAppointmentOrThrow(appointment, token);
        return ResponseEntity.ok().build();
    }

    // DELETE /appointments/{id} (Authorization: Bearer <token>)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointmentBearer(
            @PathVariable long id,
            @RequestAttribute("token") String token) {

        appointmentService.cancelAppointmentOrThrow(id, token);
        return ResponseEntity.noContent().build();
    }

}
