// register.js

// Alternância de abas
const tabBtns = document.querySelectorAll('.tab-btn');
const forms = {
    student: document.getElementById('register-student-form'),
    professor: document.getElementById('register-professor-form'),
    company: document.getElementById('register-company-form')
};
tabBtns.forEach(btn => {
    btn.onclick = () => {
        Object.values(forms).forEach(f => f.style.display = 'none');
        forms[btn.dataset.type].style.display = '';
    };
});
// Exibe estudante por padrão
forms.student.style.display = '';

// Registro de estudante
forms.student.onsubmit = async function(e) {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(this));
    const msg = this.querySelector('.register-message');
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    try {
        const res = await fetch('/api/register/student', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Estudante registrado com sucesso!';
            this.reset();
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no registro');
        }
    } catch {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao registrar.';
    }
};
// Registro de professor
forms.professor.onsubmit = async function(e) {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(this));
    const msg = this.querySelector('.register-message');
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    try {
        const res = await fetch('/api/register/professor', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Professor registrado com sucesso!';
            this.reset();
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no registro');
        }
    } catch {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao registrar.';
    }
};
// Registro de empresa
forms.company.onsubmit = async function(e) {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(this));
    const msg = this.querySelector('.register-message');
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    try {
        const res = await fetch('/api/register/company', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Empresa registrada com sucesso!';
            this.reset();
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no registro');
        }
    } catch {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao registrar.';
    }
};
