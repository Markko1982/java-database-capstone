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

            // Criando todos os 24 objetos de prescrição
            Prescription p1 = new Prescription("John Smith", 51, "Paracetamol", "500mg", "Take 1 tablet every 6 hours.");
            Prescription p2 = new Prescription("Emily Rose", 52, "Aspirin", "300mg", "Take 1 tablet after meals.");
            Prescription p3 = new Prescription("Michael Jordan", 53, "Ibuprofen", "400mg", "Take 1 tablet every 8 hours.");
            Prescription p4 = new Prescription("Olivia Moon", 54, "Antihistamine", "10mg", "Take 1 tablet daily before bed.");
            Prescription p5 = new Prescription("Liam King", 55, "Vitamin C", "1000mg", "Take 1 tablet daily.");
            Prescription p6 = new Prescription("Sophia Lane", 56, "Antibiotics", "500mg", "Take 1 tablet every 12 hours.");
            Prescription p7 = new Prescription("Noah Brooks", 57, "Paracetamol", "500mg", "Take 1 tablet every 6 hours.");
            Prescription p8 = new Prescription("Ava Daniels", 58, "Ibuprofen", "200mg", "Take 1 tablet every 8 hours.");
            Prescription p9 = new Prescription("William Harris", 59, "Aspirin", "300mg", "Take 1 tablet after meals.");
            Prescription p10 = new Prescription("Mia Green", 60, "Vitamin D", "1000 IU", "Take 1 tablet daily with food.");
            Prescription p11 = new Prescription("James Brown", 61, "Antihistamine", "10mg", "Take 1 tablet every morning.");
            Prescription p12 = new Prescription("Amelia Clark", 62, "Paracetamol", "500mg", "Take 1 tablet every 6 hours.");
            Prescription p13 = new Prescription("Ben Johnson", 63, "Ibuprofen", "400mg", "Take 1 tablet every 8 hours.");
            Prescription p14 = new Prescription("Ella Monroe", 64, "Vitamin C", "1000mg", "Take 1 tablet daily.");
            Prescription p15 = new Prescription("Lucas Turner", 65, "Aspirin", "300mg", "Take 1 tablet after meals.");
            Prescription p16 = new Prescription("Grace Scott", 66, "Paracetamol", "500mg", "Take 1 tablet every 6 hours.");
            Prescription p17 = new Prescription("Ethan Hill", 67, "Ibuprofen", "400mg", "Take 1 tablet every 8 hours.");
            Prescription p18 = new Prescription("Ruby Ward", 68, "Vitamin D", "1000 IU", "Take 1 tablet daily with food.");
            Prescription p19 = new Prescription("Jack Baker", 69, "Antibiotics", "500mg", "Take 1 tablet every 12 hours.");
            Prescription p20 = new Prescription("Mia Hall", 70, "Paracetamol", "500mg", "Take 1 tablet every 6 hours.");
            Prescription p21 = new Prescription("Owen Thomas", 71, "Ibuprofen", "200mg", "Take 1 tablet every 8 hours.");
            Prescription p22 = new Prescription("Ivy Jackson", 72, "Antihistamine", "10mg", "Take 1 tablet every morning.");
            Prescription p23 = new Prescription("Leo Martin", 73, "Vitamin C", "1000mg", "Take 1 tablet daily.");
            Prescription p24 = new Prescription("Ella Moore", 74, "Aspirin", "300mg", "Take 1 tablet after meals.");

            // Salva a lista completa de prescrições no banco de dados
            prescriptionRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23, p24));

            System.out.println("Todos os 24 registros de Prescrições foram inseridos.");
        } else {
            System.out.println("O banco de dados MongoDB já contém dados de Prescrições.");
        }
    }
}