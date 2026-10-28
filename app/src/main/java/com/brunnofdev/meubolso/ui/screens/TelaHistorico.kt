package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brunnofdev.meubolso.model.Movimentacao

@Composable
fun TelaHistorico(movimentacoes: List<Movimentacao>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Histórico", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (movimentacoes.isEmpty()) {
            Text("Nenhuma movimentação.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(movimentacoes) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(item.descricao, style = MaterialTheme.typography.titleMedium)
                                Text("${item.categoria} • ${item.data}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text("R$ ${item.valor}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}