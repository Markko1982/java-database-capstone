package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
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
    private final Service service;

    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // GET /appointments/{date}/{patientName}/{token}
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {
        // Apenas médicos podem acessar
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

        LocalDate targetDate = LocalDate.parse(date); // espera ISO: yyyy-MM-dd
        Map<String, Object> data = appointmentService.getAppointment(patientName, targetDate, token);
        return ResponseEntity.ok(data);
    }

    // GET /appointments/{date}/{patientName} (Authorization: Bearer <token>)
    @GetMapping("/{date}/{patientName}")
    public ResponseEntity<?> getAppointmentsBearer(@PathVariable String date,
            @PathVariable String patientName,
            @RequestAttribute("token") String token) {

        LocalDate targetDate = LocalDate.parse(date);
        Map<String, Object> data = appointmentService.getAppointment(patientName, targetDate, token);
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

    // POST /appointments/{token}
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(@PathVariable String token,
            @RequestBody Appointment appointment) {
        Map<String, String> body = new HashMap<>();

        // Apenas pacientes podem agendar
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

        // Valida disponibilidade
        int validation = service.validateAppointment(appointment);
        if (validation == -1) {
            body.put("message", "Médico não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        if (validation == 0) {
            body.put("message", "Horário indisponível para este médico.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        // Reserva
        int saved = appointmentService.bookAppointment(appointment, token);
        if (saved == 1) {
            body.put("message", "Agendamento criado com sucesso.");
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } else {
            body.put("message", "Erro ao criar agendamento.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    // PUT /appointments/{token}
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(@PathVariable String token,
            @RequestBody Appointment appointment) {
        // Apenas pacientes podem atualizar
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }
        return appointmentService.updateAppointment(appointment, token);
    }

    // DELETE /appointments/{id}/{token}
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable long id,
            @PathVariable String token) {
        // Apenas pacientes podem cancelar
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }
        return appointmentService.cancelAppointment(id, token);
    }
}
