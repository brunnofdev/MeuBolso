package com.brunnofdev.meubolso.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias_usuario"
)

class PreferenciasDataStore(
    private val context: Context
) {
    private val ocultarValoresKey =
        booleanPreferencesKey("ocultar_valores")

    val ocultarValores: Flow<Boolean> =
        context.dataStore.data.map { preferencias ->
            preferencias[ocultarValoresKey] ?: false
        }

    suspend fun salvarOcultacao(ocultar: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ocultarValoresKey] = ocultar
        }
    }
}