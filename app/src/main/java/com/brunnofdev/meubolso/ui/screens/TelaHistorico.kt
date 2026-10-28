package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.TipoMovimentacao

@Composable
fun TelaHistorico(
    movimentacoes: List<Movimentacao>,
    navController: NavController
)  {

        var filtroSelecionado by remember {
            mutableStateOf<TipoMovimentacao?>(null)
        }

        val movimentacoesFiltradas = movimentacoes.filter { movimentacao ->
            filtroSelecionado == null ||
                    movimentacao.tipo == filtroSelecionado
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Histórico de Movimentações",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filtroSelecionado == null,
                onClick = { filtroSelecionado = null },
                label = { Text("Todas") },
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                selected = filtroSelecionado == TipoMovimentacao.RECEITA,
                onClick = {
                    filtroSelecionado = TipoMovimentacao.RECEITA
                },
                label = { Text("Receitas") },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFD4EDDA),
                    selectedLabelColor = Color(0xFF155724)
                )
            )

            FilterChip(
                selected = filtroSelecionado == TipoMovimentacao.DESPESA,
                onClick = {
                    filtroSelecionado = TipoMovimentacao.DESPESA
                },
                label = { Text("Despesas") },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF8D7DA),
                    selectedLabelColor = Color(0xFF721C24)
                )
            )
        }


        if (movimentacoesFiltradas.isEmpty()) {
            Text(
                text = when (filtroSelecionado) {
                    TipoMovimentacao.RECEITA ->
                        "Nenhuma receita cadastrada."

                    TipoMovimentacao.DESPESA ->
                        "Nenhuma despesa cadastrada."

                    null ->
                        "Seu histórico está vazio."
                },
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movimentacoesFiltradas) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("edicao/${item.id}")
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.descricao,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "${item.categoria} • ${item.data}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = "R$ ${"%.2f".format(item.valor)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = when (item.tipo) {
                                    TipoMovimentacao.RECEITA ->
                                        Color(0xFF2E7D32)

                                    TipoMovimentacao.DESPESA ->
                                        Color(0xFFD32F2F)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}