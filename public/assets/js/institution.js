// institution.js

document.addEventListener('DOMContentLoaded', function() {
    const list = document.getElementById('institution-list');
    const msg = document.getElementById('institution-message');
    list.innerHTML = '<li>Carregando...</li>';
    fetch('/api/institutions')
        .then(r => r.json())
        .then(institutions => {
            if (!institutions.length) {
                list.innerHTML = '<li>Nenhuma instituição cadastrada.</li>';
            } else {
                list.innerHTML = institutions.map(inst =>
                    `<li><strong>${inst.name}</strong><br>Endereço: ${inst.address || '-'}<br>Telefone: ${inst.phone || '-'}<br>Email: ${inst.email || '-'}</li>`
                ).join('');
            }
        })
        .catch(() => {
            list.innerHTML = '';
            msg.textContent = 'Erro ao carregar instituições.';
            msg.style.color = 'red';
        });
});
