// src/main/resources/static/js/components/footer.js
document.addEventListener('DOMContentLoaded', () => {
  const el = document.getElementById('footer');
  if (!el) return;

  const year = new Date().getFullYear();
  el.innerHTML = `
    <footer class="site-footer">
      <div class="container footer-container">
        <div class="footer-brand">
          <img src="/images/logo/logo.png" alt="Logo da Clínica" height="28" />
        </div>
        <div class="footer-cols">
          <div>
            <h4>Empresa</h4>
            <a href="#">Sobre Nós</a><br/>
            <a href="#">Carreiras</a>
          </div>
          <div>
            <h4>Suporte</h4>
            <a href="#">Sua Conta</a><br/>
            <a href="#">Central de Ajuda</a>
          </div>
          <div>
            <h4>Legal</h4>
            <a href="#">Termos de Serviço</a><br/>
            <a href="#">Política de Privacidade</a>
          </div>
        </div>
        <div class="copy">© ${year} Clínica Skynet. Todos os direitos reservados.</div>
      </div>
    </footer>
  `;
});
