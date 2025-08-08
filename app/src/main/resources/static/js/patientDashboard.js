import { createDoctorCard } from './components/doctorCard.js';
import { openModal } from './components/modals.js';
import { getDoctors, filterDoctors } from './services/doctorServices.js';
import { patientLogin, patientSignup } from './services/patientServices.js';

document.addEventListener("DOMContentLoaded", () => {
    loadDoctorCards();

    // Adiciona listeners para os filtros
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

    // Adiciona listeners para botões de login/signup que podem estar no cabeçalho
    // Esta é uma abordagem defensiva, o header.js já deve lidar com isso.
    const loginBtn = document.getElementById("patientLogin");
    if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));

    const signupBtn = document.getElementById("patientSignup");
    if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));
});

async function loadDoctorCards() {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
}

async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;
    const filteredDoctors = await filterDoctors(name, time, specialty);
    renderDoctorCards(filteredDoctors);
}

function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
    if (doctors.length === 0) {
        contentDiv.innerHTML = "<p>Nenhum médico encontrado com os filtros aplicados.</p>";
        return;
    }
    doctors.forEach(doctor => {
        const doctorCard = createDoctorCard(doctor);
        contentDiv.appendChild(doctorCard);
    });
}

// Funções globais para serem chamadas pelos formulários dos modais
window.signupPatient = async function() {
    event.preventDefault();
    const name = document.getElementById('patient-name').value;
    const email = document.getElementById('patient-email').value;
    const password = document.getElementById('patient-password').value;
    const phone = document.getElementById('patient-phone').value;
    const result = await patientSignup({ name, email, password, phone });
    if (result.success) {
        alert("Cadastro realizado com sucesso! Faça o login.");
        document.getElementById('modal').style.display = 'none';
    } else {
        alert(`Erro no cadastro: ${result.message}`);
    }
};

window.loginPatient = async function() {
    event.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const response = await patientLogin({ email, password });
    if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('userRole', 'loggedPatient');
        window.location.reload(); // Recarrega a página para mostrar o cabeçalho de logado
    } else {
        alert("Email ou senha inválidos.");
    }
};