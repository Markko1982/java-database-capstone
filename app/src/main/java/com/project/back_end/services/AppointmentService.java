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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final Service service;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              TokenService tokenService,
                              Service service) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
        this.service = service;
    }

    // 1) Reservar
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 2) Atualizar
   
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment, String token) {
        Map<String, String> body = new HashMap<>();
        try {
            if (appointment == null || appointment.getId() == null) {
                body.put("message", "ID de agendamento é obrigatório.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
            }

            Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());
            if (existingOpt.isEmpty()) {
                body.put("message", "Agendamento não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }

            Appointment existing = existingOpt.get();

            // ✅ ownership: só o paciente dono pode atualizar
            String email = tokenService.getEmailFromToken(token);
            Patient requester = patientRepository.findByEmail(email);
            if (requester == null
                    || existing.getPatient() == null
                    || !Objects.equals(existing.getPatient().getId(), requester.getId())) {
                body.put("message", "Não autorizado a atualizar este agendamento.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }

            // ✅ força o patient correto mesmo se payload vier sem patient (ou tentar trocar)
            appointment.setPatient(existing.getPatient());

            int valid = service.validateAppointment(appointment);
            if (valid == -1) {
                body.put("message", "Médico inválido.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
            if (valid == 0) {
                body.put("message", "Horário indisponível.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            }

            appointmentRepository.save(appointment);
            body.put("message", "Agendamento atualizado.");
            return ResponseEntity.ok(body);

        } catch (Exception e) {
            body.put("message", "Erro ao atualizar agendamento.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }


    // 3) Cancelar
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> body = new HashMap<>();
        try {
            Optional<Appointment> apptOpt = appointmentRepository.findById(id);
            if (apptOpt.isEmpty()) {
                body.put("message", "Agendamento não encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }

            Appointment appt = apptOpt.get();
            String email = tokenService.getEmailFromToken(token);
            Patient requester = patientRepository.findByEmail(email);
            if (requester == null || appt.getPatient() == null || !Objects.equals(appt.getPatient().getId(), requester.getId())) {
                body.put("message", "Não autorizado a cancelar este agendamento.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }

            appointmentRepository.delete(appt);
            body.put("message", "Agendamento cancelado.");
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            body.put("message", "Erro ao cancelar agendamento.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    // 4) Buscar agendamentos para um médico em uma data (filtrando por paciente opcional)
    public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
        Map<String, Object> res = new HashMap<>();
        try {
            String email = tokenService.getEmailFromToken(token);
            Doctor doc = doctorRepository.findByEmail(email);
            if (doc == null) {
                res.put("message", "Médico não encontrado.");
                return res;
            }

            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

            List<Appointment> list;
            if (pname != null && !pname.isBlank()) {
                list = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                        doc.getId(), pname.trim(), start, end);
            } else {
                list = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doc.getId(), start, end);
            }

            List<AppointmentDTO> dtos = new ArrayList<>();
            for (Appointment a : list) {
                Long patientId = a.getPatient() != null ? a.getPatient().getId() : null;
                String patientName = a.getPatient() != null ? a.getPatient().getName() : null;
                String patientEmail = a.getPatient() != null ? a.getPatient().getEmail() : null;
                String patientPhone = a.getPatient() != null ? a.getPatient().getPhone() : null;
                String patientAddress = a.getPatient() != null ? a.getPatient().getAddress() : null;

                String doctorName = a.getDoctor() != null ? a.getDoctor().getName() : null;

                AppointmentDTO dto = new AppointmentDTO(
                        a.getId(),
                        a.getDoctorId(),
                        doctorName,
                        patientId,
                        patientName,
                        patientEmail,
                        patientPhone,
                        patientAddress,
                        a.getAppointmentTime(),
                        // LINHA CORRIGIDA ABAIXO
                        (a.getStatus() != null ? a.getStatus() : 0)
                );
                dtos.add(dto);
            }
            res.put("appointments", dtos);
            return res;
        } catch (Exception e) {
            res.put("message", "Erro ao buscar agendamentos.");
            return res;
        }
    }
}