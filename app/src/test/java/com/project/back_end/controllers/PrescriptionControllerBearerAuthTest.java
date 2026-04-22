package com.project.back_end.controllers;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.project.back_end.services.AuthService;
import com.project.back_end.services.PrescriptionService;

@ActiveProfiles("test")
@SpringBootTest(properties = "api.path=/api/")
@AutoConfigureMockMvc(addFilters = true)
class PrescriptionControllerBearerAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    void postPrescription_quandoControllerRejeitaBearer_deveRetornar401ESemSalvar() throws Exception {
        when(authService.validateToken("token-teste", "doctor"))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Token válido.")))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token inválido ou expirado.")));

        mockMvc.perform(post("/api/prescription")
                .header("Authorization", "Bearer token-teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "appointmentId": 1,
                          "patientName": "Maria",
                          "medication": "Dipirona",
                          "dosage": "500mg",
                          "doctorNotes": "Tomar após o almoço"
                        }
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token inválido ou expirado."));

        verifyNoInteractions(prescriptionService);
    }

    @Test
    void getPrescription_quandoControllerRejeitaBearer_deveRetornar401ESemConsultarServico() throws Exception {
        when(authService.validateToken("token-teste", "doctor"))
                .thenReturn(ResponseEntity.ok(Map.of("message", "Token válido.")))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token inválido ou expirado.")));

        mockMvc.perform(get("/api/prescription/1")
                .header("Authorization", "Bearer token-teste"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Token inválido ou expirado."));

        verifyNoInteractions(prescriptionService);
    }
}