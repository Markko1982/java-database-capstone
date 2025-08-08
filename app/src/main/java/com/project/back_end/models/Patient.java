package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Representa a entidade 'Patient' (Paciente) no banco de dados.
 * Armazena os detalhes pessoais e de contato dos usuários do sistema.
 */
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "Forneça um endereço de e-mail válido")
    private String email;

    /**
     * A senha para autenticação do paciente.
     * Adicionamos @JsonProperty para consistência e segurança,
     * mesmo não estando nas dicas desta seção específica.
     */
    @NotNull(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10}", message = "O número de telefone deve ter 10 dígitos")
    private String phone;

    @NotNull(message = "O endereço é obrigatório")
    @Size(max = 255, message = "O endereço não pode exceder 255 caracteres")
    private String address;

    // Construtor padrão para o JPA
    public Patient() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}