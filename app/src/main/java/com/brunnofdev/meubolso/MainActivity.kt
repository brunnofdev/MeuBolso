package com.brunnofdev.meubolso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brunnofdev.meubolso.database.AppDatabase
import com.brunnofdev.meubolso.ui.screens.*
import com.brunnofdev.meubolso.viewmodel.MeuBolsoViewModel
import com.brunnofdev.meubolso.viewmodel.MeuBolsoViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.brunnofdev.meubolso.ui.theme.MeuBolsoTheme {
                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val dao = database.movimentacaoDao()

                val viewModel: MeuBolsoViewModel = viewModel(
                    factory = MeuBolsoViewModelFactory(dao)
                )
                MeuBolsoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MeuBolsoApp(viewModel: MeuBolsoViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Coleta a lista reativa do banco de dados Room como estado do Compose
    val listaMovimentacoes by viewModel.movimentacoes.collectAsState()

    Scaffold(
        bottomBar = {
            if (currentRoute != "cadastro") {
                NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
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
                FloatingActionButton(onClick = { navController.navigate("cadastro") }) {
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
                TelaResumo(
                    saldo = viewModel.calcularSaldo(),
                    despesas = viewModel.calcularDespesas(),
                    receitas = viewModel.calcularReceitas(),
                    ultimasMovimentacoes = listaMovimentacoes
                )
            }

            composable("relatorio") {
                TelaRelatorio(
                    movimentacoes = listaMovimentacoes
                )
            }

            composable("historico") {
                TelaHistorico(
                    movimentacoes = listaMovimentacoes,
                    navController = navController
                )
            }

            composable("cadastro") {
                TelaCadastro(
                    onSalvar = { novaMovimentacao ->
                        viewModel.adicionarMovimentacao(novaMovimentacao)
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "edicao/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                val movimentacao = listaMovimentacoes.find { it.id == id }

                if (movimentacao != null) {
                    TelaEdicao(
                        movimentacaoAtual = movimentacao,
                        onSalvar = { editada ->
                            viewModel.atualizar(editada)
                            navController.popBackStack()
                        },
                        onExcluir = {
                            viewModel.excluir(movimentacao)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}