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

class MeuBolsoViewModel(private val dao: MovimentacaoDao,
                        private val preferenciasDataStore: PreferenciasDataStore)
                        : ViewModel() {

    val ocultarValores: StateFlow<Boolean> =
        preferenciasDataStore.ocultarValores.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    val movimentacoes: StateFlow<List<Movimentacao>> = dao.obterTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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


    fun adicionarMovimentacao(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.inserir(movimentacao) }
    }

    fun atualizar(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.atualizar(movimentacao) }
    }

    fun excluir(movimentacao: Movimentacao) {
        viewModelScope.launch { dao.excluir(movimentacao) }
    }

    fun definirOcultacao(ocultar: Boolean) {
        viewModelScope.launch {
            preferenciasDataStore.salvarOcultacao(ocultar)
        }
    }
}

class MeuBolsoViewModelFactory(
    private val dao: MovimentacaoDao,
    private val preferenciasDataStore: PreferenciasDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MeuBolsoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeuBolsoViewModel(
                dao,
                preferenciasDataStore
            ) as T
        }

        throw IllegalArgumentException(
            "Classe ViewModel desconhecida"
        )
    }
}