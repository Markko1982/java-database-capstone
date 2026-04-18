package com.project.back_end.controllers;

import com.project.back_end.dto.ApiAuthResponse;
import com.project.back_end.dto.ApiMessageResponse;
import com.project.back_end.dto.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AuthService;
import com.project.back_end.services.PatientService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "patient")
public class PatientController {

    private final PatientService patientService;
    private final AuthService authService;

    public PatientController(PatientService patientService, AuthService authService) {
        this.patientService = patientService;
        this.authService = authService;
    }

    // 1) Obter detalhes do paciente (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> getPatientDetailsBearer(@RequestAttribute("token") String token) {
        Map<String, Object> patient = patientService.getPatientDetails(token);
        return ResponseEntity.ok(patient);
    }

    /**
     * @deprecated Use GET /patient (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatientDetails(@PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

        return ResponseEntity.ok(patientService.getPatientDetails(token));
    }

    // 2) Criar novo paciente
    @PostMapping
    public ResponseEntity<ApiMessageResponse> createPatient(@Valid @RequestBody Patient patient) {
        boolean okToCreate = patientService.validatePatient(patient);
        if (!okToCreate) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiMessageResponse("Paciente com e-mail ou número de telefone já existe"));
        }

        patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiMessageResponse("Cadastro bem-sucedido"));
    }

    // 3) Login do paciente
    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> patientLogin(@Valid @RequestBody Login login) {
        return ResponseEntity.ok(authService.validatePatientLogin(login));
    }

    // 4) Obter consultas do paciente (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}/appointments")
    public ResponseEntity<?> getAppointmentsBearer(@PathVariable Long id,
            @RequestAttribute("token") String token) {
        return patientService.getPatientAppointment(id, token);
    }

    // 4) Obter consultas do paciente
    /**
     * @deprecated Use GET /patient/{id}/appointments (Authorization: Bearer
     *             <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/{id}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable Long id,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

        return patientService.getPatientAppointment(id, token);
    }

    // 5) Consultas do paciente com filtros via query params (REST)
    // GET /patient/appointments?condition=past&doctor=Joao
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/appointments")
    public ResponseEntity<?> listAppointments(
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String doctor,
            @RequestAttribute("token") String token) {
        return patientService.filterPatient(condition, doctor, token);
    }

    /**
     * @deprecated Use GET /patient/appointments?condition=...&doctor=...
     *             (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/filter/{condition}/{name}")
    public ResponseEntity<?> filterAppointmentsBearer(@PathVariable String condition,
            @PathVariable String name,
            @RequestAttribute("token") String token) {
        return patientService.filterPatient(condition, name, token);
    }

    /**
     * @deprecated Use GET /patient/appointments?condition=...&doctor=...
     *             (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterAppointments(@PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "patient");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

        return patientService.filterPatient(condition, name, token);
    }
}