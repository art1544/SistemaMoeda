// Arquivo: src/main/resources/static/cadastrar-vantagem.js

document.addEventListener('DOMContentLoaded', function() {

    const createAdvantageForm = document.getElementById('createAdvantageForm');
    const createAdvantageMessageElement = document.getElementById('createAdvantageMessage');
    const createAdvantageErrorElement = document.getElementById('createAdvantageError');
    const companyIdInput = document.getElementById('companyId');

    // Função para obter o JWT armazenado
    

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
        createAdvantageMessageElement.textContent = '';
        createAdvantageMessageElement.style.display = 'none';
        createAdvantageErrorElement.textContent = '';
         createAdvantageErrorElement.style.display = 'none';
    }

    // TODO: Em uma aplicação real, o ID da empresa logada seria obtido do token JWT ou da sessão
    // e preenchido automaticamente no campo companyIdInput ANTES de carregar a página,
    // ou passado diretamente no backend sem depender de um campo no formulário.
    // Por enquanto, o HTML tem um valor fixo para teste.
    // const loggedInCompanyId = ...; // Obter o ID da empresa logada
    // if (companyIdInput) { companyIdInput.value = loggedInCompanyId; }


    // Adicionar listener para o envio do formulário de criação de vantagem
    createAdvantageForm.addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevenir o envio padrão do formulário

        clearMessages(); // Limpar mensagens anteriores


        const formData = new FormData(createAdvantageForm);
         // Garantir que costInCoins seja um número
        const advantageData = {
            name: formData.get('name'),
            description: formData.get('description'),
            imageUrl: formData.get('imageUrl'),
            costInCoins: parseFloat(formData.get('costInCoins')),
            companyId: parseInt(formData.get('companyId')) // Obter o ID da empresa (atualmente fixo no HTML)
        };

        // Validação básica no frontend (opcional, o backend já valida)
         if (!advantageData.name || !advantageData.description || !advantageData.imageUrl || isNaN(advantageData.costInCoins) || advantageData.costInCoins <= 0 || isNaN(advantageData.companyId)) {
             showMessage(createAdvantageErrorElement, 'Por favor, preencha todos os campos corretamente.', true);
             return;
         }

        const jsonData = JSON.stringify(advantageData);

        try {
            const response = await fetch('/api/advantages', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData
            });

            if (response.ok) { // Status 200-299
                const createdAdvantage = await response.json(); // Backend retorna o objeto criado
                console.log('Vantagem cadastrada com sucesso:', createdAdvantage);
                showMessage(createAdvantageMessageElement, 'Vantagem cadastrada com sucesso!', false);
                createAdvantageForm.reset(); // Limpar o formulário
                // TODO: Opcional: Redirecionar ou atualizar a lista de vantagens da empresa

            } else if (response.status === 400) { // Erros de validação ou outros erros do backend
                const errors = await response.json(); // O backend retorna JSON com erros
                console.error('Erro no cadastro da vantagem:', errors);
                 let errorMessages = 'Erros no cadastro da vantagem:<br>';
                 if (typeof errors === 'object') {
                    for (const field in errors) {
                         errorMessages += `${field}: ${errors[field]}<br>`;
                     }
                 } else if (typeof errors === 'string') {
                      errorMessages = `Erro: ${errors}`; // Exibir mensagem de erro genérica do backend
                 }
                showMessage(createAdvantageErrorElement, errorMessages, true);

            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                  showMessage(createAdvantageErrorElement, 'Erro: Não autorizado para cadastrar vantagem.', true);
                 // TODO: Redirecionar para a página de login

            } else { // Outros erros HTTP
                 const errorMessage = await response.text();
                 console.error('Erro no cadastro da vantagem:', response.status, errorMessage);
                 showMessage(createAdvantageErrorElement, `Erro no cadastro da vantagem: ${response.status} ${errorMessage}`, true); // Exibe status e mensagem genérica
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend:', error);
             showMessage(createAdvantageErrorElement, 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.', true);
        }
    });
});
