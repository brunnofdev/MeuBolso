package com.brunnofdev.meubolso.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*
 * O Room converte automaticamente as anotações em código SQL.
 */
@Dao
interface MovimentacaoDao {

    /*
     * Esse método insere uma nova movimentação no banco.
     *
     * Utiliza suspend porque essa operação pode suspender
     * a execução da corrotina enquanto o banco realiza a gravação.
     * O Room executa esse tipo de operação fora da thread principal,
     * evitando que a interface fique bloqueada durante o acesso ao banco.
     *
     * O OnConflictStrategy.REPLACE define que, caso ocorra um conflito
     * de chave, o registro existente será substituído.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(movimentacao: Movimentacao)

    /*
    * Ele recebe o objeto Movimentacao com os dados modificados,
    * e o Room utiliza sua chave primária para identificar
    * qual registro deve ser atualizado.
    */
    @Update
    suspend fun atualizar(movimentacao: Movimentacao)

    /*
     * Recebe o objeto Movimentacao que queremos remover,
     * e o Room identifica o registro através da chave primária.
     */
    @Delete
    suspend fun excluir(movimentacao: Movimentacao)

    /*
     * Esse método consulta todas as movimentações armazenadas,
     * organizando os resultados pelo id em ordem decrescente.
     * Dessa forma, os registros mais recentes aparecem primeiro.
     *
     * Ele não utiliza suspend
     * já que o retorno é um flow, que permite
     * observar os resultados dessa consulta ao longo do tempo.
     *
     * Sempre que a tabela de movimentações sofrer uma alteração
     * relevante, o Room executa novamente a consulta e emite
     * uma nova lista.
     *
     * Dessa forma a interface pode observar esse flow e
     * atualizar a exibição das movimentações sem precisar
     * realizar manualmente uma nova consulta.
     */
    @Query("SELECT * FROM movimentacoes ORDER BY id DESC")
    fun obterTodas(): Flow<List<Movimentacao>>

    /*
    * Esse método consulta uma movimentação específica
    * utilizando seu ID.
    *
    * O retorno é nullable, representado pelo ?, porque
    * pode acontecer de não existir um registro com o ID informado.
    */
    @Query("SELECT * FROM movimentacoes WHERE id = :id")
    suspend fun obterPorId(id: Int): Movimentacao?
}