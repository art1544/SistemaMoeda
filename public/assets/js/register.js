// register.js

const userTypeSelect = document.getElementById('user-type-select');
const fieldsStudent = document.getElementById('fields-student');
const fieldsProfessor = document.getElementById('fields-professor');
const fieldsCompany = document.getElementById('fields-company');
const form = document.getElementById('register-form');
const msg = form.querySelector('.register-message');

function showFields(type) {
    fieldsStudent.style.display = type === 'student' ? '' : 'none';
    fieldsProfessor.style.display = type === 'professor' ? '' : 'none';
    fieldsCompany.style.display = type === 'company' ? '' : 'none';
}

userTypeSelect.addEventListener('change', function() {
    showFields(this.value);
});

// Exibe estudante por padrão
showFields(userTypeSelect.value);

function getVisibleFieldsData(type) {
    let data = {};
    let container = null;
    if (type === 'student') container = fieldsStudent;
    if (type === 'professor') container = fieldsProfessor;
    if (type === 'company') container = fieldsCompany;
    if (!container) return {};
    const inputs = container.querySelectorAll('input, select');
    inputs.forEach(input => {
        if (input.type === 'checkbox') {
            data[input.name] = input.checked;
        } else {
            data[input.name] = input.value;
        }
    });
    return data;
}

form.onsubmit = async function(e) {
    e.preventDefault();
    msg.textContent = 'Enviando...';
    msg.style.color = '#333';
    const type = userTypeSelect.value;
    let data = getVisibleFieldsData(type);
    let endpoint = '';
    if (type === 'student') {
        endpoint = '/api/register/student';
    } else if (type === 'professor') {
        endpoint = '/api/register/professor';
    } else if (type === 'company') {
        endpoint = '/api/register/company';
    }
    try {
        const res = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = (type === 'student' ? 'Estudante' : type === 'professor' ? 'Professor' : 'Empresa') + ' registrado com sucesso!';
            form.reset();
            showFields(userTypeSelect.value);
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no registro');
        }
    } catch {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao registrar.';
    }
};
