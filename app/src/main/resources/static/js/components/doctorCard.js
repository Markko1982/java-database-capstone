// A palavra 'export' permite que esta função seja importada em outros arquivos
export function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.classList.add("doctor-card");
    card.dataset.id = doctor.id; // Armazena o ID do médico no próprio elemento

    const role = localStorage.getItem("userRole");

    // Seção de informações do médico
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");
    
    const name = document.createElement("h3");
    name.textContent = doctor.name;

    const specialization = document.createElement("p");
    specialization.textContent = `Especialidade: ${doctor.specialty}`;
    
    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;
    
    const availability = document.createElement("p");
    // O backend deve fornecer 'availableTimes' como um array de strings
    availability.textContent = `Horários: ${doctor.availableTimes ? doctor.availableTimes.join(", ") : "N/A"}`;

    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    // Seção de ações (botões)
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    // Adiciona botões com base no papel do usuário
    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Excluir";
        removeBtn.addEventListener("click", async () => {
            if (confirm(`Tem certeza que deseja excluir o(a) Dr(a). ${doctor.name}?`)) {
                // A lógica para chamar a API de exclusão virá aqui
                console.log(`Excluindo médico com ID: ${doctor.id}`);
                // Ex: await deleteDoctor(doctor.id);
                card.remove(); // Remove o card da tela
            }
        });
        actionsDiv.appendChild(removeBtn);
    } else if (role === "loggedPatient") {
        const bookNowBtn = document.createElement("button");
        bookNowBtn.textContent = "Agendar Consulta";
        bookNowBtn.addEventListener("click", (e) => {
            // A lógica para abrir o modal de agendamento virá aqui
            console.log(`Agendando com o médico: ${doctor.name}`);
            // Ex: const patientData = await getPatientData(token);
            //     showBookingOverlay(e, doctor, patientData);
        });
        actionsDiv.appendChild(bookNowBtn);
    } else if (role === "patient") {
        const bookNowBtn = document.createElement("button");
        bookNowBtn.textContent = "Agendar Consulta";
        bookNowBtn.addEventListener("click", () => {
            alert("Por favor, faça login para agendar uma consulta.");
            // Pode também abrir o modal de login aqui
        });
        actionsDiv.appendChild(bookNowBtn);
    }
    
    card.appendChild(infoDiv);
    // Só adiciona a div de ações se ela tiver algum botão dentro
    if (actionsDiv.hasChildNodes()) {
        card.appendChild(actionsDiv);
    }

    return card;
}