package com.project.back_end.security;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.project.back_end.services.AuthService;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.DoctorService;

@ActiveProfiles("test")
@SpringBootTest(properties = "api.path=/api/")
@AutoConfigureMockMvc(addFilters = true)
class AuthTokenFilterUnauthorizedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private DoctorService doctorService;

    @Test
    void getPatient_semAuthorization_deveRetornar401ComApiErrorPadronizado() throws Exception {
        mockMvc.perform(get("/api/patient"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Token ausente ou inválido. Use Authorization: Bearer <token>."))
                .andExpect(jsonPath("$.path").value("/api/patient"));

        verifyNoInteractions(authService, patientService);
    }

    @Test
    void getPatient_comTokenInvalido_deveRetornar401ComMensagemDoAuthService() throws Exception {
        when(authService.validateToken("token-invalido", "patient"))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token inválido ou expirado")));

        mockMvc.perform(get("/api/patient")
                .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Token inválido ou expirado"))
                .andExpect(jsonPath("$.path").value("/api/patient"));

        verifyNoInteractions(patientService);
    }

    @Test
    void getDoctorAvailability_comTokenInvalido_deveRetornar401SemChamarServico() throws Exception {
        when(authService.validateToken("token-invalido", "doctor"))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Token inválido ou expirado")));

        mockMvc.perform(get("/api/doctor/availability/doctor/1/2026-04-22")
                .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Token inválido ou expirado"))
                .andExpect(jsonPath("$.path").value("/api/doctor/availability/doctor/1/2026-04-22"));

        verifyNoInteractions(doctorService);
    }
}