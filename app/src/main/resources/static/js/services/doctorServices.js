import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + '/doctor';

/**
 * Busca todos os médicos do servidor.
 * @returns {Promise<Array>} Uma promessa que resolve para uma lista de médicos, ou uma lista vazia em caso de erro.
 */
export async function getDoctors() {
    try {
        const response = await fetch(DOCTOR_API);
        if (!response.ok) {
            throw new Error('Não foi possível buscar os médicos.');
        }
        return await response.json();
    } catch (error) {
        console.error("Erro em getDoctors:", error);
        return []; // Retorna uma lista vazia para não quebrar a UI
    }
}

/**
 * Deleta um médico específico pelo seu ID.
 * @param {string} id - O ID do médico a ser deletado.
 * @param {string} token - O token de autenticação do admin.
 * @returns {Promise<Object>} Um objeto indicando sucesso ou falha.
 */
export async function deleteDoctor(id, token) {
    try {
        const response = await fetch(`${DOCTOR_API}/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            return { success: true, message: 'Médico deletado com sucesso!' };
        } else {
            const errorData = await response.json();
            return { success: false, message: errorData.message || 'Falha ao deletar médico.' };
        }
    } catch (error) {
        console.error("Erro em deleteDoctor:", error);
        return { success: false, message: 'Ocorreu um erro de rede.' };
    }
}

/**
 * Salva um novo médico no banco de dados.
 * @param {Object} doctor - O objeto do médico contendo os detalhes.
 * @param {string} token - O token de autenticação do admin.
 * @returns {Promise<Object>} Um objeto indicando sucesso ou falha.
 */
export async function saveDoctor(doctor, token) {
    try {
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(doctor)
        });

        if (response.ok) {
            return { success: true, message: 'Médico adicionado com sucesso!' };
        } else {
            const errorData = await response.json();
            return { success: false, message: errorData.message || 'Falha ao adicionar médico.' };
        }
    } catch (error) {
        console.error("Erro em saveDoctor:", error);
        return { success: false, message: 'Ocorreu um erro de rede.' };
    }
}

/**
 * Filtra médicos com base em nome, horário ou especialidade.
 * @param {string} name - O nome para buscar.
 * @param {string} time - O horário para filtrar (ex: 'AM', 'PM').
 * @param {string} specialty - A especialidade para filtrar.
 * @returns {Promise<Array>} Uma lista de médicos filtrados.
 */
export async function filterDoctors(name = '', time = '', specialty = '') {
    try {
        // Constrói a URL com os parâmetros de busca
        const queryParams = new URLSearchParams({ name, time, specialty });
        const response = await fetch(`${DOCTOR_API}/filter?${queryParams}`);
        
        if (!response.ok) {
            throw new Error('Erro ao filtrar médicos.');
        }
        return await response.json();
    } catch (error) {
        console.error("Erro em filterDoctors:", error);
        alert('Não foi possível carregar os médicos filtrados.');
        return [];
    }
}