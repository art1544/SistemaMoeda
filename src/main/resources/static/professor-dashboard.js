// Arquivo: src/main/resources/static/professor-dashboard.js

document.addEventListener('DOMContentLoaded', function() {

    const professorBalanceElement = document.getElementById('professorBalance');
    const transferForm = document.getElementById('transferForm');
    const transferMessageElement = document.getElementById('transferMessage');
    const transferErrorElement = document.getElementById('transferError');
    const transactionHistoryElement = document.getElementById('transactionHistory');
    const logoutButton = document.getElementById('logoutButton');

   

    // Função para limpar mensagens de transferência
    function clearTransferMessages() {
        transferMessageElement.textContent = '';
        transferErrorElement.textContent = '';
    }

    // Função para carregar o perfil do professor (saldo)
    async function loadProfessorProfile() {

        // TODO: Em uma aplicação real, o ID do professor logado seria obtido do token JWT ou da sessão
        // Por enquanto, usaremos um ID fixo ou pediremos para o usuário inserir (não seguro)
        // Para fins de teste, você pode precisar saber o ID de um professor no seu banco de dados.
        const professorId = 1; // SUBSTITUA PELO ID REAL DO PROFESSOR LOGADO

        try {
            const response = await fetch(`/api/profile/professor/${professorId}`, {
                method: 'GET'
                
            });

            if (response.ok) {
                const professor = await response.json();
                professorBalanceElement.textContent = `M$ ${professor.coinBalance.toFixed(2)}`;
            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 // TODO: Redirecionar para a página de login
                 professorBalanceElement.textContent = 'Erro: Não autorizado.';
            } else {
                const errorMessage = await response.text();
                console.error('Erro ao carregar perfil do professor:', response.status, errorMessage);
                professorBalanceElement.textContent = 'Erro ao carregar saldo.';
            }
        } catch (error) {
            console.error('Erro ao conectar com o backend para carregar perfil:', error);
            professorBalanceElement.textContent = 'Erro de conexão.';
        }
    }

    // Função para carregar o histórico de transações do professor
     async function loadTransactionHistory() {
          const professorId = 1; // SUBSTITUA PELO ID REAL DO PROFESSOR LOGADO

         try {
             const response = await fetch(`/api/profile/professor/${professorId}/transactions`, {
                 method: 'GET'
                
             });

             if (response.ok) {
                 const transactions = await response.json();
                 transactionHistoryElement.innerHTML = ''; // Limpar histórico existente

                 if (transactions.length === 0) {
                     transactionHistoryElement.innerHTML = '<li>Nenhuma transação encontrada.</li>';
                 } else {
                     transactions.forEach(tx => {
                         const listItem = document.createElement('li');
                         // Adapte a exibição com base na estrutura do seu objeto Transaction
                         let txDetails = `<strong>${tx.timestamp}</strong>: `;
                         if (tx.senderProfessor && tx.receiverStudent) {
                              txDetails += `Transferiu ${tx.amount.toFixed(2)} moedas para ${tx.receiverStudent.name || 'Aluno'} (Motivo: ${tx.reason})`;
                         } else if (tx.senderStudent && tx.receiverCompany) {
                              txDetails += `Resgatou ${tx.amount.toFixed(2)} moedas na ${tx.receiverCompany.name || 'Empresa'} (Vantagem: ${tx.advantage ? tx.advantage.name : 'N/A'})`;
                         } else {
                             txDetails += `Transação desconhecida de ${tx.amount.toFixed(2)} moedas`;
                         }

                         listItem.innerHTML = txDetails;
                         transactionHistoryElement.appendChild(listItem);
                     });
                 }

             } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 // TODO: Redirecionar para a página de login
                  transactionHistoryElement.innerHTML = '<li>Erro ao carregar histórico: Não autorizado.</li>';
             } else {
                 const errorMessage = await response.text();
                 console.error('Erro ao carregar histórico de transações:', response.status, errorMessage);
                  transactionHistoryElement.innerHTML = '<li>Erro ao carregar histórico.</li>';
             }

         } catch (error) {
             console.error('Erro ao conectar com o backend para carregar histórico:', error);
             transactionHistoryElement.innerHTML = '<li>Erro de conexão ao carregar histórico.</li>';
         }
     }

    // Adicionar listener para o envio do formulário de transferência
    transferForm.addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevenir o envio padrão do formulário

        clearTransferMessages(); // Limpar mensagens anteriores

         const professorId = 1; // SUBSTITUA PELO ID REAL DO PROFESSOR LOGADO

        const formData = new FormData(transferForm);
         // Garantir que amount seja um número (fetch formDataEntries retorna strings)
        const transferData = {
            studentId: parseInt(formData.get('studentId')),
            amount: parseFloat(formData.get('amount')),
            reason: formData.get('reason')
        };

        // Validação básica no frontend (opcional, o backend já valida)
         if (isNaN(transferData.studentId) || isNaN(transferData.amount) || transferData.amount <= 0 || !transferData.reason) {
             transferErrorElement.textContent = 'Por favor, preencha todos os campos corretamente.';
             return;
         }

        const jsonData = JSON.stringify(transferData);

        try {
            const response = await fetch(`/api/coins/transfer/professor/${professorId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData
            });

            if (response.ok) { // Status 200-299
                const result = await response.text();
                console.log('Transferência bem-sucedida:', result);
                transferMessageElement.textContent = 'Transferência realizada com sucesso!';
                transferForm.reset(); // Limpar o formulário
                loadProfessorProfile(); // Recarregar saldo
                loadTransactionHistory(); // Recarregar histórico

            } else if (response.status === 400) { // Erros de validação ou insuficiência de saldo do backend
                const error = await response.text(); // O backend pode retornar uma string ou JSON de erros
                console.error('Erro na transferência:', response.status, error);
                 transferErrorElement.textContent = `Erro na transferência: ${error}`; // Exibe a mensagem de erro do backend

            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 transferErrorElement.textContent = 'Erro: Não autorizado para realizar transferência.';
                 // TODO: Redirecionar para a página de login

            } else { // Outros erros HTTP
                 const errorMessage = await response.text();
                 console.error('Erro na transferência:', response.status, errorMessage);
                 transferErrorElement.textContent = `Erro na transferência: ${response.status} ${errorMessage}`; // Exibe status e mensagem genérica
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend para transferência:', error);
            transferErrorElement.textContent = 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.';
        }
    });

    // Listener para o botão de logout
    logoutButton.addEventListener('click', function() {
        // TODO: Opcional: Invalidar o token no backend se houver suporte a isso
        window.location.href = '/login.html'; // Redirecionar para a página de login
    });


    // Carregar dados iniciais ao carregar a página
    loadProfessorProfile();
    loadTransactionHistory();

});
