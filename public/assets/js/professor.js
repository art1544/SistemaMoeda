// professor.js

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

document.getElementById('logout-btn').onclick = logout;

const user = JSON.parse(localStorage.getItem('user') || '{}');
const professorInfo = document.getElementById('professor-info');
const studentSelect = document.getElementById('student-select');
const transactionList = document.getElementById('transaction-list');
const transferForm = document.getElementById('transfer-form');
const transferMsg = document.getElementById('transfer-message');
const balanceForm = document.getElementById('balance-form');
const balanceMsg = document.getElementById('balance-message');
const profBalanceValue = document.getElementById('prof-balance-value');

// Verifica se o usuário está logado e é um professor
if (!user.id || !user.roles || !user.roles.some(r => (r.authority || r) === 'ROLE_PROFESSOR')) {
    alert('Acesso negado. Faça login como professor.');
    window.location.href = 'login.html';
}

// Função para atualizar informações do professor
function updateProfessorInfo() {
    fetch(`/api/profile/professor/${user.id}`)
        .then(r => r.json())
        .then(data => {
            // Atualiza saldo grande
            if (profBalanceValue) {
                profBalanceValue.textContent = 'M$ ' + (data.coinBalance || 0);
            }
            // Atualiza info textual se quiser
            if (professorInfo) {
                professorInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
            }
        })
        .catch(err => {
            if (profBalanceValue) profBalanceValue.textContent = 'M$ 0';
            if (professorInfo) professorInfo.textContent = 'Erro ao carregar dados do professor';
        });
}

// Exibe dados do professor
updateProfessorInfo();

// Preenche lista de estudantes
fetch('/api/profile/students')
    .then(r => r.json())
    .then(students => {
        studentSelect.innerHTML = students.map(s => `<option value="${s.id}">${s.name} (${s.email})</option>`).join('');
    })
    .catch(err => {
        console.error('Erro ao carregar estudantes:', err);
        studentSelect.innerHTML = '<option value="">Erro ao carregar estudantes</option>';
    });

// Lista histórico de transações
function loadTransactions() {
    transactionList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/coins/professor/${user.id}/transactions`)
        .then(r => r.json())
        .then(transactions => {
            if (!transactions.length) {
                transactionList.innerHTML = '<li>Nenhuma transação encontrada.</li>';
            } else {
                transactionList.innerHTML = transactions.map(t =>
                    `<li>Para: ${t.receiverStudent ? t.receiverStudent.name : '-'} | Valor: ${t.amount} | Motivo: ${t.reason || '-'} | Data: ${t.date || t.timestamp || '-'} </li>`
                ).join('');
            }
        })
        .catch(err => {
            console.error('Erro ao carregar transações:', err);
            transactionList.innerHTML = '<li>Erro ao carregar transações</li>';
        });
}
loadTransactions();

// Alterar saldo do professor
balanceForm.onsubmit = async function(e) {
    e.preventDefault();
    balanceMsg.textContent = 'Alterando saldo...';
    balanceMsg.style.color = '#333';
    
    const amount = balanceForm.amount.value;
    
    try {
        const res = await fetch(`/api/coins/professor/${user.id}/balance`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ amount: parseFloat(amount) })
        });
        const result = await res.json().catch(() => ({}));
        
        if (res.ok) {
            balanceMsg.style.color = 'green';
            balanceMsg.textContent = result.message || 'Saldo alterado com sucesso!';
            balanceForm.reset();
            updateProfessorInfo();
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

// Transferência de moedas
transferForm.onsubmit = async function(e) {
    e.preventDefault();
    transferMsg.textContent = 'Enviando...';
    transferMsg.style.color = '#333';
    const studentId = studentSelect.value;
    const amount = transferForm.amount.value;
    const reason = transferForm.reason.value;
    
    if (!studentId) {
        transferMsg.style.color = 'red';
        transferMsg.textContent = 'Selecione um estudante';
        return;
    }
    
    try {
        const res = await fetch(`/api/coins/transfer/professor/${user.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ studentId, amount, reason })
        });
        const result = await res.json().catch(() => ({}));
        
        if (res.ok) {
            transferMsg.style.color = 'green';
            transferMsg.textContent = result.message || 'Transferência realizada com sucesso!';
            transferForm.reset();
            loadTransactions();
            updateProfessorInfo();
        } else {
            transferMsg.style.color = 'red';
            let errorMessage = 'Erro na transferência';
            
            if (typeof result === 'object' && result !== null) {
                if (result.error) {
                    errorMessage = result.error;
                } else {
                    errorMessage = Object.values(result).join(' | ');
                }
            } else if (typeof result === 'string') {
                errorMessage = result;
            }
            
            transferMsg.textContent = errorMessage;
        }
    } catch (err) {
        console.error('Erro na transferência:', err);
        transferMsg.style.color = 'red';
        transferMsg.textContent = 'Erro ao transferir moedas.';
    }
};

// Inicialização
window.onload = function() {
    updateProfessorInfo();
    loadTransactions();
};
