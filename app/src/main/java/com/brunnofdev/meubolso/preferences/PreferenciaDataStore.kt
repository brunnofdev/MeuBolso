package com.brunnofdev.meubolso.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
 * Aqui definido uma propriedade do Context que permite
 * acessar o DataStore utilizado pelo aplicativo.
 *
 * O nome identifica o arquivo onde essas
 * preferências são armazenadas. Essa instância será utilizada tanto
 * para consultar quanto para modificar os valores salvos.
 */
private val Context.dataStore by preferencesDataStore(
    name = "preferencias_usuario"
)

class PreferenciasDataStore(
    private val context: Context
) {
    /*
     * Essa chave será utilizada para identificar
     * a preferência de ocultação dos valores financeiros.
     *
     * Como essa preferência representa apenas dois estados possíveis,
     * utiliza booleanPreferencesKey, permitindo armazenar
     * um valor verdadeiro ou falso.
     */
    private val ocultarValoresKey =
        booleanPreferencesKey("ocultar_valores")

    /*
     * Aqui recupera o valor associado à chave ocultarValoresKey.
     *
     * O DataStore disponibiliza os dados através de um Flow,
     * e utiliza map para acessar especificamente a preferência
     * que queremos.
     *
     * Caso essa chave ainda não exista no armazenamento,
     * utilizamos false como valor padrão. Isso significa que,
     * inicialmente, os valores financeiros ficam visíveis.
     *
     * Como o resultado é um flow, sempre que essa preferência
     * for modificada, um novo valor será emitido, permitindo
     * que a interface acompanhe essa alteração.
     */
    val ocultarValores: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[ocultarValoresKey] ?: false
        }

    /*
    * Essa função recebe um Boolean indicando se os valores
    * financeiros devem ser ocultados ou não.
    *
    * Utiliza edit para modificar as preferências armazenadas,
    * acessando o conjunto de preferências através do parâmetro
    * preferencias.
    *
    * Dentro desse bloco, atualizamos o valor associado à chave
    * ocultarValoresKey com o Boolean recebido pela função.
    *
    * Dessa forma, quando o usuário altera a opção de ocultação,
    * essa mudança é persistida e pode ser recuperada posteriormente.
    *
    * A função é suspend porque a operação de gravação pode
    * suspender a execução da corrotina enquanto é realizada.
    */
    suspend fun salvarOcultacao(ocultar: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ocultarValoresKey] = ocultar
        }
    }
}