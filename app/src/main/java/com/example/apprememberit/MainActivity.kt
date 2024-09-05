package com.example.apprememberit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.node.CanFocusChecker.start //COMTENTADO TEMPORALMENTE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.NonDisposableHandle.parent

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import com.example.apprememberit.ViewModel.RecordatorioViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Dashboard()
        }
    }
}

@Preview
@Composable
fun Dashboard(viewModel: RecordatorioViewModel = viewModel()) {
    var recordatorioEditando by rememberSaveable { mutableStateOf<Recordatorio?>(null) }
    var recordatorioAEliminar by rememberSaveable { mutableStateOf<Recordatorio?>(null) }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#f8eeec"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (topImg, profile) = createRefs()
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .constrainAs(topImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(android.graphics.Color.parseColor("#EA6D35")),
                                Color(android.graphics.Color.parseColor("#3b608c"))
                            )
                        ),
                        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                    )
            )
            Row(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(start = 14.dp)
                        .weight(0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Bienvenido", color = Color.White, fontSize = 18.sp
                    )
                    Text(
                        text = "{Nombre de usuario}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                    .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                    .constrainAs(profile) {
                        top.linkTo(topImg.bottom)
                        bottom.linkTo(topImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, start = 200.dp)
                        .height(90.dp)
                        .width(90.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor("#ffe0c8")),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_7),
                        contentDescription = null,
                        Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    Text(
                        text = "Ajustes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color(android.graphics.Color.parseColor("#c77710"))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Panel de recordatorios
        Text(
            text = "Tus Recordatorios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        // Lista de recordatorios desde el ViewModel
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.recordatorios) { recordatorio ->
                RecordatorioCard(
                    recordatorio = recordatorio,
                    onDelete = {
                        // Mostrar cuadro de confirmación antes de eliminar
                        recordatorioAEliminar = recordatorio
                    },
                    onEdit = {
                        // Marcar el recordatorio como editando
                        recordatorioEditando = recordatorio
                    }
                )
            }
        }

        // Si hay un recordatorio en modo edición, mostrar el diálogo para editarlo
        recordatorioEditando?.let { recordatorio ->
            EditRecordatorioDialog(
                recordatorio = recordatorio,
                onDismiss = { recordatorioEditando = null },
                onSave = { nuevoTitulo, nuevaDescripcion, nuevaFecha, nuevaHora ->
                    viewModel.actualizarRecordatorio(
                        recordatorio,
                        Recordatorio(nuevoTitulo, nuevaDescripcion, nuevaFecha, nuevaHora, recordatorio.emailUsuario)
                    )
                    recordatorioEditando = null
                }
            )
        }

        // Si hay un recordatorio marcado para eliminar, mostrar el cuadro de confirmación
        recordatorioAEliminar?.let { recordatorio ->
            ConfirmDeleteDialog(
                onConfirm = {
                    viewModel.eliminarRecordatorio(recordatorio)
                    recordatorioAEliminar = null
                },
                onDismiss = {
                    recordatorioAEliminar = null
                }
            )
        }


    }
}

// Definición de la data class para los recordatorios
data class Recordatorio(val titulo: String, val descripcion: String, val fecha: String, val hora: String, val emailUsuario: String)

@Composable
fun RecordatorioCard(recordatorio: Recordatorio, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = recordatorio.titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = recordatorio.descripcion, fontSize = 16.sp, color = Color.Gray)
            Text(text = "Fecha: ${recordatorio.fecha} - Hora: ${recordatorio.hora}", fontSize = 14.sp, color = Color.Gray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text(text = "Eliminar", color = Color.Red)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onEdit) {
                    Text(text = "Editar", color = Color(android.graphics.Color.parseColor("#3b608c")))
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirmar Eliminación", fontSize = 20.sp, color = Color.Red)
        },
        text = {
            Text(text = "¿Estás seguro de que deseas eliminar este recordatorio?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text(text = "Eliminar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar", color = Color.Gray)
            }
        }
    )
}


@Composable
fun EditRecordatorioDialog(
    recordatorio: Recordatorio,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf(recordatorio.titulo) }
    var descripcion by rememberSaveable { mutableStateOf(recordatorio.descripcion) }
    var fecha by rememberSaveable { mutableStateOf(recordatorio.fecha) }
    var hora by rememberSaveable { mutableStateOf(recordatorio.hora) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nuevo Recordatorio",
                fontSize = 20.sp,
                color = Color(android.graphics.Color.parseColor("#Ea6d35"))
            )
        },
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

                // Campo de Hora
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text(text = "Hora") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(titulo, descripcion, fecha, hora) // Guardar los datos y cerrar
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
                    contentColor = Color(android.graphics.Color.parseColor("#Ea6d35"))
                )
            ) {
                Text(
                    text = "Cancelar",
                    color = Color(android.graphics.Color.parseColor("#Ea6d35"))
                )
            }
        }
    )
}

