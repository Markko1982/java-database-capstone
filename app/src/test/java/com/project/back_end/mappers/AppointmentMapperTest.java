package com.project.back_end.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.project.back_end.dto.AppointmentCreateRequest;
import com.project.back_end.models.Appointment;

class AppointmentMapperTest {

    private final AppointmentMapper mapper = new AppointmentMapper();

    @Test
    void fromCreateRequest_deveMapearAppointmentTime() {
        AppointmentCreateRequest req = new AppointmentCreateRequest();
        LocalDateTime when = LocalDateTime.now().plusDays(1);

        req.setDoctorId(10L);
        req.setAppointmentTime(when);

        Appointment appointment = mapper.fromCreateRequest(req);

        assertNotNull(appointment);
        assertEquals(when, appointment.getAppointmentTime());
    }

    @Test
    void fromCreateRequest_quandoRequestNull_deveRetornarNull() {
        assertNull(mapper.fromCreateRequest(null));
    }
}
