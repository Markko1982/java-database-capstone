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
import com.project.back_end.services.AuthService;
import com.project.back_end.services.DoctorService;
import com.project.back_end.dto.ApiMessageResponse;
import com.project.back_end.dto.ApiAuthResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}" + "doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AuthService authService;

    public DoctorController(DoctorService doctorService, AuthService authService) {
        this.doctorService = doctorService;
        this.authService = authService;
    }

    private ResponseEntity<Map<String, String>> unauthorized(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // 1) Disponibilidade do médico (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
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
    /**
     * @deprecated Use GET /doctor/availability/{user}/{doctorId}/{date}
     *             (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getAvailability(@PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, user);
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return tokenCheck;
        }

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
    public ResponseEntity<ApiMessageResponse> addDoctorBearer(
            @RequestAttribute("token") String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.saveDoctorOrThrow(doctor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiMessageResponse("Médico cadastrado com sucesso."));
    }

    // 3) Adicionar novo médico (somente admin)
    /**
     * @deprecated Use POST /doctor (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @PostMapping("/{token}")
    public ResponseEntity<ApiMessageResponse> addDoctor(@PathVariable String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.saveDoctorOrThrow(doctor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiMessageResponse("Médico cadastrado com sucesso."));
    }

    // 4) Login do médico
    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> doctorLogin(@Valid @RequestBody Login login) {
        return ResponseEntity.ok(doctorService.validateDoctor(login));
    }

    // 5) Atualizar médico (somente admin) (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public ResponseEntity<ApiMessageResponse> updateDoctorBearer(
            @RequestAttribute("token") String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.updateDoctorOrThrow(doctor);

        return ResponseEntity.ok(new ApiMessageResponse("Médico atualizado"));
    }

    // 5) Atualizar médico (somente admin)
    /**
     * @deprecated Use PUT /doctor (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @PutMapping("/{token}")
    public ResponseEntity<ApiMessageResponse> updateDoctor(@PathVariable String token,
            @Valid @RequestBody Doctor doctor) {

        doctorService.updateDoctorOrThrow(doctor);

        return ResponseEntity.ok(new ApiMessageResponse("Médico atualizado"));
    }

    // 6) Excluir médico (somente admin) (Authorization: Bearer <token>)
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> deleteDoctorBearer(
            @PathVariable Long id,
            @RequestAttribute("token") String token) {

        doctorService.deleteDoctorOrThrow(id);

        return ResponseEntity.ok(new ApiMessageResponse("Médico excluído com sucesso"));
    }

    // 6) Excluir médico (somente admin)
    /**
     * @deprecated Use DELETE /doctor/{id} (Authorization: Bearer <token>)
     */
    @Hidden
    @Deprecated
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<ApiMessageResponse> deleteDoctor(@PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> tokenCheck = authService.validateToken(token, "admin");
        if (!tokenCheck.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(tokenCheck.getStatusCode())
                    .body(new ApiMessageResponse(tokenCheck.getBody().get("message")));
        }

        doctorService.deleteDoctorOrThrow(id);
        return ResponseEntity.ok(new ApiMessageResponse("Médico excluído com sucesso"));
    }

    // 7) Filtro de médicos
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filterDoctors(@PathVariable String name,
            @PathVariable String time,
            @PathVariable("speciality") String specialty) {

        Map<String, Object> res = doctorService.filterDoctor(name, specialty, time);
        return ResponseEntity.ok(res);
    }
}