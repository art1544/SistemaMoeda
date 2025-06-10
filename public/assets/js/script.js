// script.js - navegação da tela inicial

document.addEventListener('DOMContentLoaded', function() {
    const btnLogin = document.getElementById('btn-login');
    const btnRegister = document.getElementById('btn-register');
    if (btnLogin) btnLogin.onclick = () => window.location.href = 'login.html';
    if (btnRegister) btnRegister.onclick = () => window.location.href = 'register.html';
});
