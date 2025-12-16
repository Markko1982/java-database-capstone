import { openModal } from '../components/modals.js';
import { createDoctorCard } from '../components/doctorCard.js';
import { getDoctors, filterDoctors, saveDoctor } from './doctorServices.js';

// Adiciona um listener que executa o código quando o HTML da página estiver totalmente carregado
document.addEventListener("DOMContentLoaded", () => {
    // Carrega os cartões de todos os médicos assim que a página abre
    loadDoctorCards();

    // Adiciona os listeners para a barra de busca e os filtros
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
    
    // O listener para o botão "Adicionar Médico" já está no header.js, mas podemos garantir aqui também
    // Se o header.js falhar, este código ainda funcionará.
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener('click', () => openModal('addDoctor'));
    }
});

/**
 * Função principal para carregar e renderizar todos os médicos.
 */
async function loadDoctorCards() {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
}

/**
 * Função para lidar com as mudanças nos filtros e na busca.
 */
async function filterDoctorsOnChange() {
    const name = document.getElementById("searchBar").value;
    const time = document.getElementById("filterTime").value;
    const specialty = document.getElementById("filterSpecialty").value;

    const filteredDoctors = await filterDoctors(name, time, specialty);
    renderDoctorCards(filteredDoctors);
}

/**
 * Função auxiliar para renderizar uma lista de médicos na tela.
 * @param {Array} doctors - A lista de médicos a ser renderizada.
 */
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = ""; // Limpa o conteúdo atual

    if (doctors.length === 0) {
        contentDiv.innerHTML = "<p>Nenhum médico encontrado com os filtros aplicados.</p>";
        return;
    }

    doctors.forEach(doctor => {
        const doctorCard = createDoctorCard(doctor);
        contentDiv.appendChild(doctorCard);
    });
}

/**
 * Função para lidar com a submissão do formulário de adição de médico.
 * Esta função é exposta globalmente para ser chamada pelo `onsubmit` do formulário no modal.
 */
window.adminAddDoctor = async function() {
    event.preventDefault(); // Previne o recarregamento da página
    
    const token = localStorage.getItem('token');
    if (!token) {
        alert("Sessão inválida. Faça login novamente.");
        return;
    }

    // Coleta os dados do formulário
    const name = document.getElementById('doctor-name').value;
    const specialty = document.getElementById('doctor-specialty').value;
    const email = document.getElementById('doctor-email').value;
    const password = document.getElementById('doctor-password').value;
    const phone = document.getElementById('doctor-phone').value;

    // Coleta os horários selecionados (checkboxes)
    const availableTimes = [];
    document.querySelectorAll('input[name="availability"]:checked').forEach(checkbox => {
        availableTimes.push(checkbox.value);
    });
    
    const doctor = { name, specialty, email, password, phone, availableTimes };

    const result = await saveDoctor(doctor, token);

    if (result.success) {
        alert(result.message);
        document.getElementById('modal').style.display = 'none'; // Fecha o modal
        loadDoctorCards(); // Recarrega a lista de médicos
    } else {
        alert(`Erro: ${result.message}`);
    }
}