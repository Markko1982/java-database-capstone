package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Representa a entidade 'Doctor' (Médico) no banco de dados.
 * Armazena informações profissionais, de contato e de disponibilidade do médico.
 */
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "A especialidade é obrigatória")
    @Size(min = 3, max = 50, message = "A especialidade deve ter entre 3 e 50 caracteres")
    private String specialty;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Forneça um endereço de e-mail válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10}", message = "O número de telefone deve ter 10 dígitos")
    private String phone;

    /**
     * Uma lista de horários em que o médico está disponível.
     * @ElementCollection diz ao JPA para tratar esta lista como uma coleção de
     * elementos simples, armazenando-os em uma tabela separada e ligada a este médico.
     */
    @ElementCollection(fetch = FetchType.EAGER) // EAGER para carregar os horários junto com o médico
    @CollectionTable(name = "doctor_available_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_time")
    private List<String> availableTimes;

    // Construtor padrão para o JPA
    public Doctor() {
    }

    // Getters e Setters para todos os campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}