package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.jpa.AppointmentRepository;
import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;
    private final com.project.back_end.services.Service service;

    public AppointmentService(AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService,
            com.project.back_end.services.Service service) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
        this.service = service;
    }

    // 1) Reservar (seguro: paciente vem do token)
    public int bookAppointment(Appointment appointment, String token) {
        try {
            String email = tokenService.getEmailFromToken(token);
            if (email == null)
                return -1;

            Patient patient = patientRepository.findByEmail(email);
            if (patient == null)
                return -1;

            // ignora qualquer patient vindo do payload (evita overposting)
            appointment.setPatient(patient);

            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public void bookAppointmentOrThrow(Appointment appointment, String token, Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("doctorId é obrigatório.");
        }

        var doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Médico não encontrado."));

        // monta o mínimo pra validação e persistência
        appointment.setDoctor(doctor);

        // reutiliza regra existente
        bookAppointmentOrThrow(appointment, token);
    }

    public void bookAppointmentOrThrow(Appointment appointment, String token) {
        if (appointment == null) {
            throw new IllegalArgumentException("Agendamento é obrigatório.");
        }

        int validation = service.validateAppointment(appointment);
        if (validation == -1) {
            throw new jakarta.persistence.EntityNotFoundException("Médico não encontrado.");
        }
        if (validation == 0) {
            throw new IllegalArgumentException("Horário indisponível para este médico.");
        }

        int saved = bookAppointment(appointment, token);
        if (saved != 1) {
            throw new RuntimeException("Erro ao criar agendamento.");
        }
    }

    // 2) Atualizar (NOVO PADRÃO)
    public void updateAppointmentOrThrow(Appointment appointment, String token) {
        if (appointment == null || appointment.getId() == null) {
            throw new IllegalArgumentException("ID de agendamento é obrigatório.");
        }

        Appointment existing = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Agendamento não encontrado."));

        // ownership: só o paciente dono pode atualizar
        String email = tokenService.getEmailFromToken(token);
        Patient requester = patientRepository.findByEmail(email);

        if (requester == null
                || existing.getPatient() == null
                || !Objects.equals(existing.getPatient().getId(), requester.getId())) {
            throw new SecurityException("Acesso negado.");
        }

        // força o patient correto mesmo se payload vier sem patient (ou tentar trocar)
        appointment.setPatient(existing.getPatient());

        int valid = service.validateAppointment(appointment);
        if (valid == -1) {
            throw new jakarta.persistence.EntityNotFoundException("Médico inválido.");
        }
        if (valid == 0) {
            throw new IllegalArgumentException("Horário indisponível.");
        }

        appointmentRepository.save(appointment);
    }

    public void cancelAppointmentOrThrow(long id, String token) {
        Appointment appt = appointmentRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Agendamento não encontrado."));

        String email = tokenService.getEmailFromToken(token);
        Patient requester = patientRepository.findByEmail(email);

        if (requester == null
                || appt.getPatient() == null
                || !Objects.equals(appt.getPatient().getId(), requester.getId())) {
            throw new SecurityException("Acesso negado.");
        }

        appointmentRepository.delete(appt);
    }

    public Map<String, Object> getAppointmentOrThrow(String pname, LocalDate date, String token) {
        String email = tokenService.getEmailFromToken(token);
        Doctor doc = doctorRepository.findByEmail(email);
        if (doc == null) {
            throw new jakarta.persistence.EntityNotFoundException("Médico não encontrado.");
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
                    (a.getStatus() != null ? a.getStatus() : 0));
            dtos.add(dto);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("appointments", dtos);
        return res;
    }

}
