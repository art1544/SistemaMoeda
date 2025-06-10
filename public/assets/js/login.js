// login.js

document.getElementById('login-form').onsubmit = async function(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const msg = document.getElementById('login-message');
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha: password })
        });
        const result = await response.json().catch(() => ({}));
        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = result.message || 'Login realizado com sucesso!';
            localStorage.setItem('user', JSON.stringify(result));
            // Redireciona conforme o tipo de usuário
            const roles = result.roles ? result.roles.map(r => r.authority || r) : [];
            if (roles.includes('ROLE_STUDENT')) {
                window.location.href = 'student.html';
            } else if (roles.includes('ROLE_PROFESSOR')) {
                window.location.href = 'professor.html';
            } else if (roles.includes('ROLE_COMPANY')) {
                window.location.href = 'company.html';
            } else {
                window.location.href = 'index.html';
            }
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no login');
        }
    } catch (err) {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao tentar login.';
    }
};
