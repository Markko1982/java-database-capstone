package com.project.back_end.controllers;

import com.project.back_end.dto.ApiMessageResponse;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AuthService;
import com.project.back_end.services.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AuthService authService;

    public PrescriptionController(PrescriptionService prescriptionService, AuthService authService) {
        this.prescriptionService = prescriptionService;
        this.authService = authService;
    }

    // 1) Salvar prescrição (somente médico) (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> savePrescriptionBearer(@RequestBody Prescription prescription) {
        return prescriptionService.savePrescription(prescription);
    }

    // 1) Salvar prescrição (somente médico)
    /**
     * @deprecated Use POST /prescription (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @PostMapping("/{token}")
    public ResponseEntity<?> savePrescription(@PathVariable String token,
            @RequestBody Prescription prescription) {
        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(tokenCheck.getStatusCode())
                    .body(new ApiMessageResponse(tokenCheck.getBody().get("message")));
        }

        return prescriptionService.savePrescription(prescription);
    }

    // 2) Obter prescrição por appointmentId (somente médico) (Authorization: Bearer
    // <token>)
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getPrescriptionBearer(@PathVariable Long appointmentId) {
        return prescriptionService.getPrescription(appointmentId);
    }

    // 2) Obter prescrição por appointmentId (somente médico)
    /**
     * @deprecated Use GET /prescription/{appointmentId} (Authorization: Bearer
     *             <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "doctor");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(tokenCheck.getStatusCode()).body((Map) tokenCheck.getBody());
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}