package com.project.back_end.controllers;

import com.project.back_end.dto.ApiAuthResponse;
import com.project.back_end.models.Admin;
import com.project.back_end.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}" + "admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiAuthResponse> adminLogin(@Valid @RequestBody Admin receivedAdmin) {
        return ResponseEntity.ok(authService.validateAdmin(receivedAdmin));
    }
}