package com.example.apprememberit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apprememberit.ViewModel.Usuario
import com.example.apprememberit.ViewModel.UsuarioSesion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Firebase Authentication
        auth = FirebaseAuth.getInstance()

        setContent{
            Login()
        }
    }
}

@Preview
@Composable
fun Login() {
    val context = LocalContext.current

    var usuarioSesion by remember { mutableStateOf<UsuarioSesion?>(null) }

    LaunchedEffect(Unit) {
        val email = getEmailSesionLocal(context)
        if (!email.isNullOrEmpty()) {
            getUsuarioSesionFirebase(context, email) { session ->
                usuarioSesion = session

                if (usuarioSesion != null && usuarioSesion!!.isActive) {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#f8eeec")))
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_background1),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 300.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Mi Cuenta",
                color = Color(android.graphics.Color.parseColor("#Ea6d35")),
                modifier = Modifier
                    .align(Alignment.Start),
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                },
                label = { Text(text = "Correo electrónico", fontSize = 18.sp) },
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
                    .background(Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                },
                label = { Text(text = "Contraseña", fontSize = 18.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
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
                    .background(Color.White, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { iniciarSesion(context, email.trim(), password.trim()) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#Ea6d35"))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Iniciar sesión", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#3b608c"))),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continuar sin cuenta...", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "¿No tienes una cuenta? Regístrate",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        val intent = Intent(context, RegisterActivity::class.java)
                        context.startActivity(intent)
                    },
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        val intent = Intent(context, RecuperarActivity::class.java)
                        context.startActivity(intent)
                    },
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        }
    }
}

private fun iniciarSesion(context: Context, email: String, contrasena: String) {
    if (email.isBlank() || contrasena.isBlank()) {
        Toast.makeText(context, "El correo y la contraseña son obligatorios.", Toast.LENGTH_LONG).show()
        return
    }

    //Firebase Authentication para iniciar sesión
    auth.signInWithEmailAndPassword(email, contrasena)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                //Guardar el email de sesión localmente
                guardarEmailSesionLocal(context, email)

                //Redirigir a MainActivity
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                Toast.makeText(context, "Bienvenido, ${user?.email}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Correo o contraseña incorrectos.", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error al iniciar sesión: ${it.message}", Toast.LENGTH_LONG).show()
        }
}

private fun guardarEmailSesionLocal(context: Context, email: String) {
    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("emailSesion", email)
    editor.apply()
}

private fun getEmailSesionLocal(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    return sharedPreferences.getString("emailSesion", null)
}

private fun getUsuarioSesionFirebase(context: Context, email: String, onResult: (UsuarioSesion?) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("usuarioSesion").child(email.replace(".", ","))

    //Obtener la sesión del usuario desde Firebase Realtime Database
    ref.get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            val usuarioSesion = dataSnapshot.getValue(UsuarioSesion::class.java)
            onResult(usuarioSesion)
        } else {
            onResult(null)
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(context, "Error al obtener la sesión: ${exception.message}", Toast.LENGTH_LONG).show()
        onResult(null)
    }
}


/*
private fun iniciarSesion_SharedPreferences(context: Context, email: String, contrasena: String) {
    //Validar que los campos de correo y contraseña no estén vacíos
    if (email.isBlank() || contrasena.isBlank()) {
        Toast.makeText(context, "El correo y la contraseña son obligatorios.", Toast.LENGTH_LONG).show()
        return
    }

    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    val gson = Gson()

    val jsonListaUsuarios = sharedPreferences.getString("listaUsuarios", null)
    val listaUsuariosTipo = object : TypeToken<MutableList<Usuario>>() {}.type
    val listaUsuarios: MutableList<Usuario> = if (jsonListaUsuarios != null) {
        gson.fromJson(jsonListaUsuarios, listaUsuariosTipo)
    } else {
        mutableListOf()
    }

    //Buscar usuario con el correo y la contraseña en lista de usuarios
    val usuario = listaUsuarios.find { it.email == email && it.contrasena == contrasena }

    if (usuario != null) {
        //Crear sesión de usuario
        val usuarioSesion = UsuarioSesion(
            nombre = usuario.nombre,
            email = usuario.email,
            contrasena = usuario.contrasena,
            isActive = true
        )

        val editor = sharedPreferences.edit()
        val usuarioSesionJson = gson.toJson(usuarioSesion)
        editor.putString("usuarioSesion", usuarioSesionJson)
        editor.apply()

        Toast.makeText(context, "Bienvenido, ${usuario.nombre}", Toast.LENGTH_LONG).show()

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()

        // Eliminar cualquier sesión activa anterior
        val editor = sharedPreferences.edit()
        editor.remove("usuarioSesion")
        editor.apply()
    }
}
*/

/*
private fun getUsuarioSesion_SharedPreferences(context: Context): UsuarioSesion? {
    val sharedPreferences = context.getSharedPreferences("datosApp", Context.MODE_PRIVATE)
    val gson = Gson()

    val usuarioSesionJson = sharedPreferences.getString("usuarioSesion", null)

    return if (usuarioSesionJson == null) {
        null
    } else {
        gson.fromJson(usuarioSesionJson, UsuarioSesion::class.java)
    }
}
*/