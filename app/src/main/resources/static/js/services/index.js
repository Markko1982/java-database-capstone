import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';
// A função selectRole provavelmente virá do render.js, vamos assumir que ele existe
import { selectRole } from '../render.js'; 

// Define as URLs completas das APIs
const ADMIN_API = API_BASE_URL + '/admin/login'; // Corrigido para incluir /login
const DOCTOR_API = API_BASE_URL + '/doctor/login';

// Função para manipular o login do Admin
async function adminLoginHandler() {
    const username = document.getElementById('admin-username').value;
    const password = document.getElementById('admin-password').value;
    const admin = { username, password };

    try {
        const response = await fetch(ADMIN_API, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(admin),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token); // Supondo que a resposta seja { "token": "..." }
            selectRole('admin'); // Define o papel e redireciona para o painel do admin
        } else {
            alert('Credenciais de admin inválidas!');
        }
    } catch (error) {
        console.error('Erro ao tentar fazer login do admin:', error);
        alert('Ocorreu um erro no servidor. Tente novamente mais tarde.');
    }
}

// Função para manipular o login do Médico
async function doctorLoginHandler() {
    const email = document.getElementById('doctor-email').value;
    const password = document.getElementById('doctor-password').value;
    const doctor = { email, password };

    try {
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(doctor),
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            selectRole('doctor'); // Define o papel e redireciona para o painel do médico
        } else {
            alert('Credenciais de médico inválidas!');
        }
    } catch (error) {
        console.error('Erro ao tentar fazer login do médico:', error);
        alert('Ocorreu um erro no servidor. Tente novamente mais tarde.');
    }
}

// Para que as funções possam ser chamadas pelo 'onclick' no HTML gerado dinamicamente,
// nós as anexamos ao objeto global 'window'.
window.adminLoginHandler = adminLoginHandler;
window.doctorLoginHandler = doctorLoginHandler;

// A lógica para abrir os modais ao clicar nos botões de papel (Admin, Doctor)
// geralmente fica na função selectRole, que será chamada a partir do HTML.
// A função selectRole no seu render.js ou util.js deve chamar openModal().
// Exemplo de como poderia ser a função `selectRole` em outro arquivo:
/*
function selectRole(role) {
    if (role === 'Admin') {
        openModal('adminLogin');
    } else if (role === 'Doctor') {
        openModal('doctorLogin');
    } else if (role === 'Patient') {
        localStorage.setItem('userRole', 'patient');
        window.location.href = '/pages/patientDashboard.html';
    }
}
*/