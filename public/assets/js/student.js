// student.js

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

document.getElementById('logout-btn').onclick = logout;

const user = JSON.parse(localStorage.getItem('user') || '{}');
const studentInfo = document.getElementById('student-info');
const transactionList = document.getElementById('transaction-list');
const advantageList = document.getElementById('advantage-list');
const redeemMsg = document.getElementById('redeem-message');

// Exibe dados do estudante
fetch(`/api/profile/student/${user.id || ''}`)
    .then(r => r.json())
    .then(data => {
        studentInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
    });

// Lista histórico de transações
function loadTransactions() {
    transactionList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/profile/student/${user.id || ''}/transactions`)
        .then(r => r.json())
        .then(transactions => {
            if (!transactions.length) {
                transactionList.innerHTML = '<li>Nenhuma transação encontrada.</li>';
            } else {
                transactionList.innerHTML = transactions.map(t =>
                    `<li>${t.reason || '-'} | Valor: ${t.amount} | Data: ${t.date || t.timestamp || '-'}${t.redemptionCode ? ' | Código: ' + t.redemptionCode : ''}</li>`
                ).join('');
            }
        });
}

// Lista vantagens disponíveis
function loadAdvantages() {
    advantageList.innerHTML = '<li>Carregando...</li>';
    fetch('/api/advantages')
        .then(r => r.json())
        .then(advantages => {
            if (!advantages.length) {
                advantageList.innerHTML = '<li>Nenhuma vantagem disponível.</li>';
            } else {
                advantageList.innerHTML = advantages.map(v =>
                    `<li>${v.name} - ${v.description} | Custo: ${v.costInCoins} <button onclick="redeemAdvantage(${v.id}, '${v.name.replace(/'/g, '\'')}')">Resgatar</button></li>`
                ).join('');
            }
        });
}

// Resgatar vantagem
window.redeemAdvantage = function(advantageId, advantageName) {
    if (!confirm(`Deseja realmente resgatar a vantagem "${advantageName}"?`)) return;
    redeemMsg.textContent = 'Enviando...';
    redeemMsg.style.color = '#333';
    fetch(`/api/coins/redeem/student/${user.id}`,
        {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ advantageId })
        })
        .then(async res => {
            const result = await res.json().catch(() => ({}));
            if (res.ok) {
                redeemMsg.style.color = 'green';
                redeemMsg.textContent = `Resgate realizado! Código: ${result.redemptionCode || '-'} (guarde este código)`;
                loadTransactions();
                fetch(`/api/profile/student/${user.id || ''}`)
                    .then(r => r.json())
                    .then(data => {
                        studentInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
                    });
            } else {
                redeemMsg.style.color = 'red';
                redeemMsg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro no resgate');
            }
        })
        .catch(() => {
            redeemMsg.style.color = 'red';
            redeemMsg.textContent = 'Erro ao resgatar vantagem.';
        });
};

// Verificar código de resgate
const verifyForm = document.getElementById('verify-form');
verifyForm.onsubmit = function(e) {
    e.preventDefault();
    const code = verifyForm.code.value;
    const msg = document.getElementById('verify-message');
    msg.textContent = 'Verificando...';
    fetch('/api/coins/verify-redemption', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ code })
    })
    .then(async res => {
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            msg.style.color = 'green';
            msg.textContent = `Código válido! Vantagem: ${result.advantage ? result.advantage.name : '-'} | Usado: ${result.used ? 'Sim' : 'Não'}`;
        } else {
            msg.style.color = 'red';
            msg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Código inválido');
        }
    })
    .catch(() => {
        msg.style.color = 'red';
        msg.textContent = 'Erro ao verificar código.';
    });
};

// Inicialização
window.onload = function() {
    loadTransactions();
    loadAdvantages();
};
