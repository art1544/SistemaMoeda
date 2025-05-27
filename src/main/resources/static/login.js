// Arquivo: src/main/resources/static/login.js

document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // Prevenir o envio padrão do formulário

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const loginError = document.getElementById('loginError');

    // Limpar mensagens de erro anteriores
    loginError.textContent = '';

    const loginData = {
        usernameOrEmail: email,
        password: password
    };

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) { // Status 200-299
            const jwt = await response.text(); // Ou response.json() se o backend retornar um JSON com o token
            console.log('Login bem-sucedido. JWT:', jwt);

            // Armazenar o JWT (exemplo usando localStorage)
            localStorage.setItem('jwtToken', jwt);

            // TODO: Redirecionar para a página inicial do usuário (dashboard, perfil, etc.)
            // window.location.href = '/dashboard.html'; // Exemplo de redirecionamento
             loginError.textContent = 'Login bem-sucedido! JWT recebido. (Redirecionamento a ser implementado)';

        } else if (response.status === 400) { // Erros de validação
            const errors = await response.json();
            console.error('Erros de validação:', errors);
            // Exibir erros para o usuário
            let errorMessages = 'Erro de validação:<br>';
            for (const field in errors) {
                errorMessages += `${field}: ${errors[field]}<br>`;
            }
            loginError.innerHTML = errorMessages;

        } else if (response.status === 401) { // Credenciais inválidas
             const errorMessage = await response.text();
             console.error('Erro de autenticação:', errorMessage);
             loginError.textContent = errorMessage; // Exibe a mensagem de erro do backend (Invalid username or password)

        } else { // Outros erros HTTP
            const errorMessage = await response.text();
            console.error('Erro no login:', response.status, errorMessage);
            loginError.textContent = `Erro no login: ${response.status} ${errorMessage}`; // Exibe status e mensagem de erro genérica
        }

    } catch (error) {
        console.error('Erro ao conectar com o backend:', error);
        loginError.textContent = 'Erro ao conectar com o servidor. Por favor, tente novamente mais tarde.';
    }
});
