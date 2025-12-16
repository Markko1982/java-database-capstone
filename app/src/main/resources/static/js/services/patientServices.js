import { API_BASE_URL } from "../config/config.js";

const PATIENT_API = API_BASE_URL + '/patient';

/**
 * Registra um novo paciente.
 * @param {Object} data - Dados do paciente (nome, email, senha, etc.).
 * @returns {Promise<Object>} Um objeto indicando sucesso ou falha.
 */
export async function patientSignup(data) {
    try {
        const response = await fetch(`${PATIENT_API}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        if (response.ok) {
            return { success: true, message: result.message };
        } else {
            return { success: false, message: result.message || 'Falha no cadastro.' };
        }
    } catch (error) {
        console.error("Erro em patientSignup:", error);
        return { success: false, message: 'Erro de conexão.' };
    }
}

/**
 * Realiza o login de um paciente.
 * @param {Object} data - Credenciais de login (email, senha).
 * @returns {Promise<Response>} A resposta completa da fetch para análise no frontend.
 */
export async function patientLogin(data) {
    // Retorna a promessa da fetch diretamente para que a lógica de UI possa lidar com a resposta
    console.log("Enviando dados de login do paciente:", data);
    return fetch(`${PATIENT_API}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
}

/**
 * Busca os dados do paciente logado usando um token.
 * @param {string} token - O token de autenticação.
 * @returns {Promise<Object|null>} O objeto do paciente ou null em caso de erro.
 */
export async function getPatientData(token) {
    try {
        const response = await fetch(`${PATIENT_API}/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Falha ao buscar dados do paciente.');
        return await response.json();
    } catch (error) {
        console.error("Erro em getPatientData:", error);
        return null;
    }
}

/**
 * Busca as consultas de um paciente ou as consultas de um médico.
 * @param {string} id - O ID do paciente ou do médico.
 * @param {string} token - O token de autenticação.
 * @param {string} user - O papel do usuário ('patient' ou 'doctor').
 * @returns {Promise<Array|null>} Uma lista de consultas ou null em caso de erro.
 */
export async function getPatientAppointments(id, token, user) {
    // Constrói a URL dinamicamente com base no papel do usuário
    const url = user === 'doctor' 
        ? `${API_BASE_URL}/doctor/${id}/appointments` 
        : `${PATIENT_API}/${id}/appointments`;

    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Falha ao buscar consultas.');
        return await response.json();
    } catch (error) {
        console.error("Erro em getPatientAppointments:", error);
        return null;
    }
}

/**
 * Filtra consultas com base em uma condição e nome.
 * @param {string} condition - A condição de filtro (ex: 'pending').
 * @param {string} name - O nome para buscar.
 * @param {string} token - O token de autenticação.
 * @returns {Promise<Array>} Uma lista de consultas filtradas.
 */
export async function filterAppointments(condition, name, token) {
    try {
        const queryParams = new URLSearchParams({ condition, name });
        const response = await fetch(`${PATIENT_API}/appointments/filter?${queryParams}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Erro ao filtrar consultas.');
        return await response.json();
    } catch (error) {
        console.error("Erro em filterAppointments:", error);
        alert('Ocorreu um erro inesperado ao filtrar as consultas.');
        return [];
    }
}