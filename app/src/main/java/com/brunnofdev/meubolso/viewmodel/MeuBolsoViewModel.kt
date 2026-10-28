package com.brunnofdev.meubolso.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.TipoMovimentacao

class MeuBolsoViewModel : ViewModel() {

    private val _movimentacoes = mutableStateListOf<Movimentacao>() //privada e mutável
    val movimentacoes: List<Movimentacao> = _movimentacoes //pública

    fun calcularReceitas(): Double {
        return _movimentacoes
            .filter { it.tipo == TipoMovimentacao.RECEITA }
            .sumOf { it.valor }
    }
    fun calcularDespesas(): Double {
        return _movimentacoes
            .filter { it.tipo == TipoMovimentacao.DESPESA }
            .sumOf { it.valor }
    }

    fun calcularSaldo(): Double {
        return calcularReceitas() - calcularDespesas()
    }

    fun adicionarMovimentacao(movimentacao: Movimentacao) {
        _movimentacoes.add(movimentacao)
    }

    fun editarMovimentacao(id: String, novaMovimentacao: Movimentacao) {
        val index = movimentacoes.indexOfFirst { it.id == id }
        if (index != -1) {
            _movimentacoes[index] = novaMovimentacao // A substituição atualiza a UI automaticamente
        }
    }

    fun excluirMovimentacao(id: String) {
        _movimentacoes.removeAll { it.id == id }
    }

}