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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContent{
            Dashboard()
        }
    }
}

@Preview
@Composable
fun Dashboard() {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(android.graphics.Color.parseColor("#f8eeec"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout{
            val (topImg, profile) = createRefs()
            Box (Modifier
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
                    ),shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
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
                        .weight(0.7f), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Bienvenido", color = Color.White, fontSize = 18.sp
                    )
                    Text(
                        text = "Manuel Valdés",
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
                        .padding(top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .height(90.dp)
                        .width(90.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor("#ffe0c8")),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.video_call),
                        contentDescription = null,
                        Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    Text(
                        text = "Video Call",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color(android.graphics.Color.parseColor("#c77710"))
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, end = 8.dp, start = 8.dp)
                        .height(90.dp)
                        .width(90.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor("#ffe0c8")),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = null,
                        Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    Text(
                        text = "Notification",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color(android.graphics.Color.parseColor("#c77710"))
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp)
                        .height(90.dp)
                        .width(90.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor("#ffe0c8")),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.voice_call),
                        contentDescription = null,
                        Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                    Text(
                        text = "Voice Call",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color(android.graphics.Color.parseColor("#c77710"))
                    )
                }
            }

        }

        var text by rememberSaveable { mutableStateOf(value = "") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = "Searching for...") },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(43.dp)
                        .padding(end = 6.dp)
                )
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                textColor = Color(android.graphics.Color.parseColor("#5e5e5e")),
                unfocusedLabelColor = Color(android.graphics.Color.parseColor("#5e5e5e"))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                .shadow(3.dp, shape = RoundedCornerShape(50.dp))
                .background(Color.White, CircleShape)
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, end = 24.dp, start = 24.dp)
                .shadow(3.dp, shape = RoundedCornerShape(25.dp))
                .height(145.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(android.graphics.Color.parseColor("#4c6184")),
                            Color(android.graphics.Color.parseColor("#f9c177"))
                        )
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            val (img, text1, text2) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.constrainAs(img) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )

            Text(
                text = "To Get Unlimited",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .constrainAs(text1) {
                        top.linkTo(parent.top)
                        end.linkTo(img.start)
                        start.linkTo(parent.start)
                    }
            )

            Text(
                text = "Upgrade Your Account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(text2) {
                        top.linkTo(text1.bottom)
                        end.linkTo(text1.end)
                        start.linkTo(text1.start)
                    }
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_1),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(16.dp)
                )

                Text(
                    text = "Inbox",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(android.graphics.Color.parseColor("#2e3d6d"))
                )
            }

            Column(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_2),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(16.dp)
                )

                Text(
                    text = "Maps",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(android.graphics.Color.parseColor("#2e3d6d"))
                )
            }

            Column(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_3),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        .padding(16.dp)
                )

                Text(
                    text = "Chats",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(android.graphics.Color.parseColor("#2e3d6d"))
                )
            }

        }

    }

}
