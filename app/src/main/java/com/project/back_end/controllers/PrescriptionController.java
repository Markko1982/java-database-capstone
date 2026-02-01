package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;

    public PrescriptionController(PrescriptionService prescriptionService, Service service) {
        this.prescriptionService = prescriptionService;
        this.service = service;
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null)
            return null;

        // aceita "Bearer " com qualquer casing (Bearer/bearer/BEARER)
        if (authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = authorization.substring(7).trim();
            return token.isEmpty() ? null : token;
        }
        return null;
    }

    private ResponseEntity<Map<String, String>> unauthorized(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // 1) Salvar prescrição (somente médico) (Authorization: Bearer <token>)
    @PostMapping
    public ResponseEntity<Map<String, String>> savePrescriptionBearer(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Prescription prescription) {

        String token = extractBearerToken(authorization);
        if (token == null) {
            return unauthorized("Token ausente ou inválido. Use Authorization: Bearer <token>.");
        }

        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful())
            return tokenCheck;

        return prescriptionService.savePrescription(prescription);
    }

    // 1) Salvar prescrição (somente médico)
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(@PathVariable String token,
            @RequestBody Prescription prescription) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful())
            return tokenCheck;

        return prescriptionService.savePrescription(prescription);
    }

    // 2) Obter prescrição por appointmentId (somente médico) (Authorization: Bearer
    // <token>)
    @GetMapping("/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionBearer(
            @PathVariable Long appointmentId,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        String token = extractBearerToken(authorization);
        if (token == null) {
            // mantém o tipo do endpoint (Map<String, Object>)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message", "Token ausente ou inválido. Use Authorization: Bearer <token>."));
        }

        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(tokenCheck.getStatusCode()).body((Map) tokenCheck.getBody());
        }

        return prescriptionService.getPrescription(appointmentId);
    }

    // 2) Obter prescrição por appointmentId (somente médico)
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            // Faz o cast do corpo para o tipo esperado pelo endpoint
            return ResponseEntity.status(tokenCheck.getStatusCode()).body((Map) tokenCheck.getBody());
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}
