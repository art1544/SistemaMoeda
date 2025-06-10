// forgot.js

document.getElementById('forgot-form').onsubmit = async function(e) {
    e.preventDefault();
    const email = document.getElementById('forgot-email').value;
    const msg = document.getElementById('forgot-message');
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    try {
        const response = await fetch('/api/auth/forgot-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email })
        });
        const result = await response.json().catch(() => ({}));
        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = result.message || 'Se o e-mail estiver cadastrado, você receberá instruções.';
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro ao enviar e-mail.');
        }
    } catch {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao enviar solicitação.';
    }
};
