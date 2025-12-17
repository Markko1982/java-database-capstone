package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Busca paciente por e-mail exato
    Patient findByEmail(String email);

    // Busca paciente por e-mail OU telefone
    Patient findByEmailOrPhone(String email, String phone);
}
