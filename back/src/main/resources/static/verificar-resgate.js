// Arquivo: src/main/resources/static/verificar-resgate.js

document.addEventListener('DOMContentLoaded', function() {

    const verifyRedemptionForm = document.getElementById('verifyRedemptionForm');
    const verificationMessageElement = document.getElementById('verificationMessage');
    const verificationErrorElement = document.getElementById('verificationError');

    // Função para obter o JWT armazenado (assumindo que a empresa está logada)
    

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
        verificationMessageElement.textContent = '';
        verificationMessageElement.style.display = 'none';
        verificationErrorElement.textContent = '';
         verificationErrorElement.style.display = 'none';
    }

    // Adicionar listener para o envio do formulário de verificação
    verifyRedemptionForm.addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevenir o envio padrão do formulário

        clearMessages(); // Limpar mensagens anteriores

        const redemptionCode = document.getElementById('redemptionCode').value;

         if (!redemptionCode) {
             showMessage(verificationErrorElement, 'Por favor, insira um código de resgate.', true);
             return;
         }


        const verificationData = {
            code: redemptionCode
             // TODO: Incluir o ID da empresa logada no request se o backend precisar para autorização/filtragem
             // companyId: ...
        };

        try {
            // Atualizado a URL para o endpoint real de verificação no backend
            const response = await fetch('/api/coins/verify-redemption', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(verificationData)
            });

            if (response.ok) { // Assumindo que um status 200-299 significa código válido/verificado
                const verifiedTransaction = await response.json(); // Backend retorna o objeto Transaction
                console.log('Verificação bem-sucedida:', verifiedTransaction);
                 // TODO: Exibir detalhes do resgate validado para a empresa (ex: nome do aluno, vantagem, data)
                 let successMessage = 'Código de resgate válido. Resgate confirmado!';
                 if(verifiedTransaction.senderStudent && verifiedTransaction.advantage) {
                     successMessage += ` Vantagem: ${verifiedTransaction.advantage.name}, Aluno: ${verifiedTransaction.senderStudent.name || 'N/A'}.`;
                 }
                showMessage(verificationMessageElement, successMessage, false);
                document.getElementById('verifyRedemptionForm').reset(); // Limpar o formulário

            } else if (response.status === 400) { // Erro do backend (ex: Código inválido, não encontrado, já usado, etc.)
                 const errorMessage = await response.text(); // O backend deve retornar uma mensagem de erro específica
                 console.error('Erro na verificação do resgate:', response.status, errorMessage);
                 showMessage(verificationErrorElement, `Erro na verificação: ${errorMessage}`, true); // Exibe a mensagem de erro do backend

            } else if (response.status === 401) {
                 console.error('Não autorizado. Token inválido ou expirado.');
                  showMessage(verificationErrorElement, 'Erro: Não autorizado para verificar resgate.', true);
                 // TODO: Redirecionar para a página de login da empresa

            } else { // Outros erros HTTP
                 const errorMessage = await response.text();
                 console.error('Erro na verificação do resgate:', response.status, errorMessage);
                 showMessage(verificationErrorElement, `Erro na verificação: ${response.status} ${errorMessage}`, true); // Exibe status e mensagem genérica
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend:', error);
             showMessage(verificationErrorElement, 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.', true);
        }
    });
});
