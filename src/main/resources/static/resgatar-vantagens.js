// Arquivo: src/main/resources/static/resgatar-vantagens.js

document.addEventListener('DOMContentLoaded', function() {

    const advantagesListElement = document.getElementById('advantagesList');
    const advantagesMessageElement = document.getElementById('advantagesMessage');
    const advantagesErrorElement = document.getElementById('advantagesError');

    // Função para obter o JWT armazenado
    function getJwtToken() {
        return localStorage.getItem('jwtToken'); // Ou de cookies, etc.
    }

    // Função para exibir mensagens
    function showMessage(element, message, isError = false) {
        element.textContent = message;
        element.style.display = 'block';
        if (isError) {
             element.style.color = 'red';
        } else {
            element.style.color = 'green';
        }
    }

     // Função para limpar mensagens
    function clearMessages() {
        advantagesMessageElement.textContent = '';
        advantagesMessageElement.style.display = 'none';
        advantagesErrorElement.textContent = '';
         advantagesErrorElement.style.display = 'none';
    }

    // Função para carregar a lista de vantagens
    async function loadAdvantages() {
        const token = getJwtToken();
        if (!token) {
            // TODO: Redirecionar para a página de login se não houver token
            console.error('JWT não encontrado. Redirecionando para login.');
            advantagesListElement.innerHTML = '<p>Erro: Não autenticado. Faça login para ver as vantagens.</p>';
            return;
        }

        try {
            const response = await fetch('/api/advantages', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const advantages = await response.json();
                advantagesListElement.innerHTML = ''; // Limpar lista existente

                if (advantages.length === 0) {
                    advantagesListElement.innerHTML = '<p>Nenhuma vantagem disponível no momento.</p>';
                } else {
                    advantages.forEach(advantage => {
                        const advantageItem = document.createElement('div');
                        advantageItem.classList.add('advantage-item');
                        advantageItem.innerHTML = `
                            <img src="${advantage.imageUrl || 'placeholder.png'}" alt="${advantage.name}">
                            <h3>${advantage.name}</h3>
                            <p>${advantage.description}</p>
                            <p class="cost">Custo: M$ ${advantage.costInCoins.toFixed(2)}</p>
                            <button class="redeem-button" data-advantage-id="${advantage.id}">Resgatar</button>
                        `;
                         advantagesListElement.appendChild(advantageItem);
                    });

                     // Adicionar event listeners para os botões de resgate
                    document.querySelectorAll('.redeem-button').forEach(button => {
                        button.addEventListener('click', handleRedeemClick);
                    });
                }

            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                 advantagesListElement.innerHTML = '<p>Erro ao carregar vantagens: Não autorizado.</p>';
                 // TODO: Redirecionar para a página de login
            } else {
                const errorMessage = await response.text();
                console.error('Erro ao carregar vantagens:', response.status, errorMessage);
                advantagesListElement.innerHTML = '<p>Erro ao carregar vantagens.</p>';
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend para carregar vantagens:', error);
            advantagesListElement.innerHTML = '<p>Erro de conexão ao carregar vantagens.</p>';
        }
    }

    // Função para lidar com o clique no botão Resgatar
    async function handleRedeemClick(event) {
        clearMessages();
        const advantageId = event.target.dataset.advantageId;
         // TODO: Em uma aplicação real, o ID do aluno logado seria obtido do token JWT ou da sessão
        const studentId = 2; // SUBSTITUA PELO ID REAL DO ALUNO LOGADO

        const token = getJwtToken();
         if (!token) {
            console.error('JWT não encontrado. Redirecionando para login.');
             showMessage(advantagesErrorElement, 'Erro: Não autenticado para resgatar vantagem.', true);
            // TODO: Redirecionar para a página de login
            return;
        }

        const redeemData = {
            advantageId: parseInt(advantageId)
        };

        try {
            const response = await fetch(`/api/coins/redeem/student/${studentId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(redeemData)
            });

            if (response.ok) {
                const result = await response.text();
                console.log('Resgate bem-sucedido:', result);
                showMessage(advantagesMessageElement, 'Vantagem resgatada com sucesso!', false);
                // TODO: Opcional: Recarregar saldo do aluno no dashboard ou exibir saldo atualizado aqui
                // TODO: Redirecionar para uma página de confirmação/cupom

            } else if (response.status === 400) { // Erro do backend (ex: saldo insuficiente, vantagem não encontrada)
                 const errorMessage = await response.text(); // O backend pode retornar uma string ou JSON de erros
                 console.error('Erro no resgate:', response.status, errorMessage);
                 showMessage(advantagesErrorElement, `Erro no resgate: ${errorMessage}`, true); // Exibe a mensagem de erro do backend

            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                  showMessage(advantagesErrorElement, 'Erro: Não autorizado para resgatar vantagem.', true);
                 // TODO: Redirecionar para a página de login

            } else {
                 const errorMessage = await response.text();
                 console.error('Erro no resgate:', response.status, errorMessage);
                 showMessage(advantagesErrorElement, `Erro no resgate: ${response.status} ${errorMessage}`, true); // Exibe status e mensagem genérica
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend para resgate:', error);
             showMessage(advantagesErrorElement, 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.', true);
        }
    }

    // Carregar vantagens ao carregar a página
    loadAdvantages();

});
