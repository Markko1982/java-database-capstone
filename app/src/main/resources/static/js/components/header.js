// src/main/resources/static/js/components/header.js
document.addEventListener('DOMContentLoaded', () => {
  const el = document.getElementById('header');
  if (!el) return;

  el.innerHTML = `
    <header class="site-header">
      <div class="container header-container">
        <a href="/" class="brand" aria-label="Início">
          <img src="/images/logo/logo.png" alt="Logo da Clínica" height="40" />
        </a>
        <nav class="nav-actions">
          <a href="#" id="loginLink">Login</a>
          <a href="#" id="signupLink">Inscrever-se</a>
        </nav>
      </div>
    </header>
  `;
});
