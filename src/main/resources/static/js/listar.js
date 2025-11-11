// Configurações
const API_BASE_URL = 'http://localhost:8080';

// Elementos DOM
const form = document.getElementById("formBuscar");
const mensagem = document.getElementById("mensagem");
const card = document.getElementById("dadosLevantamento");
const camposAdicionais = document.getElementById("camposAdicionais");
const tabelasContainer = document.getElementById("tabelasContainer");

// Inicialização quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    form.addEventListener("submit", handleFormSubmit);
});

// Handlers de Eventos
async function handleFormSubmit(e) {
    e.preventDefault();
    const id = document.getElementById("idLevantamento").value.trim();
    if (!id) return;

    mensagem.textContent = "Carregando...";
    card.style.display = "none";
    tabelasContainer.innerHTML = "";

    try {
        const [pontos, dados] = await Promise.all([
            fetchPontos(id),
            calcularLevantamento(id)
        ]);

        if (!pontos || !dados) {
            mensagem.textContent = "❌ Erro ao buscar dados.";
            return;
        }

        processarDados(pontos, dados);

    } catch (error) {
        console.error("Erro:", error);
        mensagem.textContent = "❌ Erro ao conectar com a API.";
    }
}

// Serviços de API
async function fetchPontos(id) {
    const response = await fetch(`${API_BASE_URL}/ponto/listarPorLevantamento/${id}`);
    if (!response.ok) throw new Error('Erro ao buscar pontos');
    return await response.json();
}

async function calcularLevantamento(id) {
    const response = await fetch(`${API_BASE_URL}/levantamentos/calcular/${id}`, {
        method: "POST"
    });
    if (!response.ok) throw new Error('Erro ao calcular levantamento');
    return await response.json();
}

// Processamento de Dados
function processarDados(pontos, dados) {
    const pontosOrdenados = ordenarPontosNaturalmente(pontos);

    if (dados.tipo === "Caminhamento Irradiado") {
        processarCaminhamentoIrradiado(pontosOrdenados, dados);
    } else {
        processarOutrosTipos(pontosOrdenados, dados);
    }
}

function processarCaminhamentoIrradiado(pontos, dados) {
    const estacoes = pontos.filter(p => !p.estacao);
    const irradiados = pontos.filter(p => p.estacao);

    const estacoesOrdenadas = ordenarPontosNaturalmente(estacoes);
    const irradiadosOrdenados = ordenarPontosNaturalmente(irradiados);

    aplicarReferencias(estacoesOrdenadas);
    aplicarReferencias(irradiadosOrdenados);

    exibirDadosLevantamento(dados);
    renderTabelaEstacoes(estacoesOrdenadas);
    renderTabelaPontos(irradiadosOrdenados, "Pontos Irradiados");

    mensagem.textContent = "";
}

function processarOutrosTipos(pontos, dados) {
    const pontosOrdenados = ordenarPontosNaturalmente(pontos);
    aplicarReferencias(pontosOrdenados);

    exibirDadosLevantamento(dados);
    renderTabelaPontos(pontosOrdenados);

    mensagem.textContent = "";
}

// Utilitários
function ordenarPontosNaturalmente(arr) {
    return [...arr].sort((a, b) => {
        if (!a?.nome) return -1;
        if (!b?.nome) return 1;
        return a.nome.localeCompare(b.nome, undefined, {
            numeric: true,
            sensitivity: 'base'
        });
    });
}

function aplicarReferencias(pontos) {
    if (pontos.length > 0) {
        const ultimoNome = pontos[pontos.length - 1].nome;
        if (!pontos[0].referencia) {
            pontos[0].referencia = ultimoNome;
        }
    }
    return pontos;
}

function decimalParaGMS(decimal) {
    if (decimal == null || isNaN(decimal)) return "-";
    const graus = Math.floor(decimal);
    const minutosDecimais = (decimal - graus) * 60;
    const minutos = Math.floor(minutosDecimais);
    const segundos = ((minutosDecimais - minutos) * 60).toFixed(2);
    return `${graus}° ${minutos}' ${segundos}"`;
}

// Renderização
function exibirDadosLevantamento(dados) {
    card.style.display = "block";
    document.getElementById("levNome").textContent = dados.nome;
    document.getElementById("levTipo").textContent = dados.tipo;
    camposAdicionais.innerHTML = gerarCamposPorTipo(dados);
}

