// Arquivo: src/main/resources/static/student-dashboard.js

document.addEventListener('DOMContentLoaded', function() {

    const studentBalanceElement = document.getElementById('studentBalance');
    const transactionHistoryElement = document.getElementById('transactionHistory');
    const logoutButton = document.getElementById('logoutButton');

    // Função para obter o JWT armazenado
    
    // Função para carregar o perfil do aluno (saldo)
    async function loadStudentProfile() {

        // TODO: Em uma aplicação real, o ID do aluno logado seria obtido do token JWT ou da sessão
        // Por enquanto, usaremos um ID fixo ou pediremos para o usuário inserir (não seguro)
        // Para fins de teste, você pode precisar saber o ID de um aluno no seu banco de dados.
        const studentId = 2; // SUBSTITUA PELO ID REAL DO ALUNO LOGADO

        try {
            const response = await fetch(`/api/profile/student/${studentId}`, {
                method: 'GET'
                
            });

            if (response.ok) {
                const student = await response.json();
                studentBalanceElement.textContent = `M$ ${student.coinBalance.toFixed(2)}`;
            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 // TODO: Redirecionar para a página de login
                 studentBalanceElement.textContent = 'Erro: Não autorizado.';
            } else {
                const errorMessage = await response.text();
                console.error('Erro ao carregar perfil do aluno:', response.status, errorMessage);
                studentBalanceElement.textContent = 'Erro ao carregar saldo.';
            }
        } catch (error) {
            console.error('Erro ao conectar com o backend para carregar perfil:', error);
            studentBalanceElement.textContent = 'Erro de conexão.';
        }
    }

    // Função para carregar o histórico de transações do aluno
     async function loadStudentTransactionHistory() {
         
          const studentId = 2; // SUBSTITUA PELO ID REAL DO ALUNO LOGADO

         try {
             const response = await fetch(`/api/profile/student/${studentId}/transactions`, {
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
                              txDetails += `Recebeu ${tx.amount.toFixed(2)} moedas de ${tx.senderProfessor.name || 'Professor'} (Motivo: ${tx.reason})`;
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

    // Listener para o botão de logout
    logoutButton.addEventListener('click', function() {
        // TODO: Opcional: Invalidar o token no backend se houver suporte a isso
        window.location.href = '/login.html'; // Redirecionar para a página de login
    });

    // Carregar dados iniciais ao carregar a página
    loadStudentProfile();
    loadStudentTransactionHistory();

});
