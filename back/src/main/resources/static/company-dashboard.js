// Arquivo: src/main/resources/static/company-dashboard.js

document.addEventListener('DOMContentLoaded', function() {

    const companyNameElement = document.getElementById('companyName');
    const companyEmailElement = document.getElementById('companyEmail');
    const companyAdvantagesListElement = document.getElementById('companyAdvantages');
    const transactionHistoryElement = document.getElementById('transactionHistory');
    const logoutButton = document.getElementById('logoutButton');

    // Função para obter o JWT armazenado
   

    // Função para carregar o perfil da empresa
    async function loadCompanyProfile() {

        // TODO: Em uma aplicação real, o ID da empresa logada seria obtido do token JWT ou da sessão
        // Por enquanto, usaremos um ID fixo (não seguro)
        // Para fins de teste, você pode precisar saber o ID de uma empresa no seu banco de dados.
        const companyId = 3; // SUBSTITUA PELO ID REAL DA EMPRESA LOGADA

        try {
            const response = await fetch(`/api/profile/company/${companyId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const company = await response.json();
                companyNameElement.textContent = company.name;
                companyEmailElement.textContent = company.email;
            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 // TODO: Redirecionar para a página de login
                  companyNameElement.textContent = 'Erro: Não autorizado.';
                  companyEmailElement.textContent = 'Erro: Não autorizado.';
            } else {
                const errorMessage = await response.text();
                console.error('Erro ao carregar perfil da empresa:', response.status, errorMessage);
                companyNameElement.textContent = 'Erro ao carregar informações.';
                companyEmailElement.textContent = 'Erro ao carregar informações.';
            }
        } catch (error) {
            console.error('Erro ao conectar com o backend para carregar perfil:', error);
            companyNameElement.textContent = 'Erro de conexão.';
            companyEmailElement.textContent = 'Erro de conexão.';
        }
    }

     // Função para carregar as vantagens oferecidas pela empresa
     // NOTA: Atualmente não temos um endpoint no backend para listar vantagens POR EMPRESA.
     // Este frontend assume que tal endpoint (/api/advantages/company/{companyId}) existirá.
     // Se o backend não tiver esse endpoint, você precisará adaptá-lo ou carregar todas as vantagens e filtrar no frontend (menos eficiente).
     async function loadCompanyAdvantages() {
         const companyId = 3; // SUBSTITUA PELO ID REAL DA EMPRESA LOGADA

         try {
             // TODO: Atualizar URL quando o endpoint existir no backend
             const response = await fetch(`/api/advantages`, { // Assuming /api/advantages lists all and we might filter or backend needs adjustment
                 method: 'GET'             });

             if (response.ok) {
                 let advantages = await response.json();
                 companyAdvantagesListElement.innerHTML = ''; // Limpar lista existente

                 // TODO: Filtrar vantagens por companyId se o endpoint acima retornar todas
                 // advantages = advantages.filter(adv => adv.company.id === companyId);

                 if (advantages.length === 0) {
                     companyAdvantagesListElement.innerHTML = '<li>Nenhuma vantagem cadastrada.</li>';
                 } else {
                     advantages.forEach(advantage => {
                         const listItem = document.createElement('li');
                          // Adapte a exibição com base na estrutura do seu objeto Advantage
                         listItem.innerHTML = `
                             <div class="advantage-details">
                                 <strong>${advantage.name}</strong> - Custo: M$ ${advantage.costInCoins.toFixed(2)}
                                 <p>${advantage.description}</p>
                             </div>
                             <!-- TODO: Adicionar botões de Editar/Excluir se necessário -->
                         `;
                         companyAdvantagesListElement.appendChild(listItem);
                     });
                 }

             } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                  companyAdvantagesListElement.innerHTML = '<li>Erro ao carregar vantagens: Não autorizado.</li>';
                 // TODO: Redirecionar para a página de login
             } else {
                 const errorMessage = await response.text();
                 console.error('Erro ao carregar vantagens da empresa:', response.status, errorMessage);
                 companyAdvantagesListElement.innerHTML = '<li>Erro ao carregar vantagens.</li>';
             }

         } catch (error) {
             console.error('Erro ao conectar com o backend para carregar vantagens:', error);
             companyAdvantagesListElement.innerHTML = '<li>Erro de conexão ao carregar vantagens.</li>';
         }
     }

    // Função para carregar o histórico de transações da empresa (resgates)
      async function loadCompanyTransactionHistory() {
        
           const companyId = 3; // SUBSTITUA PELO ID REAL DA EMPRESA LOGADA

          try {
              const response = await fetch(`/api/profile/company/${companyId}/transactions`, {
                  method: 'GET'
              });

              if (response.ok) {
                  const transactions = await response.json();
                  transactionHistoryElement.innerHTML = ''; // Limpar histórico existente

                  if (transactions.length === 0) {
                      transactionHistoryElement.innerHTML = '<li>Nenhum resgate encontrado.</li>';
                  } else {
                      transactions.forEach(tx => {
                          const listItem = document.createElement('li');
                           // Adapte a exibição com base na estrutura do seu objeto Transaction
                           // Para empresas, as transações são resgates de alunos (senderStudent, receiverCompany)
                          let txDetails = `<strong>${tx.timestamp}</strong>: Resgate de ${tx.amount.toFixed(2)} moedas`;

                          if (tx.senderStudent) {
                               txDetails += ` por ${tx.senderStudent.name || 'Aluno'}`; // Exibe o nome do aluno
                          }
                          if (tx.advantage) {
                                txDetails += ` (Vantagem: ${tx.advantage.name})`; // Exibe o nome da vantagem
                          }

                          listItem.innerHTML = txDetails;
                          transactionHistoryElement.appendChild(listItem);
                      });
                  }

              } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                  transactionHistoryElement.innerHTML = '<li>Erro ao carregar histórico: Não autorizado.</li>';
                 // TODO: Redirecionar para a página de login
             } else {
                  const errorMessage = await response.text();
                  console.error('Erro ao carregar histórico de transações da empresa:', response.status, errorMessage);
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
    loadCompanyProfile();
    loadCompanyAdvantages(); // Necessita de um endpoint no backend para listar vantagens por empresa
    loadCompanyTransactionHistory();

});
