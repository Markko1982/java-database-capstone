function renderFooter() {
    const footerDiv = document.getElementById("footer");
    if (!footerDiv) return;

    footerDiv.innerHTML = `
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-logo">
                <img src="/assets/images/logo.png" alt="Logo da Clínica" />
                <p>&copy; ${new Date().getFullYear()} Clínica Skynet. Todos os direitos reservados.</p>
            </div>
            <div class="footer-links">
                <div class="footer-column">
                    <h4>Empresa</h4>
                    <a href="#">Sobre Nós</a>
                    <a href="#">Carreiras</a>
                    <a href="#">Imprensa</a>
                </div>
                <div class="footer-column">
                    <h4>Suporte</h4>
                    <a href="#">Sua Conta</a>
                    <a href="#">Central de Ajuda</a>
                    <a href="#">Contato</a>
                </div>
                <div class="footer-column">
                    <h4>Legal</h4>
                    <a href="#">Termos de Serviço</a>
                    <a href="#">Política de Privacidade</a>
                    <a href="#">Licenciamento</a>
                </div>
            </div>
        </div>
    </footer>
    `;
}

// Chama a função para renderizar o rodapé
renderFooter();