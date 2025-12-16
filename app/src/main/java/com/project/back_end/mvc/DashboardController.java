package com.project.back_end.mvc;

import com.project.back_end.services.Service; // <-- Não se esqueça de adicionar este import!
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("mvcDashboardController")
public class DashboardController {

    // 1. DECLARE O CAMPO DO SERVIÇO
    private final Service service;

    // 2. CRIE O CONSTRUTOR PARA A INJEÇÃO DE DEPENDÊNCIA
    public DashboardController(Service service) {
        this.service = service;
    }

    @GetMapping("/admindashboard/{token}")
    public String onDashboard(@PathVariable String token) {
        // Agora a variável 'service' existe e pode ser usada
        var check = service.validateToken(token, "admin");
        if (check.getStatusCode().is2xxSuccessful()) return "redirect:/";
        return "adminDashboard";
    }

    @GetMapping("/doctordashboard/{token}")
    public String onDoctorDashboard(@PathVariable String token) {
        // Agora a variável 'service' existe e pode ser usada
        var check = service.validateToken(token, "doctor");
        if (check.getStatusCode().is2xxSuccessful()) return "redirect:/";
        return "doctorDashboard";
    }
}