// ============================================================
// Estudo de Caso 6: Clínica Veterinária (versão interativa)
// Execute com: kotlinc ClinicaVeterinaria.kt -include-runtime -d clinica.jar
//              java -jar clinica.jar
// ============================================================

import java.util.Locale

// ============================================================
// [Commit 1] Modelagem de dados (POO com data class)
// ============================================================

// POO + Tipos primários (String, Double, Boolean)
// Null Safety: 'alergias' é String? (opcional / nullable)
data class Pet(
    val nome: String,
    val especie: String,
    val peso: Double,
    val isAgressivo: Boolean,
    val alergias: String? = null
)

data class Servico(
    val descricao: String,
    val preco: Double
)

// Relaciona o Pet, os serviços prestados e o código de status (Int)
data class Atendimento(
    val pet: Pet,
    val servicos: List<Servico>,
    val status: Int
)

// ============================================================
// [Commit 2] Carrinho de serviços e taxa de agressividade
// ============================================================

const val TAXA_AGRESSIVIDADE = 25.0

// LAÇO 'for': itera sobre os serviços executados para calcular o subtotal
fun calcularSubtotal(servicos: List<Servico>): Double {
    var subtotal = 0.0
    for (servico in servicos) {
        subtotal += servico.preco
    }
    return subtotal
}

// CONDICIONAL 'if/else': taxa extra de R$ 25,00 para pets agressivos
fun calcularTaxa(pet: Pet): Double {
    return if (pet.isAgressivo) {
        TAXA_AGRESSIVIDADE
    } else {
        0.0
    }
}

fun calcularTotal(atendimento: Atendimento): Double =
    calcularSubtotal(atendimento.servicos) + calcularTaxa(atendimento.pet)

// ============================================================
// [Commit 3] Null Safety (alergias) e status com 'when'
// ============================================================

// NULL SAFETY: chamada segura (?.) + operador Elvis (?:)
// Nulo, vazio ou em branco -> "Nenhuma alergia conhecida"
fun descreverAlergias(pet: Pet): String =
    pet.alergias?.trim()?.takeIf { it.isNotEmpty() } ?: "Nenhuma alergia conhecida"

// CONDICIONAL 'when': mapeia o código do status para a mensagem
fun descreverStatus(codigo: Int): String {
    return when (codigo) {
        1 -> "Triagem"
        2 -> "Em Atendimento"
        3 -> "Pronto para Alta"
        else -> "Status Desconhecido"
    }
}

// ============================================================
// [Commit 4] Utilitários de formatação e entrada (readln)
// ============================================================

val LOCALE_BR: Locale = Locale.forLanguageTag("pt-BR")

fun moeda(valor: Double): String =
    "R$ " + String.format(LOCALE_BR, "%,.2f", valor)

// Lê um texto obrigatório (repete até o usuário digitar algo)
fun lerTexto(mensagem: String): String {
    while (true) {
        print(mensagem)
        val entrada = readln().trim()
        if (entrada.isNotEmpty()) return entrada
        println("  ⚠ Campo obrigatório. Tente novamente.")
    }
}

// Lê um Double positivo (aceita vírgula ou ponto)
fun lerPeso(mensagem: String): Double {
    while (true) {
        print(mensagem)
        val peso = readln().trim().replace(',', '.').toDoubleOrNull()
        if (peso != null && peso > 0) return peso
        println("  ⚠ Informe um número maior que zero (ex.: 12,5).")
    }
}

// Lê uma resposta Sim/Não e devolve um Boolean
fun lerSimNao(mensagem: String): Boolean {
    while (true) {
        print("$mensagem (s/n): ")
        when (readln().trim().lowercase()) {
            "s", "sim" -> return true
            "n", "nao", "não" -> return false
            else -> println("  ⚠ Responda com 's' ou 'n'.")
        }
    }
}

// Campo opcional: devolve null se o usuário apenas apertar ENTER
fun lerAlergiasOpcional(): String? {
    print("Alergias conhecidas (ENTER se não houver): ")
    return readln().trim().ifEmpty { null }
}

// ============================================================
// [Commit 4] Cadastro do pet e menu de serviços (interativo)
// ============================================================

val CATALOGO = listOf(
    Servico("Consulta clínica", 120.0),
    Servico("Vacina antirrábica", 80.0),
    Servico("Banho e tosa", 65.5),
    Servico("Exame de sangue", 150.0),
    Servico("Vermifugação", 45.0),
    Servico("Raio-X", 200.0)
)

