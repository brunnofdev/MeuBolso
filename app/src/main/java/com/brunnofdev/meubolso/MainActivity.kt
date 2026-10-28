package com.brunnofdev.meubolso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.brunnofdev.meubolso.ui.screens.*
import com.brunnofdev.meubolso.viewmodel.MeuBolsoViewModel
import com.brunnofdev.meubolso.ui.theme.BackgroundBlue
import com.brunnofdev.meubolso.ui.theme.PrimaryBlue
import com.brunnofdev.meubolso.ui.theme.IncomeGreen
import com.brunnofdev.meubolso.ui.theme.ExpenseRed
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.brunnofdev.meubolso.ui.theme.MeuBolsoTheme{
            MeuBolsoApp()
        }}
    }
}

@Composable
fun MeuBolsoApp(viewModel: MeuBolsoViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

                if (currentRoute != "cadastro") {
                NavigationBar (containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Resumo") },
                        label = { Text("Resumo") },
                        selected = currentRoute == "resumo",
                        onClick = { navController.navigate("resumo") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Histórico") },
                        label = { Text("Histórico") },
                        selected = currentRoute == "historico",
                        onClick = { navController.navigate("historico") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Relatório") },
                        label = { Text("Relatório") },
                        selected = currentRoute == "relatorio",
                        onClick = { navController.navigate("relatorio") }
                    )
                }
                }
            },

        floatingActionButton = {
            if (currentRoute == "resumo" || currentRoute == "historico") {
                FloatingActionButton( { navController.navigate("cadastro") }) {
                    Icon(Icons.Default.Add, contentDescription = "Nova Movimentação")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "resumo",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("resumo") {
                TelaResumo(saldo = viewModel.calcularSaldo(),
                    despesas = viewModel.calcularDespesas(),
                    receitas = viewModel.calcularReceitas(),
                    ultimasMovimentacoes = viewModel.movimentacoes)
            }

            composable("relatorio") {
                TelaRelatorio(
                    movimentacoes = viewModel.movimentacoes
                )
            }

            composable("historico") {
                TelaHistorico(movimentacoes = viewModel.movimentacoes, navController = navController)

        }
            composable("cadastro") {
                TelaCadastro(
                    onSalvar = { novaMovimentacao ->
                        viewModel.adicionarMovimentacao(novaMovimentacao)
                        navController.popBackStack()
                    }
                )
            }
            composable("edicao/{id}") { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("id")
                val movimentacao = viewModel.movimentacoes.find { it.id == id }

                if (movimentacao != null) {
                    TelaEdicao(
                        movimentacaoAtual = movimentacao,
                        onSalvar = { editada ->
                            viewModel.editarMovimentacao(id!!, editada)
                            navController.popBackStack()
                        },
                        onExcluir = {
                            viewModel.excluirMovimentacao(id!!)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
