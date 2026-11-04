package com.brunnofdev.meubolso.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimentacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(movimentacao: Movimentacao)

    @Update
    suspend fun atualizar(movimentacao: Movimentacao)

    @Delete
    suspend fun excluir(movimentacao: Movimentacao)

    @Query("SELECT * FROM movimentacoes ORDER BY id DESC")
    fun obterTodas(): Flow<List<Movimentacao>>

    @Query("SELECT * FROM movimentacoes WHERE id = :id")
    suspend fun obterPorId(id: Int): Movimentacao?
}