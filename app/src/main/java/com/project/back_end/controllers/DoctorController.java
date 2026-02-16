package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    private ResponseEntity<Map<String, String>> unauthorized(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // 1) Disponibilidade do médico (Authorization: Bearer <token>)
    @GetMapping("/availability/{user}/{doctorId}/{date}")
    public ResponseEntity<?> getAvailabilityBearer(@PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @RequestAttribute("token") String token) {

        LocalDate target = LocalDate.parse(date); // yyyy-MM-dd
        List<String> slots = doctorService.getDoctorAvailability(doctorId, target);

        Map<String, Object> body = new HashMap<>();
        body.put("availability", slots);
        return ResponseEntity.ok(body);
    }

    // 1) Disponibilidade do médico
    @Hidden
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getAvailability(@PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, user);
        if (!tokenCheck.getStatusCode().is2xxSuccessful())
            return tokenCheck;

        LocalDate target = LocalDate.parse(date); // yyyy-MM-dd
        List<String> slots = doctorService.getDoctorAvailability(doctorId, target);

        Map<String, Object> body = new HashMap<>();
        body.put("availability", slots);
        return ResponseEntity.ok(body);
    }

    // 2) Lista de médicos
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors() {
        Map<String, Object> body = new HashMap<>();
        body.put("doctors", doctorService.getDoctors());
        return ResponseEntity.ok(body);
    }

    // 3) Adicionar novo médico (somente admin) (Authorization: Bearer <token>)

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<Map<String, String>> addDoctorBearer(
            @RequestAttribute("token") String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.saveDoctorOrThrow(doctor);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Médico cadastrado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 3) Adicionar novo médico (somente admin)
    @Hidden
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(@PathVariable String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.saveDoctorOrThrow(doctor);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Médico cadastrado com sucesso.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 4) Login do médico
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(@Valid @RequestBody Login login) {
        return doctorService.validateDoctor(login);
    }

    // 5) Atualizar médico (somente admin) (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public ResponseEntity<Map<String, String>> updateDoctorBearer(
            @RequestAttribute("token") String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.updateDoctorOrThrow(doctor);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Médico atualizado");
        return ResponseEntity.ok(body);
    }

    // 5) Atualizar médico (somente admin)
    @Hidden
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(@PathVariable String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.updateDoctorOrThrow(doctor);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Médico atualizado");
        return ResponseEntity.ok(body);
    }

    // 6) Excluir médico (somente admin) (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDoctorBearer(
            @PathVariable Long id,
            @RequestAttribute("token") String token) {

        doctorService.deleteDoctorOrThrow(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Médico excluído com sucesso");
        return ResponseEntity.ok(body);
    }

    // 6) Excluir médico (somente admin)
    @Hidden
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable long id,
            @PathVariable String token) {
        ResponseEntity<Map<String, String>> tokenCheck = service.validateToken(token, "admin");
        if (!tokenCheck.getStatusCode().is2xxSuccessful())
            return tokenCheck;

        Map<String, String> body = new HashMap<>();
        int result = doctorService.deleteDoctor(id);
        if (result == 1) {
            body.put("message", "Médico excluído com sucesso");
            return ResponseEntity.ok(body);
        } else if (result == -1) {
            body.put("message", "Médico não encontrado com id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        } else {
            body.put("message", "Ocorreu algum erro interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    // 7) Filtro de médicos
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(@PathVariable String name,
            @PathVariable String time,
            @PathVariable("speciality") String specialty) {
        Map<String, Object> res = service.filterDoctor(name, specialty, time);
        return ResponseEntity.ok(res);
    }
}
