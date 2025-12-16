package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.jpa.AppointmentRepository;
import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          DoctorRepository doctorRepository,
                          TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    /** 1) Criar paciente */
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /** 2) Consultas do paciente (valida o token e compara o ID) */
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        Map<String, Object> res = new HashMap<>();
        try {
            String email = tokenService.getEmailFromToken(token);
            Patient pFromToken = patientRepository.findByEmail(email);
            if (pFromToken == null || !Objects.equals(pFromToken.getId(), id)) {
                res.put("message", "Não autorizado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            // LINHA CORRIGIDA ABAIXO
            List<Appointment> appts = appointmentRepository.findByPatient_Id(id);
            
            res.put("appointments", toDTOs(appts));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao buscar consultas.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    /** 3) Filtra por condição (past/future) */
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            int status = "past".equalsIgnoreCase(condition) ? 1 : 0;
            List<Appointment> appts =
                    appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status);
            res.put("appointments", toDTOs(appts));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao filtrar consultas.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    /** 4) Filtra por nome do médico */
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<Appointment> appts =
                    appointmentRepository.filterByDoctorNameAndPatientId(name, patientId);
            res.put("appointments", toDTOs(appts));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao filtrar por médico.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    /** 5) Filtra por condição e nome do médico */
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, long patientId) {
        Map<String, Object> res = new HashMap<>();
        try {
            int status = "past".equalsIgnoreCase(condition) ? 1 : 0;
            List<Appointment> appts =
                    appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(name, patientId, status);
            res.put("appointments", toDTOs(appts));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao filtrar consultas.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    /** 6) Detalhes do paciente a partir do token */
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        Map<String, Object> res = new HashMap<>();
        try {
            String email = tokenService.getEmailFromToken(token);
            Patient p = patientRepository.findByEmail(email);
            if (p == null) {
                res.put("message", "Paciente não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }

            // evita expor senha
            Map<String, Object> sanitized = new HashMap<>();
            sanitized.put("id", p.getId());
            sanitized.put("name", p.getName());
            sanitized.put("email", p.getEmail());
            sanitized.put("phone", p.getPhone());
            sanitized.put("address", p.getAddress());

            res.put("patient", sanitized);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao obter detalhes do paciente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    /* --------------------- helpers --------------------- */

    private List<AppointmentDTO> toDTOs(List<Appointment> appts) {
        if (appts == null) return Collections.emptyList();
        return appts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private AppointmentDTO toDTO(Appointment a) {
        Doctor d = a.getDoctor();
        Patient p = a.getPatient();

        Long doctorId = (d != null ? d.getId() : null);
        String doctorName = (d != null ? d.getName() : null);

        Long patientId = (p != null ? p.getId() : null);
        String patientName = (p != null ? p.getName() : null);
        String patientEmail = (p != null ? p.getEmail() : null);
        String patientPhone = (p != null ? p.getPhone() : null);
        String patientAddress = (p != null ? p.getAddress() : null);
        
        int statusInt = (a.getStatus() != null) ? a.getStatus() : 0;

        return new AppointmentDTO(
                a.getId(),
                doctorId,
                doctorName,
                patientId,
                patientName,
                patientEmail,
                patientPhone,
                patientAddress,
                a.getAppointmentTime(),
                statusInt
        );
    }
}