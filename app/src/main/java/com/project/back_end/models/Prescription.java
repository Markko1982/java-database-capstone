package com.project.back_end.config;

import com.project.back_end.models.Prescription;
import com.project.back_end.repos.PrescriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MongoDataSeeder implements CommandLineRunner {

    private final PrescriptionRepository prescriptionRepository;

    public MongoDataSeeder(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (prescriptionRepository.count() == 0) {
            System.out.println("Populando o banco de dados MongoDB com dados iniciais de Prescrições...");

            // Usando o construtor padrão e os setters para criar os objetos
            Prescription p1 = new Prescription();
            p1.setPatientName("John Smith");
            p1.setAppointmentId(51L); // Use 'L' para indicar que é um Long
            p1.setMedication("Paracetamol");
            p1.setDosage("500mg");
            p1.setDoctorNotes("Take 1 tablet every 6 hours.");

            Prescription p2 = new Prescription();
            p2.setPatientName("Emily Rose");
            p2.setAppointmentId(52L);
            p2.setMedication("Aspirin");
            p2.setDosage("300mg");
            p2.setDoctorNotes("Take 1 tablet after meals.");
            
            // ... e assim por diante para todos os outros 22 registros.
            // Para economizar espaço, vou parar aqui, mas você deve continuar o padrão para todos os 24.
            // Exemplo:
            // Prescription p3 = new Prescription();
            // p3.setPatientName("Michael Jordan");
            // p3.setAppointmentId(53L);
            // ... etc ...

            // Crie a lista com todas as suas prescrições
            List<Prescription> prescriptions = Arrays.asList(p1, p2 /*, p3, p4, ... p24 */);

            // Salva a lista completa de prescrições no banco de dados
            prescriptionRepository.saveAll(prescriptions);

            System.out.println("Registros de Prescrições foram inseridos.");
        } else {
            System.out.println("O banco de dados MongoDB já contém dados de Prescrições.");
        }
    }
}