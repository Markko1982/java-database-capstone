package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.mongo.PrescriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /** 1) Salvar prescrição */
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription) {
        Map<String, String> body = new HashMap<>();
        try {
            prescriptionRepository.save(prescription);
            body.put("message", "Prescrição salva");
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (Exception e) {
            body.put("message", "Erro ao salvar prescrição");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    /** 2) Buscar prescrição por appointmentId */
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId) {
        Map<String, Object> res = new HashMap<>();
        try {
            List<Prescription> list = prescriptionRepository.findByAppointmentId(appointmentId);
            res.put("prescriptions", list);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("message", "Erro ao buscar prescrição");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
