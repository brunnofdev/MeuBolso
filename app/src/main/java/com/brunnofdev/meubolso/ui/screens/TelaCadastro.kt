
package com.brunnofdev.meubolso.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brunnofdev.meubolso.model.Movimentacao
import com.brunnofdev.meubolso.model.TipoMovimentacao
import com.brunnofdev.meubolso.ui.components.CampoData

@Composable
fun TelaCadastro(onSalvar: (Movimentacao) -> Unit) {
    var descricao by remember { mutableStateOf("") }
    var valorText by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(false) }

    var erroDescricao by remember { mutableStateOf(false) }
    var erroValor by remember { mutableStateOf(false) }
    var erroData by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Cadastrar nova movimentação",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = {
                descricao = it
                if (it.isNotBlank()) erroDescricao = false
            },
            label = { Text("Descrição *") },
            isError = erroDescricao,
            supportingText = {
                if (erroDescricao) {
                    Text("Informe a descrição")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = valorText,
            onValueChange = {
                valorText = it
                if (it.isNotBlank()) erroValor = false
            },
            label = { Text("Valor *") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            isError = erroValor,
            supportingText = {
                if (erroValor) {
                    Text("Digite um valor maior que zero")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        CampoData(
            data = data,
            onDataSelecionada = {
                data = it
                erroData = false
            }
        )

        if (erroData) {
            Text(
                text = "Selecione uma data.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "Tipo de movimentação *",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !tipo,
                onClick = { tipo = false }
            )
            Text(
                "Despesa",
                modifier = Modifier.padding(end = 16.dp)
            )

            RadioButton(
                selected = tipo,
                onClick = { tipo = true }
            )
            Text("Receita")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val valorTratado = valorText
                    .replace(",", ".")
                    .toDoubleOrNull()

                val descricaoValida = descricao.isNotBlank()
                val valorValido = valorTratado != null && valorTratado > 0.0
                val dataValida = data.isNotBlank()

                erroDescricao = !descricaoValida
                erroValor = !valorValido
                erroData = !dataValida

                if (
                    descricaoValida &&
                    valorValido &&
                    dataValida &&
                    valorTratado != null
                ) {
                    val tipoCadastro =
                        if (tipo) {
                            TipoMovimentacao.RECEITA
                        } else {
                            TipoMovimentacao.DESPESA
                        }

                    onSalvar(
                        Movimentacao(
                            descricao = descricao.trim(),
                            valor = valorTratado,
                            categoria = categoria.ifBlank { "Geral" },
                            data = data,
                            tipo = tipoCadastro
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
