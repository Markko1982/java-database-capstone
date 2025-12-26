package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa a entidade 'Admin' no banco de dados.
 * Mapeada para uma tabela no banco de dados usando a anotação @Entity do JPA.
 */
@Entity
public class Admin {

    /**
     * O identificador único para o Admin.
     * É a chave primária da tabela, marcada com @Id.
     * O valor é gerado automaticamente pelo banco de dados, configurado por @GeneratedValue.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome de usuário do admin, usado para login.
     
     */
    @NotBlank(message = "O nome de usuário não pode ser nulo")
    private String username;

    /**
     * A senha do admin para autenticação.
     * @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) garante que este campo pode ser
     * recebido em requisições (como um cadastro), mas NUNCA será enviado em respostas JSON,
     * protegendo a senha de ser exposta.
     */
    @NotBlank(message = "A senha não pode ser nula")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // JPA requer um construtor sem argumentos.
    public Admin() {
    }

    // Getters e Setters para permitir o acesso aos campos privados.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}