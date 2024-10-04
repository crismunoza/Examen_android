package com.example.s5sum2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s5sum2.ui.theme.S5sum2Theme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContent {
            S5sum2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF89BDE0)) // Color lila
        ) {
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
                val path = Path().apply {
                    moveTo(0f, 100f)
                    cubicTo(size.width / 2, 200f, size.width / 2, 0f, size.width, 100f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    color = Color(0xFF89BDE0)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo_app), // Actualiza con tu logo
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = -20.dp)
            )
        }

        // Texto de bienvenida
        Text(
            text = "¡ Bienvenido a EcoVisual !",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Texto explicativo
        Text(
            text = "Estamos aquí para ayudarte a convertir sonidos en alertas visuales y vibraciones, haciéndote la vida más accesible y conectada. Con EcoVisual, nunca te perderás de una notificación importante. ¡Explora nuestras funciones y experimenta una nueva forma de comunicación!",
            fontSize = 16.sp,
            color = Color(0xFF000000),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de acción
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF89E0C4))
        ) {
            Text(text = "Iniciar Sesión", color = Color.White)
        }

        Button(
            onClick = {
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDAE089))
        ) {
            Text(text = "Crear Cuenta", color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val intent = Intent(context, TutorialActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(0.3f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF89BDE0))
        ) {
            Text(text = "Tutorial", color = Color.White)
        }


        //parte inferior decoracion
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.bottom_left),
                contentDescription = "Bottom Left Image",
                modifier = Modifier
                    .size(150.dp)
                    .offset(x = (-10.dp)),
                colorFilter = ColorFilter.tint(Color(0xFF89E0C4))
            )

            Image(
                painter = painterResource(id = R.drawable.bottom_right),
                contentDescription = "Bottom Right Image",
                modifier = Modifier
                    .size(150.dp)
                    .offset(x = (10.dp)),
                colorFilter = ColorFilter.tint(Color(0xFF89E0C4))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    S5sum2Theme {
        MainScreen()
    }
}
