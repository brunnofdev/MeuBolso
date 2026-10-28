package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.TipoMovimentacao
import com.brunnofdev.meubolso.ui.theme.BackgroundBlue

@Composable
fun TelaResumo(
    saldo: Double,
    receitas: Double,
    despesas: Double,
    ultimasMovimentacoes: List<Movimentacao>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(BackgroundBlue)
    ) {
        Text(
            text = "Resumo Financeiro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Saldo Atual", fontSize = 16.sp)
                Text(
                    text = "R$ ${"%.2f".format(saldo)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Receitas", fontSize = 14.sp)
                    Text(
                        text = "R$ ${"%.2f".format(receitas)}",
                        color = Color(0xFF155724),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8D7DA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Despesas", fontSize = 14.sp)
                    Text(
                        text = "R$ ${"%.2f".format(despesas)}",
                        color = Color(0xFF721C24),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Últimas Movimentações",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ultimasMovimentacoes.takeLast(5).reversed()) { mov ->
                ItemMovimentacao(mov)
            }
        }
    }
}

@Composable
fun ItemMovimentacao(movimentacao: Movimentacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = movimentacao.descricao, fontWeight = FontWeight.Bold)
                Text(
                    text = "${movimentacao.categoria} • ${movimentacao.data}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "R$ ${"%.2f".format(movimentacao.valor)}",
                color = if (movimentacao.tipo == TipoMovimentacao.RECEITA) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

