package com.brunnofdev.meubolso.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoMovimentacao {
    RECEITA, DESPESA
}

@Entity(tableName = "movimentacoes")
data class Movimentacao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val tipo: TipoMovimentacao,
    val categoria: String,
    val data: String
)

