package com.project.back_end.services;

import com.project.back_end.dto.ApiAuthResponse;
import com.project.back_end.dto.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.jpa.AdminRepository;
import com.project.back_end.exceptions.UnauthorizedException;
import com.project.back_end.repo.jpa.PatientRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public ApiAuthResponse validateAdmin(Admin receivedAdmin) {
        if (receivedAdmin == null || receivedAdmin.getUsername() == null || receivedAdmin.getPassword() == null) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin == null || !receivedAdmin.getPassword().equals(admin.getPassword())) {
            throw new UnauthorizedException("Usuário ou senha inválidos.");
        }

        String token = tokenService.generateToken(admin.getUsername());
        return new ApiAuthResponse(token, "Login realizado com sucesso.");
    }

    public ApiAuthResponse validatePatientLogin(Login login) {
        if (login == null || login.getIdentifier() == null || login.getPassword() == null) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        Patient p = patientRepository.findByEmail(login.getIdentifier());
        if (p == null || !login.getPassword().equals(p.getPassword())) {
            throw new UnauthorizedException("E-mail ou senha inválidos.");
        }

        String token = tokenService.generateToken(p.getEmail());
        return new ApiAuthResponse(token, "Login realizado com sucesso.");
    }
}