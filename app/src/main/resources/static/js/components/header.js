// Função principal para renderizar o cabeçalho dinamicamente
function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return; // Se não houver div de header, não faz nada

    // Limpa o estado do usuário se estiver na página inicial
    if (window.location.pathname === "/" || window.location.pathname.endsWith("/index.html")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
    }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Validação de segurança: se o papel requer login mas não há token, desloga
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Sessão expirada ou login inválido. Por favor, faça login novamente.");
        window.location.href = "/";
        return;
    }
    
    let headerContent = `
    <header class="header">
        <div class="header-logo">
            <a href="/"><img src="/assets/images/logo.png" alt="Logo da Clínica" /></a>
        </div>
        <nav class="header-nav">
    `;

    // Constrói o conteúdo do cabeçalho com base no papel do usuário
    if (role === "admin") {
        headerContent += `
            <button id="addDocBtn" class="adminBtn">Adicionar Médico</button>
            <a href="#" id="logoutBtn">Logout</a>
        `;
    } else if (role === "doctor") {
        headerContent += `
            <a href="/doctor/dashboard">Início</a>
            <a href="#" id="logoutBtn">Logout</a>
        `;
    } else if (role === "loggedPatient") {
        headerContent += `
            <a href="/patient/dashboard">Início</a>
            <a href="#">Consultas</a>
            <a href="#" id="logoutPatientBtn">Logout</a>
        `;
    } else { // Padrão para "patient" (não logado) ou nulo
        headerContent += `
            <a href="#" id="loginBtn">Login</a>
            <a href="#" id="signupBtn">Inscrever-se</a>
        `;
    }

    headerContent += `
        </nav>
    </header>
    `;

    // Injeta o HTML gerado na página
    headerDiv.innerHTML = headerContent;
    // Anexa os listeners de evento aos botões recém-criados
    attachHeaderButtonListeners(role);
}

// Anexa os listeners de evento para os botões do cabeçalho
function attachHeaderButtonListeners(role) {
    if (role === "admin") {
        const addDocBtn = document.getElementById("addDocBtn");
        if (addDocBtn) {
            addDocBtn.addEventListener("click", () => openModal('addDoctor'));
        }
    }
    
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }
    
    const logoutPatientBtn = document.getElementById("logoutPatientBtn");
    if (logoutPatientBtn) {
        logoutPatientBtn.addEventListener("click", logoutPatient);
    }
    
    // Listeners para os botões de login/signup podem ser adicionados aqui se necessário
    // Ex: document.getElementById("loginBtn").addEventListener...
}

// Função de logout para Admin e Médico
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

// Função de logout específica para Paciente, para manter o papel "patient"
function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient"); // Volta para o estado não-logado
    window.location.href = "/patient/dashboard"; // Ou para a página inicial
}

// Chama a função principal para construir o cabeçalho quando o script carregar
renderHeader();