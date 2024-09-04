package com.example.apprememberit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.ui.graphics.Color


@Composable
fun NuevoRecordatorio(onDismiss: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Nuevo Recordatorio", fontSize = 20.sp, color = Color(android.graphics.Color.parseColor("#Ea6d35"))) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campo de Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text(text = "Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text(text = "Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de Fecha
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text(text = "Fecha") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Acciones para guardar los datos aquí
                    onDismiss() // Cierra el popup
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(android.graphics.Color.parseColor("#Ea6d35"))
                )
            ) {
                Text(text = "Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(android.graphics.Color.parseColor("#Ea6d35")) // Color del botón
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = Color(android.graphics.Color.parseColor("#Ea6d35")) // Color del texto
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NuevoRecordatorioPreview() {
    NuevoRecordatorio(onDismiss = {})
}