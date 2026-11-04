package com.brunnofdev.meubolso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.MovimentacaoDao
import com.brunnofdev.meubolso.model.TipoMovimentacao
import com.brunnofdev.meubolso.preferences.PreferenciasDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
 * O ViewModel recebe pelo construtor o DAO do Room e a classe
 * responsável pelas preferências do DataStore.
 *
 * Dessa forma, ele consegue acessar tanto as movimentações
 * financeiras quanto a preferência de ocultação dos valores,
 * sem que a interface precise acessar essas classes diretamente.
 *
 * Também utilizamos o viewModelScope para executar operações
 * assíncronas, mantendo essas tarefas vinculadas ao ciclo
 * de vida do ViewModel.
 */
class MeuBolsoViewModel(private val dao: MovimentacaoDao,
                        private val preferenciasDataStore: PreferenciasDataStore)
    : ViewModel() {

    /*
    * Aqui recuperamos a preferência de ocultação dos valores
    * através do Flow disponibilizado pelo DataStore.
    *
    * Utilizamos stateIn para transformar esse Flow em um StateFlow,
    * que mantém o último valor emitido e permite que a interface
    * consulte diretamente o estado atual da preferência.
    *
    * O viewModelScope define o escopo em que esse fluxo será
    * observado, enquanto o WhileSubscribed determina que a coleta
    * permaneça ativa enquanto houver observadores.
    *
    * O valor 5000 representa um intervalo de cinco segundos:
    * após o último observador deixar de acompanhar o fluxo,
    * a coleta continuará ativa durante esse período antes de parar.
    *
    * O initialValue define o valor inicial do estado enquanto
    * aguarda a primeira emissão do DataStore.
    */
    val ocultarValores: StateFlow<Boolean> =
        preferenciasDataStore.ocultarValores.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /*
     * O DAO disponibiliza um Flow contendo a lista de movimentações
     * armazenadas no Room. Utilizamos stateIn para transformar
     * esse fluxo em um StateFlow, mantendo a lista atual disponível
     * para consulta pela interface.
     *
     * O valor inicial é uma lista vazia, utilizada enquanto
     * aguardamos a primeira emissão do banco de dados.
     *
     * Como o Room observa as alterações na tabela, quando uma
     * movimentação é inserida, atualizada ou excluída, uma nova
     * lista pode ser emitida e o estado será atualizado.
     */
    val movimentacoes: StateFlow<List<Movimentacao>> = dao.obterTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /*
     * Essas funções realizam os cálculos financeiros a partir
     * da lista de movimentações atualmente disponível no ViewModel.
     *
     * Primeiro filtramos os registros de acordo com o tipo
     * da movimentação. Depois, utilizamos sumOf para somar
     * os valores correspondentes.
     *
     * Dessa forma, conseguimos calcular separadamente o total
     * de receitas e o total de despesas sem precisar executar
     * novas consultas diretamente no banco.
     */
    fun calcularReceitas(): Double {
        return movimentacoes.value
            .filter { it.tipo == TipoMovimentacao.RECEITA }
            .sumOf { it.valor }
    }

    fun calcularDespesas(): Double {
        return movimentacoes.value
            .filter { it.tipo == TipoMovimentacao.DESPESA }
            .sumOf { it.valor }
    }

    fun calcularSaldo(): Double {
        return calcularReceitas() - calcularDespesas()
    }

    /*
    * Essas funções são responsáveis por realizar as operações
    * de escrita no banco de dados.
    *
    * Utilizamos viewModelScope.launch para iniciar uma corrotina
    * dentro do escopo do ViewModel. Assim, podemos chamar os
    * métodos suspend do DAO sem bloquear a thread principal.
    *
    * A inserção, atualização e exclusão são delegadas ao DAO,
    * enquanto o Room fica responsável por executar as operações
    * e atualizar o Flow de movimentações quando necessário.
    */
    fun adicionarMovimentacao(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.inserir(movimentacao) }
    }

    fun atualizar(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.atualizar(movimentacao) }
    }

    fun excluir(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.excluir(movimentacao) }
    }

    /*
     * Essa função modifica a preferência de ocultação dos valores.
     *
     * Como salvarOcultacao é uma função suspend do DataStore,
     * utilizamos novamente viewModelScope.launch para executá-la
     * dentro de uma corrotina.
     *
     * Após a gravação, o Flow do DataStore emite o novo valor,
     * que atualiza o StateFlow ocultarValores observado pela interface.
     */
    fun definirOcultacao(ocultar: Boolean) {
        viewModelScope.launch {
            preferenciasDataStore.salvarOcultacao(ocultar)
        }
    }
}

/*
 * [FACTORY]
 *
 * Como o MeuBolsoViewModel recebe dependências no construtor,
 * precisamos fornecer uma maneira de criá-lo com essas instâncias.
 *
 * Essa Factory recebe o DAO e o DataStore e os utiliza para
 * construir o ViewModel quando ele é solicitado pelo Android.
 *
 * Dessa forma, conseguimos inicializar o ViewModel com suas
 * dependências sem precisar instanciá-lo diretamente na Activity.
 */
class MeuBolsoViewModelFactory(
    private val dao: MovimentacaoDao,
    private val preferenciasDataStore: PreferenciasDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        /*
         * Verificamos se a classe solicitada corresponde
         * ao MeuBolsoViewModel.
         *
         * Caso corresponda, criamos uma instância passando
         * o DAO e o DataStore recebidos pela Factory.
         *
         * O cast é necessário porque o método create precisa
         * retornar um tipo genérico de ViewModel.
         */
        if (modelClass.isAssignableFrom(MeuBolsoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeuBolsoViewModel(
                dao,
                preferenciasDataStore
            ) as T
        }

        /*
        * Caso a Factory seja solicitada para uma classe
        * de ViewModel que ela não conhece, lançamos uma exceção.
        */
        throw IllegalArgumentException(
            "Classe ViewModel desconhecida"
        )
    }
}