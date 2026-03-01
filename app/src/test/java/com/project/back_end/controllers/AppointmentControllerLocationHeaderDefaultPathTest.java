package com.project.back_end.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.back_end.dto.AppointmentCreateRequest;
import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.mappers.AppointmentMapper;
import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;

@ActiveProfiles("test")
@SpringBootTest(properties = "api.path=/")
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerLocationHeaderDefaultPathTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentMapper appointmentMapper;

    @Test
    void postAppointments_deveRetornarLocationSemPrefixoQuandoApiPathBarra() throws Exception {
        Long id = 123L;
        Long doctorId = 10L;
        LocalDateTime when = LocalDateTime.now().plusDays(1);

        AppointmentCreateRequest request = new AppointmentCreateRequest();
        request.setDoctorId(doctorId);
        request.setAppointmentTime(when);

        Appointment mapped = new Appointment();
        Appointment saved = new Appointment();
        saved.setId(id);

        when(appointmentMapper.fromCreateRequest(any(AppointmentCreateRequest.class))).thenReturn(mapped);
        when(appointmentService.bookAppointmentOrThrow(same(mapped), eq("token"), eq(doctorId))).thenReturn(saved);
        when(appointmentMapper.toDto(saved)).thenReturn(new AppointmentDTO(
                id, doctorId, null, null, null, null, null, null, when, 0));

        mockMvc.perform(post("/appointments")
                .requestAttr("token", "token")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/appointments/" + id));
    }
}
