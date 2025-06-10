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

// Exibe dados do professor
fetch(`/api/profile/professor/${user.id || ''}`)
    .then(r => r.json())
    .then(data => {
        professorInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
    });

// Preenche lista de estudantes
fetch('/api/profile/students')
    .then(r => r.json())
    .then(students => {
        studentSelect.innerHTML = students.map(s => `<option value="${s.id}">${s.name} (${s.email})</option>`).join('');
    });

// Lista histórico de transações
function loadTransactions() {
    transactionList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/profile/professor/${user.id || ''}/transactions`)
        .then(r => r.json())
        .then(transactions => {
            if (!transactions.length) {
                transactionList.innerHTML = '<li>Nenhuma transação encontrada.</li>';
            } else {
                transactionList.innerHTML = transactions.map(t =>
                    `<li>Para: ${t.receiverStudent ? t.receiverStudent.name : '-'} | Valor: ${t.amount} | Motivo: ${t.reason || '-'} | Data: ${t.date || t.timestamp || '-'} </li>`
                ).join('');
            }
        });
}
loadTransactions();

// Transferência de moedas
transferForm.onsubmit = async function(e) {
    e.preventDefault();
    transferMsg.textContent = 'Enviando...';
    transferMsg.style.color = '#333';
    const studentId = studentSelect.value;
    const amount = transferForm.amount.value;
    const reason = transferForm.reason.value;
    try {
        const res = await fetch(`/api/coins/transfer/professor/${user.id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ studentId, amount, reason })
        });
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            transferMsg.style.color = 'green';
            transferMsg.textContent = 'Transferência realizada com sucesso!';
            loadTransactions();
            // Atualiza saldo
            fetch(`/api/profile/professor/${user.id || ''}`)
                .then(r => r.json())
                .then(data => {
                    professorInfo.textContent = `Bem-vindo, ${data.name || user.username || ''} | Saldo: ${data.coinBalance || 0}`;
                });
        } else {
            transferMsg.style.color = 'red';
            transferMsg.textContent = typeof result === 'object' ? Object.values(result).join(' | ') : (result || 'Erro na transferência');
        }
    } catch {
        transferMsg.style.color = 'red';
        transferMsg.textContent = 'Erro ao transferir moedas.';
    }
};
