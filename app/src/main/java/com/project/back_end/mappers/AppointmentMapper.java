package com.project.back_end.mappers;

import org.springframework.stereotype.Component;

import com.project.back_end.dto.AppointmentCreateRequest;
import com.project.back_end.models.Appointment;
import com.project.back_end.dto.AppointmentDTO;

@Component
public class AppointmentMapper {

    public Appointment fromCreateRequest(AppointmentCreateRequest request) {
        if (request == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(request.getAppointmentTime());
        return appointment;
    }

    public AppointmentDTO toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        var doctor = appointment.getDoctor();
        var patient = appointment.getPatient();

        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDoctorId(),
                doctor != null ? doctor.getName() : null,
                appointment.getPatientId(),
                patient != null ? patient.getName() : null,
                patient != null ? patient.getEmail() : null,
                patient != null ? patient.getPhone() : null,
                patient != null ? patient.getAddress() : null,
                appointment.getAppointmentTime(),
                appointment.getStatus() != null ? appointment.getStatus() : 0);
    }

}
