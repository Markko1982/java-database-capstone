package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.jpa.AdminRepository;
import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
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

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> body = new HashMap<>();
        if (receivedAdmin == null || receivedAdmin.getUsername() == null || receivedAdmin.getPassword() == null) {
            body.put("message", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
        if (admin == null || !receivedAdmin.getPassword().equals(admin.getPassword())) {
            body.put("message", "Usuário ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        try {
            String token = tokenService.generateToken(admin.getUsername());
            Map<String, String> ok = new HashMap<>();
            ok.put("token", token);
            ok.put("message", "Login realizado com sucesso.");
            return ResponseEntity.ok(ok);
        } catch (Exception e) {
            body.put("message", "Erro ao gerar token.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        String n = name == null ? "" : name.trim();
        String s = specialty == null ? "" : specialty.trim();
        String t = time == null ? "" : time.trim().toUpperCase();

        if (!n.isEmpty() && !s.isEmpty() && !t.isEmpty()) return doctorService.filterDoctorsByNameSpecilityandTime(n, s, t);
        if (!n.isEmpty() && !t.isEmpty()) return doctorService.filterDoctorByNameAndTime(n, t);
        if (!n.isEmpty() && !s.isEmpty()) return doctorService.filterDoctorByNameAndSpecility(n, s);
        if (!s.isEmpty() && !t.isEmpty()) return doctorService.filterDoctorByTimeAndSpecility(s, t);
        if (!s.isEmpty()) return doctorService.filterDoctorBySpecility(s);
        if (!t.isEmpty()) return doctorService.filterDoctorsByTime(t);

        Map<String, Object> res = new HashMap<>();
        res.put("doctors", doctorService.getDoctors());
        return res;
    }

    public int validateAppointment(Appointment appointment) {
        if (appointment == null || appointment.getDoctorId() == null || appointment.getAppointmentTime() == null) {
            return 0;
        }
        Optional<Doctor> optDoc = doctorRepository.findById(appointment.getDoctorId());
        if (optDoc.isEmpty()) return -1;

        LocalDate date = appointment.getAppointmentTime().toLocalDate();
        List<String> available = doctorService.getDoctorAvailability(appointment.getDoctorId(), date);
        if (available == null || available.isEmpty()) return 0;

        String desired = appointment.getAppointmentTime()
                .toLocalTime().withSecond(0).withNano(0).toString().substring(0, 5);
        return available.contains(desired) ? 1 : 0;
    }

    public boolean validatePatient(Patient patient) {
        if (patient == null) return false;
        String email = patient.getEmail();
        String phone = patient.getPhone();
        Patient existing = patientRepository.findByEmailOrPhone(email, phone);
        return existing == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> body = new HashMap<>();
        if (login == null || login.getIdentifier() == null || login.getPassword() == null) {
            body.put("message", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        Patient p = patientRepository.findByEmail(login.getIdentifier());
        if (p == null || !login.getPassword().equals(p.getPassword())) {
            body.put("message", "E-mail ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }

        try {
            String token = tokenService.generateToken(p.getEmail());
            Map<String, String> ok = new HashMap<>();
            ok.put("token", token);
            ok.put("message", "Login realizado com sucesso.");
            return ResponseEntity.ok(ok);
        } catch (Exception e) {
            body.put("message", "Erro ao gerar token.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        try {
            String email = tokenService.getEmailFromToken(token);
            if (email == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Token inválido ou expirado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
            }
            Patient p = patientRepository.findByEmail(email);
            if (p == null) {
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Paciente não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
            }

            if (condition != null && !condition.isBlank() && name != null && !name.isBlank())
                return patientService.filterByDoctorAndCondition(condition, name, p.getId());
            if (condition != null && !condition.isBlank())
                return patientService.filterByCondition(condition, p.getId());
            if (name != null && !name.isBlank())
                return patientService.filterByDoctor(name, p.getId());

            return patientService.getPatientAppointment(p.getId(), token);

        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Erro ao filtrar agendamentos do paciente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }
}
