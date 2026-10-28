# MeuBolso 💰

Um aplicativo Android simples e intuitivo para controle financeiro pessoal, desenvolvido inteiramente com **Jetpack Compose** e **Kotlin**. O projeto foca em uma interface declarativa, reativa e moderna utilizando as diretrizes do Material Design 3.

## ✨ Funcionalidades

*   **📊 Resumo Financeiro:** Visualização rápida do saldo atual, total de receitas e total de despesas do mês, além de um atalho para as últimas movimentações.
*   **📝 Gerenciamento de Movimentações:** Cadastro, edição e exclusão de receitas e despesas. Inclui integração com o calendário nativo do Android (`DatePickerDialog`) para inserção segura de datas.
*   **📋 Histórico Detalhado:** Lista completa de transações com esquema de cores dinâmicas (Verde para Receitas, Vermelho para Despesas) e filtros rápidos por tipo de movimentação.
*   **📈 Relatórios Inteligentes:** Agrupamento automático de despesas por categoria, exibindo o valor total gasto e a porcentagem que cada categoria representa do total das despesas.
*   **🎨 Tema Personalizado:** Interface customizada com tons de azul e componentes modernos (como `NavigationBar` e `FilterChips`).

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Kotlin
*   **Interface Gráfica:** Jetpack Compose (UI Declarativa)
*   **Arquitetura & Estado:** `ViewModel` com estados reativos (`mutableStateListOf`)
*   **Navegação:** Jetpack Navigation Compose (Single-Activity Architecture)
*   **Design System:** Material Design 3

## 🚀 Como Executar

1. Clone o repositório.
2. Abra o projeto no **Android Studio**.
3. Aguarde a sincronização do Gradle.
4. Execute o aplicativo em um emulador ou dispositivo físico rodando Android.

> **Nota:** Atualmente, os dados são armazenados na memória em tempo de execução para fins de demonstração (requisito do miniprojeto atual).