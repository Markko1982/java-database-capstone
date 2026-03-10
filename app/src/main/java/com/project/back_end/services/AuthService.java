package com.project.back_end.services;

import com.project.back_end.dto.ApiAuthResponse;
import com.project.back_end.dto.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.jpa.AdminRepository;
import com.project.back_end.repo.jpa.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class AuthService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;

    public AuthService(TokenService tokenService,
            AdminRepository adminRepository,
            PatientRepository patientRepository) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> body = new HashMap<>();
        try {
            boolean valid = tokenService.validateToken(token, user);
            if (!valid) {
                body.put("message", "Token inválido ou expirado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }
            body.put("message", "Token válido.");
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            body.put("message", "Erro ao validar token.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    public ResponseEntity<ApiAuthResponse> validateAdmin(Admin receivedAdmin) {
        if (receivedAdmin == null || receivedAdmin.getUsername() == null || receivedAdmin.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiAuthResponse(null, "Credenciais inválidas."));
        }

        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin == null || !receivedAdmin.getPassword().equals(admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiAuthResponse(null, "Usuário ou senha inválidos."));
        }

        try {
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(new ApiAuthResponse(token, "Login realizado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiAuthResponse(null, "Erro ao gerar token."));
        }
    }

    public ResponseEntity<ApiAuthResponse> validatePatientLogin(Login login) {
        if (login == null || login.getIdentifier() == null || login.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiAuthResponse(null, "Credenciais inválidas."));
        }

        Patient p = patientRepository.findByEmail(login.getIdentifier());
        if (p == null || !login.getPassword().equals(p.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiAuthResponse(null, "E-mail ou senha inválidos."));
        }

        try {
            String token = tokenService.generateToken(p.getEmail());
            return ResponseEntity.ok(new ApiAuthResponse(token, "Login realizado com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiAuthResponse(null, "Erro ao gerar token."));
        }
    }
}