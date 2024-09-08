package com.example.apprememberit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction


class RecuperarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Recuperar()
        }
    }
}

@Preview
@Composable
fun Recuperar() {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }

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
            text = "Recuperar contraseña",
            color = Color(android.graphics.Color.parseColor("#Ea6d35")),
            modifier = Modifier.padding(top = 16.dp, start = 24.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Campo de Correo Electrónico
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null // Restablecer el mensaje de error si se cambia el valor
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.email), contentDescription = null,
                    modifier = Modifier
                        .size(63.dp)
                        .padding(start = 6.dp)
                        .padding(3.dp)
                )
            },
            label = { Text(text = "Correo electrónico", fontSize = 18.sp) },
            isError = emailError != null,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
                unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .background(Color.White, CircleShape)
        )

        // Mostrar el mensaje de error si es necesario
        emailError?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp)
            )
        }

        //Botón de Enviar
        Button(
            onClick = {
                if (email.isEmpty()) {
                    emailError = "El correo electrónico es obligatorio"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Formato de correo electrónico no válido"
                } else {
                    Toast.makeText(context, "Se enviará un correo de recuperación a $email", Toast.LENGTH_LONG).show()

                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#Ea6d35"))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .height(56.dp)
        ) {
            Text(text = "Enviar", color = Color.White, fontSize = 22.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "Iniciar sesión",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 150.dp)
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    },
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        }
    }
}