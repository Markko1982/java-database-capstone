package com.project.back_end.mappers;

import org.springframework.stereotype.Component;

import com.project.back_end.dto.AppointmentCreateRequest;
import com.project.back_end.models.Appointment;

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
}
