// Arquivo: src/main/resources/static/resetar-senha.js

document.getElementById('resetPasswordForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevenir o envio padrão do formulário

    const newPassword = document.getElementById('newPassword').value;
    const confirmNewPassword = document.getElementById('confirmNewPassword').value;
    const resetMessage = document.getElementById('resetMessage');
    const resetError = document.getElementById('resetError');

    // Limpar mensagens anteriores
    resetMessage.textContent = '';
    resetError.textContent = '';

     // Validação básica de confirmação de nova senha no frontend
    if (newPassword !== confirmNewPassword) {
        resetError.textContent = 'As novas senhas não conferem.';
        return;
    }
    // A validação de complexidade de senha será feita no backend com @ValidPassword

    const resetData = {
        newPassword: newPassword,
        confirmNewPassword: confirmNewPassword
         // Note: The backend DTO might not need confirmNewPassword if validation is done on the backend side
         // but including it here aligns with a typical form structure.
    };

    try {
        const response = await fetch('/api/auth/reset-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(resetData)
        });

        if (response.ok) { // Status 200-299
            const result = await response.text();
            console.log('Senha resetada com sucesso:', result);
            resetMessage.textContent = 'Sua senha foi resetada com sucesso!';
            document.getElementById('resetPasswordForm').reset(); // Limpar o formulário
             // TODO: Opcional: Redirecionar para a página de login

        } else if (response.status === 400) { // Erro do backend (ex: token inválido/expirado, senhas não conferem)
             const errorMessage = await response.text(); // O backend pode retornar uma mensagem de erro específica
             console.error('Erro ao resetar senha:', response.status, errorMessage);
             resetError.textContent = `Erro: ${errorMessage}`; // Exibe a mensagem de erro do backend

        } else { // Outros erros HTTP
            const errorMessage = await response.text();
            console.error('Erro ao resetar senha:', response.status, errorMessage);
            resetError.textContent = `Erro ao resetar senha: ${response.status} ${errorMessage}`; // Exibe status e mensagem genérica
        }

    } catch (error) {
        console.error('Erro ao conectar com o backend:', error);
        resetError.textContent = 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.';
    }
});
