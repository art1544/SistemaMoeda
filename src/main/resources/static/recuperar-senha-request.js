// Arquivo: src/main/resources/static/recuperar-senha-request.js

document.getElementById('forgotPasswordForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevenir o envio padrão do formulário

    const email = document.getElementById('email').value;
    const recoveryMessage = document.getElementById('recoveryMessage');
     const recoveryError = document.getElementById('recoveryError');

    // Limpar mensagens anteriores
    recoveryMessage.textContent = '';
     recoveryError.textContent = '';

    const requestData = {
        email: email
    };

    try {
        const response = await fetch('/api/auth/forgot-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (response.ok) { // Status 200-299
            const result = await response.text();
            console.log('Solicitação de recuperação enviada:', result);
            recoveryMessage.textContent = 'Se um usuário com este email for encontrado, um link de recuperação será enviado.'; // Mensagem genérica por segurança

        } else if (response.status === 400) { // Erro do backend (ex: usuário não encontrado, erro de validação)
             const errorMessage = await response.text(); // O backend pode retornar uma mensagem de erro específica
             console.error('Erro na solicitação de recuperação:', response.status, errorMessage);
             recoveryError.textContent = `Erro: ${errorMessage}`; // Exibe a mensagem de erro do backend

        } else { // Outros erros HTTP
            const errorMessage = await response.text();
            console.error('Erro na solicitação de recuperação:', response.status, errorMessage);
            recoveryError.textContent = `Erro ao enviar solicitação de recuperação: ${response.status} ${errorMessage}`; // Exibe status e mensagem genérica
        }

    } catch (error) {
        console.error('Erro ao conectar com o backend:', error);
        recoveryError.textContent = 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.';
    }
});
