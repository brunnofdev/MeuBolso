package com.brunnofdev.meubolso.model

enum class TipoMovimentacao {
    RECEITA, DESPESA
}

data class Movimentacao(
    val id: String = java.util.UUID.randomUUID().toString(),
    val descricao: String,
    val valor: Double,
    val tipo: TipoMovimentacao,
    val categoria: String,
    val data: String
)

