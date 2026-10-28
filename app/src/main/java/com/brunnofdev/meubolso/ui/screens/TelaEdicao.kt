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

@Composable
fun TelaEdicao(movimentacaoAtual : Movimentacao,
    onSalvar: (Movimentacao) -> Unit, onExcluir: () -> Unit) {

    var descricao by remember { mutableStateOf(movimentacaoAtual.descricao) }
    var valorText by remember { mutableStateOf(movimentacaoAtual.valor.toString().replace(".", ",")) }
    var categoria by remember { mutableStateOf(movimentacaoAtual.categoria) }
    var data by remember { mutableStateOf(movimentacaoAtual.data) }
    var tipo by remember { mutableStateOf(
        if(movimentacaoAtual.tipo == TipoMovimentacao.RECEITA) true else false ) }

    var erroDescricao by remember { mutableStateOf(false) }
    var erroValor by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cadastrar nova Movimentação", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = {
                descricao = it
                if (it.isNotEmpty()) erroDescricao = false
            },
            label = { Text("Descrição *") },
            isError = erroDescricao,
            supportingText = {
                if (erroDescricao) {
                    Text("Informe a descrição", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = valorText,
            onValueChange = {
                valorText = it
                if (it.isNotEmpty()) erroValor = false
            },
            label = { Text("Valor (Ex: 50.00) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = erroValor,
            supportingText = {
                if (erroValor) {
                    Text("Digite um valor numérico válido", color = MaterialTheme.colorScheme.error)
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

        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Tipo de Movimentação *",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !tipo,
                onClick = { tipo = false }
            )
            Text("Despesa", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = tipo,
                onClick = { tipo = true }
            )
            Text("Receita")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val valorTratado = valorText.replace(",", ".").toDoubleOrNull()
                val descricaoValida = descricao.isNotBlank()
                val valorValido = valorTratado != null && valorTratado > 0.0

                val tipoCadastro = if(tipo) TipoMovimentacao.RECEITA else TipoMovimentacao.DESPESA

                erroDescricao = !descricaoValida
                erroValor = !valorValido

                if (descricaoValida && valorValido && valorTratado != null) {
                    onSalvar(
                        Movimentacao(
                            id = movimentacaoAtual.id,
                            descricao = descricao.trim(),
                            valor = valorTratado,
                            categoria = categoria.ifBlank { "Geral" },
                            data = data.ifBlank { "Hoje" },
                            tipo = tipoCadastro
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onExcluir() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Excluir Movimentação")
        }
    }
}