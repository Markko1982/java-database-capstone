package com.project.back_end.dto;

/**
 * DTO de Login para receber as credenciais do usuário via @RequestBody.
 * Não possui anotações de persistência.
 */
public class Login {

    private String identifier; // email (Médico/Paciente) ou username (Administrador)
    private String password;

    public Login() {
    }

    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
