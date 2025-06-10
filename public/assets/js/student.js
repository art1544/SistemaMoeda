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
const balanceForm = document.getElementById('balance-form');
const balanceMsg = document.getElementById('balance-message');

// Verifica se o usuário está logado e é um estudante
if (!user.id || !user.roles || !user.roles.some(r => (r.authority || r) === 'ROLE_STUDENT')) {
    alert('Acesso negado. Faça login como estudante.');
    window.location.href = 'login.html';
}

// Função para atualizar informações do estudante
function updateStudentInfo() {
    fetch(`/api/profile/student/${user.id}`)
        .then(r => r.json())
        .then(data => {
            studentInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
        })
        .catch(err => {
            console.error('Erro ao carregar dados do estudante:', err);
            studentInfo.textContent = 'Erro ao carregar dados do estudante';
        });
}

// Exibe dados do estudante
updateStudentInfo();

// Lista histórico de transações
function loadTransactions() {
    transactionList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/profile/student/${user.id}/transactions`)
        .then(r => r.json())
        .then(transactions => {
            if (!transactions.length) {
                transactionList.innerHTML = '<li>Nenhuma transação encontrada.</li>';
            } else {
                transactionList.innerHTML = transactions.map(t =>
                    `<li>${t.reason || '-'} | Valor: ${t.amount} | Data: ${t.date || t.timestamp || '-'}${t.redemptionCode ? ' | Código: ' + t.redemptionCode : ''}</li>`
                ).join('');
            }
        })
        .catch(err => {
            console.error('Erro ao carregar transações:', err);
            transactionList.innerHTML = '<li>Erro ao carregar transações</li>';
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
        })
        .catch(err => {
            console.error('Erro ao carregar vantagens:', err);
            advantageList.innerHTML = '<li>Erro ao carregar vantagens</li>';
        });
}

// Alterar saldo do estudante
balanceForm.onsubmit = async function(e) {
    e.preventDefault();
    balanceMsg.textContent = 'Alterando saldo...';
    balanceMsg.style.color = '#333';
    
    const amount = balanceForm.amount.value;
    
    try {
        const res = await fetch(`/api/coins/student/${user.id}/balance`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: parseFloat(amount) })
        });
        const result = await res.json().catch(() => ({}));
        
        if (res.ok) {
            balanceMsg.style.color = 'green';
            balanceMsg.textContent = result.message || 'Saldo alterado com sucesso!';
            balanceForm.reset();
            updateStudentInfo();
        } else {
            balanceMsg.style.color = 'red';
            let errorMessage = 'Erro ao alterar saldo';
            
            if (typeof result === 'object' && result !== null) {
                if (result.error) {
                    errorMessage = result.error;
                } else {
                    errorMessage = Object.values(result).join(' | ');
                }
            } else if (typeof result === 'string') {
                errorMessage = result;
            }
            
            balanceMsg.textContent = errorMessage;
        }
    } catch (err) {
        console.error('Erro ao alterar saldo:', err);
        balanceMsg.style.color = 'red';
        balanceMsg.textContent = 'Erro ao alterar saldo.';
    }
};

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
                updateStudentInfo();
            } else {
                redeemMsg.style.color = 'red';
                let errorMessage = 'Erro no resgate';
                
                if (typeof result === 'object' && result !== null) {
                    if (result.error) {
                        errorMessage = result.error;
                    } else {
                        errorMessage = Object.values(result).join(' | ');
                    }
                } else if (typeof result === 'string') {
                    errorMessage = result;
                }
                
                redeemMsg.textContent = errorMessage;
            }
        })
        .catch(err => {
            console.error('Erro ao resgatar vantagem:', err);
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
            let errorMessage = 'Código inválido';
            
            if (typeof result === 'object' && result !== null) {
                if (result.error) {
                    errorMessage = result.error;
                } else {
                    errorMessage = Object.values(result).join(' | ');
                }
            } else if (typeof result === 'string') {
                errorMessage = result;
            }
            
            msg.textContent = errorMessage;
        }
    })
    .catch(err => {
        console.error('Erro ao verificar código:', err);
        msg.style.color = 'red';
        msg.textContent = 'Erro ao verificar código.';
    });
};

// Inicialização
window.onload = function() {
    loadTransactions();
    loadAdvantages();
};
