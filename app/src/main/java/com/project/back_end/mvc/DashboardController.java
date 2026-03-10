package com.project.back_end.mvc;

import com.project.back_end.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller("mvcDashboardController")
public class DashboardController {

    private final AuthService authService;

    public DashboardController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admindashboard/{token}")
    public String onDashboard(@PathVariable String token) {
        var check = authService.validateToken(token, "admin");
        if (check.getStatusCode().is2xxSuccessful())
            return "redirect:/";
        return "adminDashboard";
    }

    @GetMapping("/doctordashboard/{token}")
    public String onDoctorDashboard(@PathVariable String token) {
        var check = authService.validateToken(token, "doctor");
        if (check.getStatusCode().is2xxSuccessful())
            return "redirect:/";
        return "doctorDashboard";
    }
}