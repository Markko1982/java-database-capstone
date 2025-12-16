package com.project.back_end.controllers;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service = service;
    }

    // 1) Obter detalhes do paciente
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatientDetails(@PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) return tokenCheck;

        return patientService.getPatientDetails(token);
    }

    // 2) Criar novo paciente
    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        Map<String, String> body = new HashMap<>();

        boolean okToCreate = service.validatePatient(patient);
        if (!okToCreate) {
            body.put("message", "Paciente com e-mail ou número de telefone já existe");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        int res = patientService.createPatient(patient);
        if (res == 1) {
            body.put("message", "Cadastro bem-sucedido");
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } else {
            body.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    // 3) Login do paciente
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> patientLogin(@RequestBody Login login) {
        return service.validatePatientLogin(login);
    }

    // 4) Obter consultas do paciente
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable Long id,
                                             @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) return tokenCheck;

        return patientService.getPatientAppointment(id, token);
    }

    // 5) Filtrar consultas do paciente
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterAppointments(@PathVariable String condition,
                                                @PathVariable String name,
                                                @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) return tokenCheck;

        return service.filterPatient(condition, name, token);
    }
}
