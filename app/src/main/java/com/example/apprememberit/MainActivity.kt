package com.example.apprememberit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import com.example.apprememberit.ViewModel.FirebaseViewModel
import com.example.apprememberit.ViewModel.Recordatorio
import com.example.apprememberit.ViewModel.RecordatorioViewModel
import java.util.UUID
import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

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
fun Dashboard() {
    val context = LocalContext.current

    //Se utilizan 2 Viewmodels, uno para Firebase (usuarios con cuenta) y otro para SharedPreferences (usuarios sin cuenta)
    val firebaseViewModel: FirebaseViewModel = remember { FirebaseViewModel(context) }
    val recordatorioViewModel: RecordatorioViewModel = remember { RecordatorioViewModel(context) }

    var recordatorioEditando by rememberSaveable { mutableStateOf<Recordatorio?>(null) }
    var recordatorioAEliminar by rememberSaveable { mutableStateOf<Recordatorio?>(null) }
    var mostrarNuevoRecordatorio by rememberSaveable { mutableStateOf(false) }
    var mensajeBienvenida by remember { mutableStateOf("Bienvenido") }

    val usuarioActual = FirebaseAuth.getInstance().currentUser
    val emailUsuario = usuarioActual?.email

    //Uso de SharedPreferences si no hay usuario autenticado
    val usandoSharedPreferences = emailUsuario == null

    //Generar un UUID temporal si no hay usuario autenticado
    var uuidSinCuenta by rememberSaveable { mutableStateOf<String?>(null) }

    if (emailUsuario == null && uuidSinCuenta == null) {
        uuidSinCuenta = UUID.randomUUID().toString()
    }

    val identificadorUsuario = emailUsuario ?: uuidSinCuenta

    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm")
    var recordatorios by rememberSaveable { mutableStateOf(listOf<Recordatorio>()) }

    //Refrescar los recordatorios, usa Firebase o SharedPreferences según el estado del usuario
    fun refrescarRecordatorios() {
        if (!usandoSharedPreferences) {
            //Si el usuario está autenticado, usamos Firebase
            identificadorUsuario?.let { userId ->
                obtenerRecordatoriosDesdeFirebase(context, userId, firebaseViewModel) { listaRecordatorios ->
                    recordatorios = listaRecordatorios.sortedBy { recordatorio ->
                        try {
                            LocalDateTime.parse("${recordatorio.fecha} ${recordatorio.hora}", formatter)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al formatear fecha u hora", Toast.LENGTH_LONG).show()
                            LocalDateTime.now()
                        }
                    }
                }
            }
        } else {
            recordatorios = recordatorioViewModel.obtenerRecordatoriosSinCuenta()
        }
    }
    //---------- Reconocimiento de voz ----------//

    //Snackbar para visualizar mensajes del reconocimiento de voz
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val reconocimientoVozHelper = remember {
        ReconocimientoVozHelper(
            context = context,
            onResult = { recognizedText ->
                snackbarMessage = recognizedText
                showSnackbar = true

                coroutineScope.launch {
                    delay(5000)
                    showSnackbar = false
                }

                //Nuevo recordatorio
                if (recognizedText.equals("Agregar recordatorio", ignoreCase = true) ||
                    recognizedText.equals("Nuevo recordatorio", ignoreCase = true) ||
                    recognizedText.equals("Agregar", ignoreCase = true) ||
                    recognizedText.equals("Nuevo", ignoreCase = true)) {

                    mostrarNuevoRecordatorio = true
                    Toast.makeText(context, "Abriendo Nuevo Recordatorio", Toast.LENGTH_SHORT).show()
                }

                //Cerrar aplicación
                if (recognizedText.equals("Salir", ignoreCase = true) ||
                    recognizedText.equals("Cerrar", ignoreCase = true) ||
                    recognizedText.equals("Cerrar aplicación", ignoreCase = true)) {

                    Toast.makeText(context, "Cerrando aplicación...", Toast.LENGTH_SHORT).show()

                    //Cerrar aplicación
                    (context as Activity).finishAffinity()
                    exitProcess(0)
                }
            },
            onError = {
                snackbarMessage = "Error al reconocer voz"
                showSnackbar = true

                coroutineScope.launch {
                    delay(5000)
                    showSnackbar = false
                }
            },
            onListening = {
                snackbarMessage = "Escuchando..."
                showSnackbar = true
            },
            onProcessing = {
                snackbarMessage = "Procesando..."
                showSnackbar = true
            }
        )
    }

    //---------- Mensaje de bienvenida y panel de recordatorios ----------//
    LaunchedEffect(identificadorUsuario) {
        if (emailUsuario != null) {
            firebaseViewModel.obtenerUsuarioPorEmail(emailUsuario) { usuario, errorMessage ->
                if (usuario != null) {
                    mensajeBienvenida = "Hola, ${usuario.nombre}"
                } else {
                    mensajeBienvenida = "Hola, $emailUsuario"
                    errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            mensajeBienvenida = "Bienvenido/a"
        }

        refrescarRecordatorios()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#f8eeec")))
    ) {
        Column(Modifier.fillMaxSize()) {
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
                            text = mensajeBienvenida,
                            color = Color.White,
                            fontSize = 35.sp,
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
                    //Menú
                    AbrirMenu(
                        context = context,
                        onLogout = {
                            val email = obtenerEmailDeSharedPreferences(context)

                            if (!email.isNullOrEmpty()) {
                                cerrarSesion(context)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al obtener el email del usuario",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            //Redirigir al LoginActivity
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        },
                        onLogin = {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        },
                        onRegister = {
                            val intent = Intent(context, RegisterActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Título
            Text(
                text = "Tus Recordatorios",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            //Mostrar lista de recordatorios o mensaje de no hay recordatorios
            if (recordatorios.isEmpty()) {
                Text(
                    text = "No tienes recordatorios. ¡Agrega uno nuevo!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recordatorios) { recordatorio ->
                        RecordatorioCard(
                            recordatorio = recordatorio,
                            onDelete = {
                                recordatorioAEliminar = recordatorio
                            },
                            onEdit = {
                                recordatorioEditando = recordatorio
                            }
                        )
                    }
                }
            }

            //---------- Editar recordatorio ----------//
            recordatorioEditando?.let { recordatorio ->
                EditRecordatorioDialog(
                    recordatorio = recordatorio,
                    onDismiss = { recordatorioEditando = null },
                    emailUsuario = emailUsuario,
                    firebaseViewModel = if (!usandoSharedPreferences) firebaseViewModel else null,
                    recordatorioViewModel = if (usandoSharedPreferences) recordatorioViewModel else null,
                    onSave = {
                        recordatorioEditando = null
                        refrescarRecordatorios()
                    }
                )
            }


            ///---------- Eliminar recordatorio ----------//
            recordatorioAEliminar?.let { recordatorio ->
                ConfirmDeleteDialog(
                    recordatorio = recordatorio,
                    onConfirm = {
                        if (usandoSharedPreferences) {
                            //Eliminar desde SharedPreferences
                            recordatorioViewModel.eliminarRecordatorio(recordatorio)
                            Toast.makeText(context, "Recordatorio eliminado correctamente", Toast.LENGTH_LONG).show()
                        } else {
                            //Eliminar desde Firebase
                            firebaseViewModel.eliminarRecordatorio(recordatorio) { success, errorMessage ->
                                if (success) {
                                    Toast.makeText(context, "Recordatorio eliminado correctamente", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Error al eliminar: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        refrescarRecordatorios()
                        recordatorioAEliminar = null
                    },
                    onDismiss = {
                        recordatorioAEliminar = null
                    }
                )
            }
        }
        //---------- Nuevo Recordatorio ----------//
        if (mostrarNuevoRecordatorio) {
            NuevoRecordatorioDialog(
                onDismiss = { mostrarNuevoRecordatorio = false },
                firebaseViewModel = if (!usandoSharedPreferences) firebaseViewModel else null,  // Pasar firebaseViewModel si no se usan SharedPreferences
                recordatorioViewModel = if (usandoSharedPreferences) recordatorioViewModel else null,  // Pasar recordatorioViewModel si se usan SharedPreferences
                emailUsuario = if (usandoSharedPreferences) null else emailUsuario,  // Enviar null si se usan SharedPreferences
                onSave = {
                    mostrarNuevoRecordatorio = false
                    refrescarRecordatorios()
                }
            )
        }

        //---------- Botón "Hablar" para reconocimiento de voz ----------//
        ExtendedFloatingActionButton(
            onClick = {
                snackbarMessage = "Escuchando..."
                showSnackbar = true
                //Verificar permiso para usar el micrófono
                reconocimientoVozHelper.startListening()
            },
            backgroundColor = Color(android.graphics.Color.parseColor("#3b608c")),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(56.dp)
                .width(160.dp)
                .offset(x = (-180).dp),
            icon = {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Hablar",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            text = {
                Text(
                    text = "Hablar",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 0.dp)
                )
            }
        )
        //---------- Botón "+ Nuevo" para agregar recordatorios ----------//
        ExtendedFloatingActionButton(
            onClick = { mostrarNuevoRecordatorio = true },
            backgroundColor = Color(android.graphics.Color.parseColor("#EA6D35")),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .height(56.dp)
                .width(160.dp),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            },
            text = {
                Text(
                    text = "Nuevo",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 0.dp)
                )
            }
        )
        //---------- Snackbar para mostrar el estado de reconocimiento de voz ----------//
        if (showSnackbar) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = Color.Black.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        Text(
                            text = snackbarMessage,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }

    }
}


//----------Panel de recordatorios----------//
@Composable
fun RecordatorioCard(recordatorio: Recordatorio, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = recordatorio.titulo, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(text = recordatorio.descripcion, fontSize = 23.sp, color = Color.Gray)
            Text(text = "Fecha: ${recordatorio.fecha} - ${recordatorio.hora}", fontSize = 22.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(android.graphics.Color.parseColor("#3b608c")),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

//----------Dialog "Confirmar Eliminación"----------//
@Composable
fun ConfirmDeleteDialog(
    recordatorio: Recordatorio,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit // La lógica de eliminación se pasa desde fuera
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Confirmar Eliminación", fontSize = 27.sp, color = Color.Red)
        },
        text = {
            Text(text = "¿Está seguro de que desea eliminar el recordatorio \"${recordatorio.titulo}\"?", fontSize = 20.sp)
        },
        confirmButton = {
            Button(
                onClick = onConfirm, // Se delega la acción de confirmación afuera
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text(text = "Eliminar", color = Color.White, fontSize = 18.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar", color = Color.Gray, fontSize = 18.sp)
            }
        }
    )
}

@Composable
fun EditRecordatorioDialog(recordatorio: Recordatorio, onDismiss: () -> Unit, emailUsuario: String?, firebaseViewModel: FirebaseViewModel?, recordatorioViewModel: RecordatorioViewModel?, onSave: () -> Unit
) {
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(recordatorio.titulo) }
    var descripcion by rememberSaveable { mutableStateOf(recordatorio.descripcion) }
    var fecha by rememberSaveable { mutableStateOf(recordatorio.fecha) }
    var hora by rememberSaveable { mutableStateOf(recordatorio.hora) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            hora = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Editar Recordatorio",
                fontSize = 27.sp,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //Selector de categorías de recordatorios
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                ) {
                    TextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (firebaseViewModel?.categorias ?: recordatorioViewModel?.categorias)?.forEach { categoria ->
                            DropdownMenuItem(onClick = {
                                categoriaSeleccionada = categoria
                                expanded = false
                            }) {
                                Text(text = categoria)
                            }
                        }
                    }
                }

                //Campo "Descripción"
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text(text = "Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                //Campo "Fecha"
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { },
                    label = { Text(text = "Fecha") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            datePickerDialog.show()
                        },
                    isError = fecha.isBlank()
                )

                //Campo "Hora"
                OutlinedTextField(
                    value = hora,
                    onValueChange = { },
                    label = { Text(text = "Hora") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            timePickerDialog.show()
                        },
                    isError = hora.isBlank()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevoRecordatorio = recordatorio.copy(
                        titulo = categoriaSeleccionada,
                        descripcion = descripcion,
                        fecha = fecha,
                        hora = hora
                    )

                    // Validar si el usuario tiene cuenta o no
                    if (emailUsuario != null) {
                        // Usuario con cuenta, utilizar Firebase
                        firebaseViewModel?.actualizarRecordatorio(recordatorio, nuevoRecordatorio) { success, errorMessage ->
                            if (success) {
                                Toast.makeText(context, "Recordatorio actualizado correctamente", Toast.LENGTH_LONG).show()
                                onSave()
                            } else {
                                Toast.makeText(context, "Error al actualizar: $errorMessage", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        // Usuario sin cuenta, utilizar SharedPreferences
                        recordatorioViewModel?.actualizarRecordatorio(recordatorio, nuevoRecordatorio)
                        Toast.makeText(context, "Recordatorio actualizado correctamente", Toast.LENGTH_LONG).show()
                        onSave()
                    }
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
                Text(text = "Cancelar", color = Color(android.graphics.Color.parseColor("#Ea6d35")))
            }
        }
    )
}

//----------Dialog "Nuevo Recordatorio"----------//
@Composable
fun NuevoRecordatorioDialog(onDismiss: () -> Unit, firebaseViewModel: FirebaseViewModel?, recordatorioViewModel: RecordatorioViewModel?, emailUsuario: String?, onSave: () -> Unit
) {
    var categoriaSeleccionada by rememberSaveable { mutableStateOf("Seleccione") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            hora = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

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
                // Selector de categorías de recordatorios
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                ) {
                    TextField(
                        value = categoriaSeleccionada,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = !expanded }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (firebaseViewModel?.categorias ?: recordatorioViewModel?.categorias ?: emptyList()).forEach { categoria ->
                            DropdownMenuItem(onClick = {
                                categoriaSeleccionada = categoria
                                expanded = false
                            }) {
                                Text(text = categoria)
                            }
                        }
                    }
                }

                // Campo "Descripción"
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text(text = "Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = descripcion.isBlank()
                )

                // Campo "Fecha"
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text(text = "Fecha") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            datePickerDialog.show()
                        },
                    isError = fecha.isBlank()
                )

                // Campo "Hora"
                OutlinedTextField(
                    value = hora,
                    onValueChange = {},
                    label = { Text(text = "Hora") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            timePickerDialog.show()
                        },
                    isError = hora.isBlank()
                )

                // Mostrar mensaje de error si hay algún campo inválido
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validar que todos los campos sean válidos
                    if (categoriaSeleccionada == "Seleccione") {
                        errorMessage = "Debe seleccionar una categoría."
                    } else if (descripcion.isBlank()) {
                        errorMessage = "La descripción es obligatoria."
                    } else if (fecha.isBlank()) {
                        errorMessage = "Debe seleccionar una fecha."
                    } else if (hora.isBlank()) {
                        errorMessage = "Debe seleccionar una hora."
                    } else {
                        errorMessage = ""
                        val nuevoRecordatorio = Recordatorio(
                            id = if (firebaseViewModel != null) firebaseViewModel.generarId() else null,
                            titulo = categoriaSeleccionada,
                            descripcion = descripcion,
                            fecha = fecha,
                            hora = hora,
                            emailUsuario = emailUsuario ?: ""
                        )

                        if (firebaseViewModel != null) {
                            // Agregar el recordatorio a Firebase
                            firebaseViewModel.agregarRecordatorio(nuevoRecordatorio) { success, errorMessage ->
                                if (success) {
                                    Toast.makeText(context, "Recordatorio guardado correctamente", Toast.LENGTH_LONG).show()
                                    onSave()
                                } else {
                                    Toast.makeText(context, "Error al guardar: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else if (recordatorioViewModel != null) {
                            // Agregar el recordatorio en SharedPreferences
                            recordatorioViewModel.agregarRecordatorio(nuevoRecordatorio)
                            Toast.makeText(context, "Recordatorio guardado correctamente", Toast.LENGTH_LONG).show()
                            onSave()
                        }
                    }
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
                Text(text = "Cancelar", color = Color(android.graphics.Color.parseColor("#Ea6d35")))
            }
        }
    )
}

//----------Menú "Ajustes"----------//
@Composable
fun AbrirMenu(context: Context, onLogout: () -> Unit, onLogin: () -> Unit, onRegister: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // Obtener el usuario actual desde Firebase Authentication
    val usuarioActual = FirebaseAuth.getInstance().currentUser

    // Las opciones a mostrar en el menú dependen del estado de la sesión.
    // 1) Usuario activo, muestra "Cerrar sesión".
    // 2) Sin usuario activo, muestra las opciones "Crear cuenta" e "Iniciar sesión"
    Column(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp, start = 200.dp)
            .height(80.dp)
            .width(80.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#ffe0c8")),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { expanded = true },
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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            //Si el usuario está autenticado, mostrar la opción de "Cerrar sesión"
            if (usuarioActual != null) {
                DropdownMenuItem(onClick = {
                    onLogout() // Cerrar sesión
                    expanded = false
                }) {
                    Text("Cerrar sesión")
                }
            } else {
                //Si no hay usuario autenticado, mostrar "Crear cuenta" e "Iniciar sesión"
                DropdownMenuItem(onClick = {
                    onRegister() //Ir a pantalla de registro
                    expanded = false
                }) {
                    Text("Crear cuenta")
                }
                DropdownMenuItem(onClick = {
                    onLogin() //Ir a pantalla de inicio de sesión
                    expanded = false
                }) {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}

//----------Métodos privados----------//
private fun cerrarSesion(context: Context) {
    //Cerrar sesión con Firebase Authentication
    FirebaseAuth.getInstance().signOut()

    //Eliminar el email de SharedPreferences
    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("emailSesion") // Eliminar el email guardado
    editor.apply()

    Toast.makeText(context, "Sesión cerrada correctamente.", Toast.LENGTH_LONG).show()

    //Redirigir a LoginActivity
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

fun obtenerRecordatoriosDesdeFirebase(context: Context, emailUsuario: String?, viewModel: FirebaseViewModel, onResult: (List<Recordatorio>) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm")

    if (emailUsuario != null) {
        viewModel.obtenerRecordatoriosPorEmail(emailUsuario) { listaRecordatorios, errorMessage ->
            if (errorMessage == null) {
                val recordatoriosOrdenados = listaRecordatorios.sortedBy { recordatorio ->
                    LocalDateTime.parse("${recordatorio.fecha} ${recordatorio.hora}", formatter)
                }
                onResult(recordatoriosOrdenados)
            } else {
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }
    }
}


//Obtener el email de SharedPreferences
private fun obtenerEmailDeSharedPreferences(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    return sharedPreferences.getString("emailSesion", null)
}

