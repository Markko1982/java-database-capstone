package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Para o médico, por intervalo de tempo
    @Query("""
            SELECT a FROM Appointment a
            LEFT JOIN FETCH a.doctor d
            LEFT JOIN FETCH a.patient p
            WHERE a.doctor.id = :doctorId
              AND a.appointmentTime BETWEEN :start AND :end
            ORDER BY a.appointmentTime ASC
            """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // Para o médico + nome parcial do paciente + intervalo
    @Query("""
            SELECT a FROM Appointment a
            LEFT JOIN FETCH a.doctor d
            LEFT JOIN FETCH a.patient p
            WHERE a.doctor.id = :doctorId
              AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
              AND a.appointmentTime BETWEEN :start AND :end
            ORDER BY a.appointmentTime ASC
            """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);

    // Excluir todos por médico
    @Modifying
    @Transactional
    void deleteAllByDoctor_Id(Long doctorId); // <-- PRIMEIRA CORREÇÃO

    // Todos por paciente
    List<Appointment> findByPatient_Id(Long patientId); // <-- SEGUNDA CORREÇÃO

    // Por paciente + status, ordenado por data/hora ASC  (USADO NO PatientService)
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

    // Filtro por nome do médico + paciente
    @Query("""
            SELECT a FROM Appointment a
            JOIN a.doctor d
            WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
              AND a.patient.id = :patientId
            ORDER BY a.appointmentTime ASC
            """)
    List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

    // Filtro por nome do médico + paciente + status
    @Query("""
            SELECT a FROM Appointment a
            JOIN a.doctor d
            WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
              AND a.patient.id = :patientId
              AND a.status = :status
            ORDER BY a.appointmentTime ASC
            """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);
}