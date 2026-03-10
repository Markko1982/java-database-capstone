package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.mongo.PrescriptionRepository;
import com.project.back_end.dto.ApiMessageResponse;

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
    public ResponseEntity<ApiMessageResponse> savePrescription(Prescription prescription) {
        try {
            prescriptionRepository.save(prescription);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiMessageResponse("Prescrição salva"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiMessageResponse("Erro ao salvar prescrição"));
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
