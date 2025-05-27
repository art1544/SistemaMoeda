// Arquivo: src/main/resources/static/cadastro.js

document.addEventListener('DOMContentLoaded', function() {

    const registrationForm = document.getElementById('studentRegistrationForm');
    const registrationError = document.getElementById('registrationError');
    const registrationSuccess = document.getElementById('registrationSuccess');
    const institutionSelect = document.getElementById('institution');

    // Função para limpar mensagens
    function clearMessages() {
        registrationError.textContent = '';
        registrationSuccess.textContent = '';
    }

    // Função para carregar instituições
    async function loadInstitutions() {
        try {
            const response = await fetch('/api/institutions');
            if (response.ok) {
                const institutions = await response.json();
                // Limpar opções existentes (exceto a inicial de carregamento)
                institutionSelect.innerHTML = '';
                 let defaultOption = document.createElement('option');
                 defaultOption.value = '';
                 defaultOption.textContent = 'Selecione uma instituição';
                 institutionSelect.appendChild(defaultOption);

                institutions.forEach(inst => {
                    const option = document.createElement('option');
                    option.value = inst.id;
                    option.textContent = inst.name;
                    institutionSelect.appendChild(option);
                });
            } else {
                console.error('Erro ao carregar instituições:', response.status);
                institutionSelect.innerHTML = '<option value="">Erro ao carregar instituições</option>';
            }
        } catch (error) {
            console.error('Erro ao conectar com o backend para instituições:', error);
             institutionSelect.innerHTML = '<option value="">Erro ao carregar instituições</option>';
        }
    }

     // Carregar instituições ao carregar a página
    loadInstitutions();

    // Adicionar listener para o envio do formulário
    registrationForm.addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevenir o envio padrão do formulário

        clearMessages(); // Limpar mensagens anteriores

        const formData = new FormData(registrationForm);
        const registrationData = Object.fromEntries(formData.entries());

         // Validação básica de email e confirmação de email no frontend
        if (registrationData.email !== registrationData.confirmEmail) {
            registrationError.textContent = 'Os emails não conferem.';
            return;
        }

         // Remover o campo de confirmação de email antes de enviar para o backend
        delete registrationData.confirmEmail;

         // Validação básica de senha e confirmação de senha no frontend
        if (registrationData.password !== registrationData.confirmPassword) {
            registrationError.textContent = 'As senhas não conferem.';
            return;
        }
         // A validação de complexidade de senha será feita no backend com @ValidPassword

        const jsonData = JSON.stringify(registrationData);

        try {
            const response = await fetch('/api/register/student', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData
            });

            if (response.ok) { // Status 200-299
                const result = await response.text(); // Ou response.json() dependendo do backend
                console.log('Cadastro bem-sucedido:', result);
                registrationSuccess.textContent = 'Cadastro realizado com sucesso!';
                registrationForm.reset(); // Limpar o formulário após sucesso
                // TODO: Opcional: Redirecionar para a página de login ou outra página

            } else if (response.status === 400) { // Erros de validação ou outros erros do backend
                const errors = await response.json();
                console.error('Erro no cadastro:', errors);
                // Exibir erros para o usuário
                let errorMessages = 'Erros no cadastro:<br>';
                 if (typeof errors === 'object') {
                    for (const field in errors) {
                         errorMessages += `${field}: ${errors[field]}<br>`;
                     }
                 } else if (typeof errors === 'string') {
                     errorMessages += errors; // Exibir mensagem de erro genérica do backend
                 }
                registrationError.innerHTML = errorMessages;

            } else { // Outros erros HTTP
                 const errorMessage = await response.text();
                 console.error('Erro no cadastro:', response.status, errorMessage);
                 registrationError.textContent = `Erro no cadastro: ${response.status} ${errorMessage}`; // Exibe status e mensagem de erro genérica
            }

        } catch (error) {
            console.error('Erro ao conectar com o backend:', error);
            registrationError.textContent = 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.';
        }
    });
});
