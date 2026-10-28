
package com.brunnofdev.meubolso.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoData(
    data: String,
    onDataSelecionada: (String) -> Unit
) {
    var mostrarCalendario by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = data,
            onValueChange = {},
            readOnly = true,
            enabled = true,
            label = { Text("Data *") },
            placeholder = { Text("Selecione uma data") },
            trailingIcon = { Text("📅") },
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    mostrarCalendario = true
                }
        )
    }

    if (mostrarCalendario) {
        val estado = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        estado.selectedDateMillis?.let { millis ->
                            val formato = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale("pt", "BR")
                            )
                            formato.timeZone = TimeZone.getTimeZone("UTC")

                            onDataSelecionada(
                                formato.format(Date(millis))
                            )
                        }

                        mostrarCalendario = false
                    },
                    enabled = estado.selectedDateMillis != null
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = estado)
        }
    }
}
