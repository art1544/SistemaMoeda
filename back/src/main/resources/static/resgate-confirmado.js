// Arquivo: src/main/resources/static/resgate-confirmado.js

document.addEventListener('DOMContentLoaded', function() {

    const advantageNameElement = document.getElementById('advantageName');
    const companyNameElement = document.getElementById('companyName');
    const advantageCostElement = document.getElementById('advantageCost');
    const redemptionTimestampElement = document.getElementById('redemptionTimestamp');
    const redemptionCodeElement = document.getElementById('redemptionCode');

    // Função para obter parâmetros da URL
    function getQueryParameter(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    // Obter dados dos parâmetros da URL
    const advantageName = getQueryParameter('advantageName');
    const companyName = getQueryParameter('companyName');
    const advantageCost = getQueryParameter('advantageCost');
    const redemptionTimestamp = getQueryParameter('timestamp');
    const redemptionCode = getQueryParameter('code');

    // Popular os elementos HTML com os dados
    if (advantageName) { advantageNameElement.textContent = advantageName; }
    if (companyName) { companyNameElement.textContent = companyName; }
    if (advantageCost) { advantageCostElement.textContent = parseFloat(advantageCost).toFixed(2); }
    if (redemptionTimestamp) { redemptionTimestampElement.textContent = new Date(redemptionTimestamp).toLocaleString(); } // Formatar data/hora
    if (redemptionCode) { redemptionCodeElement.textContent = redemptionCode; }

     // TODO: Implementar lógica para buscar dados do backend se preferir não passar tudo via query params
     // Exemplo: Se passar apenas o transactionId via query param, buscar os detalhes da transação no backend
     // const transactionId = getQueryParameter('transactionId');
     // if (transactionId) { fetchTransactionDetails(transactionId); }

});

/*
// Exemplo de como a página de resgate (resgatar-vantagens.js) redirecionaria após sucesso:
// window.location.href = `/resgate-confirmado.html?advantageName=${encodeURIComponent(advantage.name)}&companyName=${encodeURIComponent(advantage.company.name)}&advantageCost=${advantage.costInCoins}&timestamp=${new Date().toISOString()}&code=${encodeURIComponent(redemptionCode)}`;
*/
