package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.brunnofdev.meubolso.model.Movimentacao

@Composable
fun TelaHistorico(
    movimentacoes: List<Movimentacao>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Mantendo o padrão de espaçamento do Resumo
    ) {
        Text(
            text = "Histórico de Movimentações",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (movimentacoes.isEmpty()) {
            Text(
                text = "Seu histórico está vazio.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else{
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(movimentacoes) { item ->
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
                            Column {
                                Text(
                                    text = item.descricao,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${item.categoria} • ${item.data}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = "R$ ${item.valor}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}