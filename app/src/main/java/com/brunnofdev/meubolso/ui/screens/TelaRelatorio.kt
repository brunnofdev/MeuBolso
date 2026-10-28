
package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.TipoMovimentacao
import java.util.Locale.getDefault

@Composable
fun TelaRelatorio(movimentacoes: List<Movimentacao>) {

    val despesas = movimentacoes.filter { it.tipo == TipoMovimentacao.DESPESA }

    val totalDespesas = despesas.sumOf { it.valor }

    val despesasPorCategoria = despesas
        .groupBy { it.categoria.uppercase(getDefault()) }
        .mapValues { entry -> entry.value.sumOf { it.valor } }
        .toList()
        .sortedByDescending { it.second }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Relatório de Despesas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (despesas.isEmpty()) {
            Text(
                text = "Nenhuma despesa cadastrada.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(despesasPorCategoria) { (categoria, totalCategoria) ->

                    val porcentagem = if (totalDespesas > 0) {
                        (totalCategoria / totalDespesas) * 100
                    } else 0.0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = categoria,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${"%.1f".format(porcentagem)}% das despesas",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Text(
                                text = "R$ ${"%.2f".format(totalCategoria)}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}