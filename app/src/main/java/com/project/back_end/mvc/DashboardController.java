package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    private final Service service;

    public DashboardController(Service service) {
        this.service = service;
    }

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        var check = service.validateToken(token, "admin");
        if (!check.getStatusCode().is2xxSuccessful()) return "redirect:/";
        return "admin/adminDashboard";
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        var check = service.validateToken(token, "doctor");
        if (!check.getStatusCode().is2xxSuccessful()) return "redirect:/";
        return "doctor/doctorDashboard";
    }
}
