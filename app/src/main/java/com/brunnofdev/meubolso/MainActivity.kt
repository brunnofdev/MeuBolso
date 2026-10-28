package com.brunnofdev.meubolso

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.brunnofdev.meubolso.ui.screens.TelaResumo
import com.brunnofdev.meubolso.ui.theme.MeuBolsoTheme
import com.brunnofdev.meubolso.viewmodel.MeuBolsoViewModel
import kotlin.collections.emptyList

class MainActivity : ComponentActivity() {

    private val viewModel: MeuBolsoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeuBolsoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val movimentacoes = viewModel.movimentacoes

                    TelaResumo(
                        saldo = viewModel.calcularSaldo(),
                        receitas = viewModel.calcularReceitas(),
                        despesas = viewModel.calcularDespesas(),
                        ultimasMovimentacoes = movimentacoes
                    )
                }
            }
        }
    }
}