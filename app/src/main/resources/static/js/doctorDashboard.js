import { getPatientAppointments } from './patientServices.js'; 
import { createPatientRow } from '../components/patientRows.js'; 

// Variáveis de estado para o painel
let selectedDate = new Date().toISOString().split('T')[0]; // Data de hoje no formato YYYY-MM-DD
let patientName = null;
const token = localStorage.getItem('token');
const doctorId = localStorage.getItem('userId'); // Supondo que o ID do usuário logado é salvo aqui

document.addEventListener("DOMContentLoaded", () => {
    // Referências aos elementos do DOM
    const tableBody = document.getElementById("patientTableBody");
    const searchBar = document.getElementById("searchBar");
    const todayButton = document.getElementById("todayAppointmentsBtn");
    const datePicker = document.getElementById("dateFilter");

    // Define o valor inicial do date picker para hoje
    datePicker.value = selectedDate;

    // Configura os listeners de evento
    searchBar.addEventListener('input', (e) => {
        patientName = e.target.value.trim() === '' ? null : e.target.value.trim();
        loadAppointments();
    });

    todayButton.addEventListener('click', () => {
        selectedDate = new Date().toISOString().split('T')[0];
        datePicker.value = selectedDate;
        loadAppointments();
    });

    datePicker.addEventListener('change', (e) => {
        selectedDate = e.target.value;
        loadAppointments();
    });

    // Função para carregar as consultas
    async function loadAppointments() {
        if (!token || !doctorId) {
            tableBody.innerHTML = `<tr><td colspan="5">Erro: Sessão inválida ou ID do médico não encontrado.</td></tr>`;
            return;
        }

        tableBody.innerHTML = `<tr><td colspan="5">Carregando...</td></tr>`;

        try {
            // A API para buscar consultas de um médico ainda não foi definida, usaremos um placeholder
            // A função 'getPatientAppointments' provavelmente precisará ser ajustada ou uma nova criada
            const appointments = await getDoctorAppointments(selectedDate, patientName, token, doctorId);
            
            tableBody.innerHTML = ""; // Limpa a tabela

            if (!appointments || appointments.length === 0) {
                document.getElementById('noPatientRecord').style.display = 'block';
            } else {
                document.getElementById('noPatientRecord').style.display = 'none';
                appointments.forEach(appointment => {
                    const row = createPatientRow(appointment.patient); // Supondo que cada consulta tem um objeto paciente
                    tableBody.appendChild(row);
                });
            }
        } catch (error) {
            console.error("Erro ao carregar consultas:", error);
            tableBody.innerHTML = `<tr><td colspan="5">Falha ao carregar as consultas.</td></tr>`;
        }
    }

    // Carrega as consultas iniciais (de hoje)
    loadAppointments();
});

// Função placeholder para buscar consultas do médico, pois a original era para pacientes
async function getDoctorAppointments(date, patientName, token, doctorId) {
    // Esta função precisaria de um endpoint de API específico no backend
    console.log(`Buscando consultas para o médico ${doctorId} na data ${date}`);
    // Exemplo de como a chamada de API poderia ser:
    // const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/appointments?date=${date}&patientName=${patientName}`, { headers: ... });
    return []; // Retornando vazio por enquanto
}