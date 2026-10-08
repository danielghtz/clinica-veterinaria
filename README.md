# 🐾 Clínica Veterinária — Estudo de Caso 6

Aplicação de console em **Kotlin** que simula o atendimento de uma clínica veterinária: cadastro do pet, seleção de serviços em um menu, cálculo de taxas e emissão de extrato.

## ✨ Funcionalidades

- Cadastro interativo do pet (nome, espécie, peso, agressividade e alergias opcionais)
- Menu de serviços com carrinho (cálculo do subtotal)
- Taxa adicional de **R$ 25,00** para pets agressivos
- Tratamento de alergias com Null Safety (`"Nenhuma alergia conhecida"` quando vazio)
- Status do atendimento: `1` Triagem · `2` Em Atendimento · `3` Pronto para Alta
- Extrato final detalhado no console

## 🧠 Conceitos aplicados

| Requisito | Onde foi aplicado |
|-----------|-------------------|
| POO (`data class`) | `Pet`, `Servico`, `Atendimento` |
| Tipos primários | `String`, `Double`, `Boolean`, `Int` |
| `if/else` | `calcularTaxa()` e exibição da taxa no extrato |
| `when` | `descreverStatus()` e validações do menu |
| `for` | `calcularSubtotal()`, menu de serviços e extrato |
| Null Safety | `alergias: String?`, `?.` e `?:` em `descreverAlergias()` |
| Entrada interativa | `readln()` em `lerTexto()`, `lerPeso()`, `lerSimNao()` |

## 🛠️ Tecnologias

- Kotlin 1.6+ (necessário para `readln()`)
- JDK 11 ou superior

## ▶️ Como executar

### Opção 1 — Linha de comando

```bash
kotlinc ClinicaVeterinaria.kt -include-runtime -d clinica.jar
java -jar clinica.jar
```

### Opção 2 — IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ.
2. Abra `ClinicaVeterinaria.kt`.
3. Clique no ícone ▶ ao lado da função `main()`.

## 📋 Exemplo de uso

```text
--- CADASTRO DO PET ---
Nome: Rex
Espécie (ex.: Cachorro, Gato): Cachorro
Peso em kg: 28,4
O pet tem comportamento agressivo? (s/n): s
Alergias conhecidas (ENTER se não houver): Dipirona

Subtotal dos serviços........: R$ 200,00
Taxa de manuseio (agressivo).: R$ 25,00
TOTAL A PAGAR................: R$ 225,00
```

## 📁 Estrutura

```text
.
├── ClinicaVeterinaria.kt
├── .gitignore
└── README.md
```

## 📄 Licença

Projeto acadêmico de estudo. Uso livre para fins educacionais.
