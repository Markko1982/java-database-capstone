package com.project.back_end.config;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("dev") // Só executa quando o perfil 'dev' estiver ativo
public class MongoDataSeeder implements CommandLineRunner {

    private final PrescriptionRepository prescriptionRepository;

    public MongoDataSeeder(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public void run(String... args) {
        try {
            if (prescriptionRepository.count() == 0) {
                System.out.println("[Seeder] Populando o MongoDB com dados iniciais de Prescrições...");

                Prescription p1 = new Prescription();
                p1.setPatientName("John Smith");
                p1.setAppointmentId(51L);
                p1.setMedication("Paracetamol");
                p1.setDosage("500mg");
                p1.setDoctorNotes("Take 1 tablet every 6 hours.");

                Prescription p2 = new Prescription();
                p2.setPatientName("Emily Rose");
                p2.setAppointmentId(52L);
                p2.setMedication("Aspirin");
                p2.setDosage("300mg");
                p2.setDoctorNotes("Take 1 tablet after meals.");

                List<Prescription> prescriptions = Arrays.asList(p1, p2);
                prescriptionRepository.saveAll(prescriptions);

                System.out.println("[Seeder] Registros de Prescrições inseridos.");
            } else {
                System.out.println("[Seeder] MongoDB já contém dados de Prescrições. Nada a fazer.");
            }
        } catch (Exception e) {
            // Não derruba a aplicação se houver problema de conexão/autenticação
            System.err.println("[Seeder] Falha ao popular MongoDB: " + e.getMessage());
        }
    }
}