fun cadastrarPet(): Pet {
    println("\n--- CADASTRO DO PET ---")
    val nome = lerTexto("Nome: ")
    val especie = lerTexto("Espécie (ex.: Cachorro, Gato): ")
    val peso = lerPeso("Peso em kg: ")
    val agressivo = lerSimNao("O pet tem comportamento agressivo?")
    val alergias = lerAlergiasOpcional()
    return Pet(nome, especie, peso, agressivo, alergias)
}

fun selecionarServicos(): List<Servico> {
    val escolhidos = mutableListOf<Servico>()

    while (true) {
        println("\n--- MENU DE SERVIÇOS ---")
        // LAÇO 'for': percorre o catálogo para montar o menu
        for ((indice, servico) in CATALOGO.withIndex()) {
            println("  ${indice + 1}. ${servico.descricao.padEnd(22)} ${moeda(servico.preco)}")
        }
        println("  0. Finalizar seleção")
        println("Serviços no carrinho: ${escolhidos.size}")
        print("Escolha uma opção: ")

        val opcao = readln().trim().toIntOrNull()

        when {
            opcao == 0 -> {
                if (escolhidos.isEmpty()) {
                    println("  ⚠ Selecione ao menos um serviço antes de finalizar.")
                } else {
                    return escolhidos
                }
            }
            opcao != null && opcao in 1..CATALOGO.size -> {
                val servico = CATALOGO[opcao - 1]
                escolhidos.add(servico)
                println("  ✔ '${servico.descricao}' adicionado.")
            }
            else -> println("  ⚠ Opção inválida.")
        }
    }
}

// ============================================================
// [Commit 4] Extrato final do atendimento
// ============================================================

fun exibirExtrato(atendimento: Atendimento) {
    val pet = atendimento.pet
    val subtotal = calcularSubtotal(atendimento.servicos)  // for
    val taxa = calcularTaxa(pet)                           // if/else
    val total = subtotal + taxa

    println("\n==================================================")
    println("          CLÍNICA VETERINÁRIA - EXTRATO")
    println("==================================================")
    println("Pet.........: ${pet.nome}")
    println("Espécie.....: ${pet.especie}")
    println("Peso........: ${String.format(LOCALE_BR, "%.1f", pet.peso)} kg")
    println("Agressivo...: ${if (pet.isAgressivo) "Sim" else "Não"}")
    println("Alergias....: ${descreverAlergias(pet)}")                      // ?. e ?:
    println("Status......: ${atendimento.status} - ${descreverStatus(atendimento.status)}")  // when
    println("--------------------------------------------------")
    println("SERVIÇOS PRESTADOS")

    // LAÇO 'for': detalha cada serviço executado
    for ((indice, servico) in atendimento.servicos.withIndex()) {
        println("  ${indice + 1}. ${servico.descricao.padEnd(24)} ${moeda(servico.preco)}")
    }

    println("--------------------------------------------------")
    println("Subtotal dos serviços........: ${moeda(subtotal)}")
    if (pet.isAgressivo) {
        println("Taxa de manuseio (agressivo).: ${moeda(taxa)}")
    } else {
        println("Taxa de manuseio.............: não aplicável")
    }
    println("TOTAL A PAGAR................: ${moeda(total)}")
    println("==================================================")
}

// ============================================================
// [Commit 4] Fluxo do atendimento (status evolui: 1 -> 2 -> 3)
// ============================================================

fun executarAtendimento() {
    val pet = cadastrarPet()

    // Status 1: Triagem (pet acabou de ser cadastrado)
    var atendimento = Atendimento(pet, emptyList(), status = 1)
    println("\nStatus atual: ${descreverStatus(atendimento.status)}")

    // Status 2: Em Atendimento (serviços sendo selecionados)
    atendimento = atendimento.copy(status = 2)
    println("Status atual: ${descreverStatus(atendimento.status)}")
    val servicos = selecionarServicos()
    atendimento = atendimento.copy(servicos = servicos)

    // Status 3: Pronto para Alta (serviços concluídos)
    atendimento = atendimento.copy(status = 3)
    println("\nStatus atual: ${descreverStatus(atendimento.status)}")

    exibirExtrato(atendimento)
}

fun main() {
    println("==================================================")
    println("     BEM-VINDO AO SISTEMA DA CLÍNICA VETERINÁRIA")
    println("==================================================")

    do {
        executarAtendimento()
    } while (lerSimNao("\nDeseja registrar outro atendimento?"))

    println("\nEncerrando o sistema. Até logo!")
}
