package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

class ResetpasActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            S5sum2Theme {
                ResetScreen(auth)
            }
        }
    }
}

@Composable
fun ResetScreen(auth: FirebaseAuth?) {
    //variables locales  a usar
    val context = LocalContext.current
    var emailState by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isEmailSent by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // canvas y logo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color(0xFF89BDE0))
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
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = -20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Restablece tu Contraseña",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(100.dp))

                // Campo para ingresar el correo
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Ingresa tu Correo") },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.email), contentDescription = null)
                    },
                    textStyle = TextStyle(color = Color(0xFF161414)),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.8f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // validacion de correo
                Button(
                    onClick = {
                        if (auth != null) {
                            val trimmedEmail = emailState.trim()
                            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                            if (trimmedEmail.isEmpty() || !trimmedEmail.matches(emailPattern.toRegex())) {
                                dialogMessage = "Por favor, ingresa un correo válido."
                                dialogTitle = "Error"
                                showDialog = true
                                return@Button
                            }

                            isLoading = true
                            auth.sendPasswordResetEmail(trimmedEmail)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        isEmailSent = true
                                        dialogMessage = "Correo de restablecimiento enviado. Revisa tu bandeja."
                                        dialogTitle = "Éxito"
                                    } else {
                                        isEmailSent = false
                                        dialogMessage = "Error al enviar el correo: ${task.exception?.message}"
                                        dialogTitle = "Error"
                                    }
                                    showDialog = true
                                }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDAE089))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Enviar", color = Color.White)
                    }
                }

                // Mostrar el diálogo de confirmación o error
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(dialogTitle) },
                        text = { Text(dialogMessage) },
                        confirmButton = {}
                    )

                    // Si el correo fue enviado, iniciar temporizador de 3 segundos
                    if (isEmailSent) {
                        LaunchedEffect(Unit) {
                            delay(3000)
                            showDialog = false
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

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

                // Mostrar el diálogo de confirmación o error
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Resultado") },
                        text = { Text(dialogMessage) },
                        confirmButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ResetScreenPreview() {
    S5sum2Theme {
        ResetScreen(auth = null)
    }
}
