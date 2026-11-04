package com.brunnofdev.meubolso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.MovimentacaoDao
import com.brunnofdev.meubolso.util.Converters

/*
* Essa classe é responsável por configurar o banco de dados utilizando o Room.
 * O Room trabalha sobre o SQLite, mas permite utilizar classes Kotlin e
 * anotações para definir as tabelas e as operações do banco.
 *
 * Na anotação @Database, informamos quais entidades fazem parte do banco.
 * Nesse caso, temos a entidade Movimentacao, que representa a tabela onde
 * serão armazenadas as receitas e despesas do usuário.
 *
 * Também utiliza @TypeConverters, porque alguns tipos utilizados
 * no Kotlin não são armazenados diretamente pelo SQLite.
 * O conversor permite transformar o Enum de tipo de
 * movimentação em um formato que o banco consiga armazenar.
 */
@Database(entities = [Movimentacao::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Conecta a base de dados às funções de consulta (DAO)
    abstract fun movimentacaoDao(): MovimentacaoDao

    companion object {
        /*
          * Aqui utilizamos o padrão Singleton para garantir que o
         * aplicativo trabalhe com uma única instância do banco de dados.
         *
         * A variável INSTANCE armazena essa instância. O @Volatile
         * permite que as alterações feitas nela sejam visíveis para
         * as diferentes threads que acessam essa variável.
         *
         * O synchronized é utilizado para controlar o acesso durante
         * a criação do banco. Assim, se duas threads tentarem criar
         * a instância ao mesmo tempo, apenas uma poderá executar
         * esse trecho por vez.
         *
         * Isso evita a criação de várias instâncias desnecessárias
         * do banco de dados durante a execução do aplicativo.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
        * Essa função é responsável por fornecer a instância do banco.
        *
        * Primeiro verificamos se ela já foi criada. Caso ainda não
        * exista, utilizamos o synchronized para criar a instância
        * de forma segura.
        *
        * O applicationContext é utilizado no lugar do contexto
        * de uma Activity, porque o banco precisa acompanhar o
        * ciclo de vida do aplicativo, e não de uma tela específica.
        *
        * O nome serve para identificar o arquivo do banco
        * que será armazenado localmente no dispositivo.
        */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, // Usa o contexto da App e não da Activity (evita vazamentos)
                    AppDatabase::class.java,
                    "meubolso_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}