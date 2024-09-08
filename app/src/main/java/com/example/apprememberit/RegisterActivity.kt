package com.example.apprememberit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Usuario(val nombre: String, val email: String, val contrasena: String)

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Register()
        }
    }
}

@Preview
@Composable
fun Register() {
    // Obtenemos el contexto dentro de una función composable
    val context = LocalContext.current

    // Variables para almacenar los valores del formulario
    var text_nombre by rememberSaveable { mutableStateOf("") }
    var text_correo by rememberSaveable { mutableStateOf("") }
    var text_contrasena by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color(android.graphics.Color.parseColor("#f8eeec")))
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_background1),
            contentDescription = null
        )

        Text(
            text = "Registrarte",
            color = Color(android.graphics.Color.parseColor("#3b608c")),
            modifier = Modifier.padding(top = 16.dp, start = 24.dp),
            fontSize = 48.sp,
            fontWeight = FontWeight.SemiBold
        )

        TextField(
            value = text_nombre, onValueChange = { text_nombre = it },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.name), contentDescription = null,
                    modifier = Modifier
                        .size(63.dp)
                        .padding(start = 6.dp)
                        .padding(3.dp)
                )
            },
            label = { Text(text = "Nombre", fontSize = 20.sp) },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
                unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .background(Color.White, CircleShape)
        )

        TextField(
            value = text_correo, onValueChange = { text_correo = it },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.email), contentDescription = null,
                    modifier = Modifier
                        .size(63.dp)
                        .padding(start = 6.dp)
                        .padding(3.dp)
                )
            },
            label = { Text(text = "Correo electrónico", fontSize = 20.sp) },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
                unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .background(Color.White, CircleShape)
        )

        TextField(
            value = text_contrasena, onValueChange = { text_contrasena = it },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password), contentDescription = null,
                    modifier = Modifier
                        .size(63.dp)
                        .padding(start = 6.dp)
                        .padding(6.dp)
                )
            },
            label = { Text(text = "Contraseña", fontSize = 20.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
                unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .background(Color.White, CircleShape)
        )

        Button(
            onClick = { guardarUsuarioEnSharedPreferences(context, text_nombre, text_correo, text_contrasena) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#3b608c"))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .height(56.dp)
        ) {
            Text(text = "Crear cuenta", color = Color.White, fontSize = 28.sp)
        }

        //Redirigir a Login
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                .clickable {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
        ) {
            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        }

    }
}
private fun guardarUsuarioEnSharedPreferences(context: Context, nombre: String, correo: String, contrasena: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    val gson = Gson()

    //Obtener lista de usuarios almacenados en memoria
    val jsonListaUsuarios = sharedPreferences.getString("listaUsuarios", null)
    val listaUsuariosTipo = object : TypeToken<MutableList<Usuario>>() {}.type

    val listaUsuarios: MutableList<Usuario> = if (jsonListaUsuarios != null) {
        gson.fromJson(jsonListaUsuarios, listaUsuariosTipo)
    } else {
        mutableListOf()
    }

    val existeEmail = listaUsuarios.any { it.email == correo }

    if (existeEmail) {
        Toast.makeText(context, "Ya existe un usuario con este correo electrónico.", Toast.LENGTH_LONG).show()
    } else {
        val nuevoUsuario = Usuario(nombre, correo, contrasena)
        listaUsuarios.add(nuevoUsuario)
        //Actualiza lista de usuarios en memoria
        val editor = sharedPreferences.edit()
        val jsonActualizado = gson.toJson(listaUsuarios)
        editor.putString("listaUsuarios", jsonActualizado)

        //Crea sesión
        val usuarioSesion = UsuarioSesion(
            nombre = nuevoUsuario.nombre,
            email = nuevoUsuario.email,
            contrasena = nuevoUsuario.contrasena,
            isActive = true
        )
        //Guardar
        val usuarioSesionJson = gson.toJson(usuarioSesion)
        editor.putString("usuarioSesion", usuarioSesionJson)

        editor.apply()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Éxito")
        builder.setMessage("Cuenta creada correctamente.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()

            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        builder.setCancelable(false)
        builder.show()
    }
}


