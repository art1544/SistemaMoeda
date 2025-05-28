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
            // O backend agora retorna um JSON com info do usuário, não um JWT
            const userInfo = await response.json(); 
            console.log('Login bem-sucedido. Informações do usuário:', userInfo);

            // TODO: Armazenar informações do usuário (não JWT) no localStorage ou session storage se necessário para navegação
            // Exemplo: localStorage.setItem('loggedInUser', JSON.stringify(userInfo));
            // NOTA: Sem JWT, a autenticação em requisições subsequentes não está sendo feita automaticamente.
            // Você precisará gerenciar o estado de login no frontend ou adicionar outra forma de autenticação.

            // Redirecionar para a página apropriada com base no papel (role) do usuário
            // Assumindo que a resposta do backend inclui o papel do usuário, por exemplo, userInfo.roles
            if (userInfo.roles && userInfo.roles.includes('ROLE_PROFESSOR')) {
                window.location.href = '/professor-dashboard.html';
            } else if (userInfo.roles && userInfo.roles.includes('ROLE_STUDENT')) {
                 window.location.href = '/student-dashboard.html';
            } else if (userInfo.roles && userInfo.roles.includes('ROLE_COMPANY')) {
                 window.location.href = '/company-dashboard.html';
            } else {
                 // Papel desconhecido ou não incluído na resposta
                 console.warn('Papel do usuário desconhecido. Redirecionando para página padrão.');
                 loginError.textContent = 'Login bem-sucedido, mas papel do usuário desconhecido.';
                 // Opcional: Redirecionar para uma página padrão ou exibir um erro
            }

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
