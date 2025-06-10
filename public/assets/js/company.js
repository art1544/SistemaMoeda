// company.js

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

document.getElementById('logout-btn').onclick = logout;

const user = JSON.parse(localStorage.getItem('user') || '{}');
const companyInfo = document.getElementById('company-info');
const advantageForm = document.getElementById('advantage-form');
const advantageMsg = document.getElementById('advantage-message');
const companyAdvantagesList = document.getElementById('company-advantages-list');
const transactionList = document.getElementById('transaction-list');

// Verifica se o usuário está logado e é uma empresa
if (!user.id || !user.roles || !user.roles.some(r => (r.authority || r) === 'ROLE_COMPANY')) {
    alert('Acesso negado. Faça login como empresa.');
    window.location.href = 'login.html';
}

// Função para atualizar informações da empresa
function updateCompanyInfo() {
    fetch(`/api/profile/company/${user.id}`)
        .then(r => r.json())
        .then(data => {
            companyInfo.textContent = `Bem-vindo, ${data.name || user.username || ''}`;
        })
        .catch(err => {
            console.error('Erro ao carregar dados da empresa:', err);
            companyInfo.textContent = 'Erro ao carregar dados da empresa';
        });
}

// Exibe dados da empresa
updateCompanyInfo();

// Lista vantagens da empresa
function loadCompanyAdvantages() {
    companyAdvantagesList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/advantages/company/${user.id}`)
        .then(r => r.json())
        .then(advantages => {
            if (!advantages.length) {
                companyAdvantagesList.innerHTML = '<li>Nenhuma vantagem cadastrada.</li>';
            } else {
                companyAdvantagesList.innerHTML = advantages.map(v =>
                    `<li>
                        <strong>${v.name}</strong> - ${v.description}<br>
                        Custo: ${v.costInCoins} moedas<br>
                        <img src="${v.imageUrl}" alt="${v.name}" style="max-width: 100px; max-height: 100px;"><br>
                        <button onclick="deleteAdvantage(${v.id}, '${v.name.replace(/'/g, '\'')}')">Excluir</button>
                    </li>`
                ).join('');
            }
        })
        .catch(err => {
            console.error('Erro ao carregar vantagens da empresa:', err);
            companyAdvantagesList.innerHTML = '<li>Erro ao carregar vantagens</li>';
        });
}

// Lista histórico de transações
function loadTransactions() {
    transactionList.innerHTML = '<li>Carregando...</li>';
    fetch(`/api/profile/company/${user.id}/transactions`)
        .then(r => r.json())
        .then(transactions => {
            if (!transactions.length) {
                transactionList.innerHTML = '<li>Nenhuma transação encontrada.</li>';
            } else {
                transactionList.innerHTML = transactions.map(t =>
                    `<li>De: ${t.senderStudent ? t.senderStudent.name : '-'} | Valor: ${t.amount} | Motivo: ${t.reason || '-'} | Data: ${t.date || t.timestamp || '-'}${t.used ? ' | Usado: Sim' : ' | Usado: Não'}</li>`
                ).join('');
            }
        })
        .catch(err => {
            console.error('Erro ao carregar transações:', err);
            transactionList.innerHTML = '<li>Erro ao carregar transações</li>';
        });
}

// Cadastrar nova vantagem
advantageForm.onsubmit = async function(e) {
    e.preventDefault();
    advantageMsg.textContent = 'Cadastrando vantagem...';
    advantageMsg.style.color = '#333';
    
    const formData = new FormData(this);
    const advantageData = {
        name: formData.get('name'),
        description: formData.get('description'),
        imageUrl: formData.get('imageUrl'),
        costInCoins: parseFloat(formData.get('costInCoins')),
        companyId: user.id
    };
    
    try {
        const res = await fetch('/api/advantages', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(advantageData)
        });
        const result = await res.json().catch(() => ({}));
        
        if (res.ok) {
            advantageMsg.style.color = 'green';
            advantageMsg.textContent = result.message || 'Vantagem cadastrada com sucesso!';
            advantageForm.reset();
            loadCompanyAdvantages();
        } else {
            advantageMsg.style.color = 'red';
            let errorMessage = 'Erro ao cadastrar vantagem';
            
            if (typeof result === 'object' && result !== null) {
                if (result.error) {
                    errorMessage = result.error;
                } else {
                    errorMessage = Object.values(result).join(' | ');
                }
            } else if (typeof result === 'string') {
                errorMessage = result;
            }
            
            advantageMsg.textContent = errorMessage;
        }
    } catch (err) {
        console.error('Erro ao cadastrar vantagem:', err);
        advantageMsg.style.color = 'red';
        advantageMsg.textContent = 'Erro ao cadastrar vantagem.';
    }
};

// Excluir vantagem
window.deleteAdvantage = function(advantageId, advantageName) {
    if (!confirm(`Deseja realmente excluir a vantagem "${advantageName}"?`)) return;
    
    fetch(`/api/advantages/${advantageId}`, {
        method: 'DELETE'
    })
    .then(async res => {
        const result = await res.json().catch(() => ({}));
        if (res.ok) {
            alert('Vantagem excluída com sucesso!');
            loadCompanyAdvantages();
        } else {
            let errorMessage = 'Erro ao excluir vantagem';
            
            if (typeof result === 'object' && result !== null) {
                if (result.error) {
                    errorMessage = result.error;
                } else {
                    errorMessage = Object.values(result).join(' | ');
                }
            } else if (typeof result === 'string') {
                errorMessage = result;
            }
            
            alert(errorMessage);
        }
    })
    .catch(err => {
        console.error('Erro ao excluir vantagem:', err);
        alert('Erro ao excluir vantagem.');
    });
};

// Inicialização
window.onload = function() {
    loadCompanyAdvantages();
    loadTransactions();
}; 