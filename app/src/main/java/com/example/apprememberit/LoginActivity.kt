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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            Login()
        }
    }
}

@Preview
@Composable
fun Login() {
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
            text = "Mi Cuenta",
            color = Color(android.graphics.Color.parseColor("#Ea6d35")),
            modifier = Modifier.padding(top = 16.dp, start = 24.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold
        )

        var text by rememberSaveable { mutableStateOf(value = "") }

        TextField(
            value = text, onValueChange = { text = it },
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

        var text2 by rememberSaveable { mutableStateOf(value = "") }
        TextField(
            value = text2, onValueChange = { text2 = it },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.password), contentDescription = null,
                    modifier = Modifier
                        .size(63.dp)
                        .padding(start = 6.dp)
                        .padding(6.dp)
                )
            },
            label = { Text(text = "Contraseña", fontSize = 18.sp) },
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
            onClick = { /* Acción al hacer clic en el botón */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#Ea6d35"))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                .height(56.dp) // Ajustar la altura del botón para que coincida con los TextField
        ) {
            Text(text = "Iniciar sesión", color = Color.White, fontSize = 22.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(android.graphics.Color.parseColor("#3b608c"))
            )
        }
    }
}
