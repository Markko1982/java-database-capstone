package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret:change-me-in-application-properties}")
    private String secret;

    private static final long EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public String generateToken(String identifier) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(identifier)
                .issuedAt(now)
                .expiration(exp)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractIdentifier(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(sanitize(token))
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token);
            String type = (user == null ? "" : user.trim().toLowerCase(Locale.ROOT));

            switch (type) {
                case "admin":
                    return adminRepository.findByUsername(identifier) != null;
                case "doctor":
                case "doutor":
                    return doctorRepository.findByEmail(identifier) != null;
                case "patient":
                case "paciente":
                    return patientRepository.findByEmail(identifier) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String getEmailFromToken(String token) {
        return extractIdentifier(token);
    }

    public String getUsernameFromToken(String token) {
        return extractIdentifier(token);
    }

    private String sanitize(String token) {
        if (token == null) return "";
        String t = token.trim();
        if (t.toLowerCase(Locale.ROOT).startsWith("bearer ")) {
            return t.substring(7).trim();
        }
        return t;
    }
}
