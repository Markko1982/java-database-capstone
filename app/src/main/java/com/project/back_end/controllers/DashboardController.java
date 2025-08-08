package com.project.back_end.controllers;

import com.project.back_end.services.TokenService; // <-- MUDANÇA IMPORTANTE AQUI
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {

    private final TokenService tokenService; // <-- MUDANÇA IMPORTANTE AQUI

    @Autowired
    public DashboardController(TokenService tokenService) { // <-- MUDANÇA IMPORTANTE AQUI
        this.tokenService = tokenService;
    }

    @GetMapping("/admin/dashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        // Agora chamamos o método do seu TokenService
        Map<String, Object> validationResult = tokenService.validateToken(token, "admin");

        if (validationResult.isEmpty()) {
            return "admin/adminDashboard";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/doctor/dashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        // Agora chamamos o método do seu TokenService
        Map<String, Object> validationResult = tokenService.validateToken(token, "doctor");

        if (validationResult.isEmpty()) {
            return "doctor/doctorDashboard";
        } else {
            return "redirect:/";
        }
    }
}