function gerarCamposPorTipo(dados) {
    const templates = {
        "Irradiação": () => `
            <p><strong>Área:</strong> ${dados.area?.toFixed(2) || '0.00'} m²
               <em>(${(dados.area / 10000).toFixed(4)} ha)</em></p>
            <p><strong>Perímetro:</strong> ${dados.perimetro?.toFixed(2) || '0.00'} m</p>
        `,
        "Caminhamento": () => `
            <p><strong>Área:</strong> ${dados.area?.toFixed(2) || '0.00'} m²
               <em>(${(dados.area / 10000).toFixed(4)} ha)</em></p>
            <p><strong>Perímetro:</strong> ${dados.perimetro?.toFixed(2) || '0.00'} m</p>
            <p><strong>Erro Angular:</strong> ${dados.erroAngular?.toExponential(4) || '0.0000'}</p>
            <p><strong>Erro Linear Absoluto:</strong> ${dados.erroLinearAbs?.toExponential(4) || '0.0000'}</p>
            <p><strong>Erro Linear Relativo:</strong> ${dados.erroLinearRelativo?.toExponential(4) || '0.0000'}</p>
        `,
        "Caminhamento Irradiado": () => `
            <p><strong>Perímetro:</strong> ${dados.perimetro?.toFixed(2) || '0.00'} m</p>
            <p><strong>Erro Angular:</strong> ${dados.erroAngular?.toExponential(4) || '0.0000'}</p>
            <p><strong>Erro Linear Absoluto:</strong> ${dados.erroLinearAbs?.toExponential(4) || '0.0000'}</p>
            <p><strong>Erro Linear Relativo:</strong> ${dados.erroLinearRelativo?.toExponential(4) || '0.0000'}</p>
        `
    };

    return templates[dados.tipo] ? templates[dados.tipo]() : "";
}

function renderTabelaPontos(pontos, titulo = "Pontos") {
    const tabelaHTML = `
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <strong>${titulo}</strong>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-bordered align-middle text-center">
                    <thead class="table-dark">
                        <tr>
                            <th>Nome</th>
                            <th>Estação</th>
                            <th>Referência</th>
                            <th>Ângulo Lido</th>
                            <th>Ângulo Hz</th>
                            <th>Distância (m)</th>
                            <th>Azimute</th>
                            <th>Proj. X</th>
                            <th>Proj. Y</th>
                            <th>Coord. X</th>
                            <th>Coord. Y</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${pontos.map(ponto => gerarLinhaTabela(ponto)).join("")}
                    </tbody>
                </table>
            </div>
        </div>`;

    tabelasContainer.insertAdjacentHTML("beforeend", tabelaHTML);
}

function renderTabelaEstacoes(estacoes) {
    const tabelaHTML = `
        <div class="card mb-4">
            <div class="card-header bg-secondary text-white">
                <strong>Estações</strong>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-bordered align-middle text-center">
                    <thead class="table-dark">
                        <tr>
                            <th>Nome</th>
                            <th>Referência</th>
                            <th>Ângulo Lido</th>
                            <th>Ângulo Hz</th>
                            <th>Distância (m)</th>
                            <th>Azimute</th>
                            <th>Proj. X</th>
                            <th>Proj. Y</th>
                            <th>Coord. X</th>
                            <th>Coord. Y</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${estacoes.map(estacao => gerarLinhaTabela(estacao, true)).join("")}
                    </tbody>
                </table>
            </div>
        </div>`;

    tabelasContainer.insertAdjacentHTML("beforeend", tabelaHTML);
}

function gerarLinhaTabela(ponto, isEstacao = false) {
    return `
        <tr>
            <td>${ponto.nome}</td>
            ${!isEstacao ? `<td>${ponto.estacao ?? "-"}</td>` : ""}
            <td>${ponto.referencia ?? "-"}</td>
            <td>${decimalParaGMS(ponto.anguloLido)}</td>
            <td>${decimalParaGMS(ponto.anguloHz)}</td>
            <td>${ponto.distancia?.toFixed(2) ?? "-"}</td>
            <td>${decimalParaGMS(ponto.azimute)}</td>
            <td>${ponto.projX?.toFixed(4) ?? "-"}</td>
            <td>${ponto.projY?.toFixed(4) ?? "-"}</td>
            <td>${ponto.coordX?.toFixed(4) ?? "-"}</td>
            <td>${ponto.coordY?.toFixed(4) ?? "-"}</td>
        </tr>
    `;
}