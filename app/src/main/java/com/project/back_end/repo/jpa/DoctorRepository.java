package com.project.back_end.repo.jpa;


import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // 1) Email exato
    Doctor findByEmail(String email);

    // 2) Nome com correspondência parcial (LIKE + CONCAT)
    @Query("""
           SELECT d
           FROM Doctor d
           WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Doctor> findByNameLike(@Param("name") String name);

    // 3) Nome parcial + especialidade exata, ambos case-insensitive
    @Query("""
           SELECT d
           FROM Doctor d
           WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
             AND LOWER(d.specialty) = LOWER(:specialty)
           """)
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            @Param("name") String name,
            @Param("specialty") String specialty);

    // 4) Especialidade (case-insensitive) via convenção
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
