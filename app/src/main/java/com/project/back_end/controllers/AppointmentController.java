package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
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
    public ResponseEntity<Map<String, String>> bookAppointmentBearer(
            @RequestAttribute("token") String token,
            @RequestBody Appointment appointment) {

        appointmentService.bookAppointmentOrThrow(appointment, token);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Agendamento criado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // PUT /appointments (Authorization: Bearer <token>)
    @PutMapping
    public ResponseEntity<Map<String, String>> updateAppointmentBearer(
            @RequestAttribute("token") String token,
            @RequestBody Appointment appointment) {

        appointmentService.updateAppointmentOrThrow(appointment, token);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Agendamento atualizado.");
        return ResponseEntity.ok(body);
    }

    // DELETE /appointments/{id} (Authorization: Bearer <token>)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelAppointmentBearer(
            @PathVariable long id,
            @RequestAttribute("token") String token) {

        appointmentService.cancelAppointmentOrThrow(id, token);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Agendamento cancelado.");
        return ResponseEntity.ok(body);
    }

}
