package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            S5sum2Theme {
                LoginScreen(auth)
            }
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth?) {
    // variable para valores
    val context = LocalContext.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val textFieldFontSize = (0.04f * screenWidth.value).sp
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.TopCenter

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = (0.03f * screenWidth.value).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height((0.08f * screenHeight.value).dp))
                    Text(
                        text = "Inicia Sesión",
                        fontSize = (0.08f * screenWidth.value).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDAE089),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height((0.04f * screenHeight.value).dp))

                    Image(
                        painter = painterResource(id = R.drawable.login_ico),
                        contentDescription = "Login Icon",
                        modifier = Modifier.size((0.35f * screenWidth.value).dp)
                    )

                    Spacer(modifier = Modifier.height((0.05f * screenHeight.value).dp))

                    // Campo de correo
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { emailState = it },
                        label = { Text("Ingresa Correo") },
                        leadingIcon = {
                            Icon(painter = painterResource(id = R.drawable.email), contentDescription = "Email Icon")
                        },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = textFieldFontSize,
                            color = Color(0xFF161414)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height((0.03f * screenHeight.value).dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = passwordState,
                        onValueChange = { passwordState = it },
                        label = { Text("Ingresa Contraseña", fontSize = textFieldFontSize) },
                        leadingIcon = {
                            Icon(painter = painterResource(id = R.drawable.password), contentDescription = "Password Icon")
                        },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = textFieldFontSize,
                            color = Color(0xFF161414)
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height((0.05f * screenHeight.value).dp))

                    // Botón de inicio de sesión
                    Button(
                        onClick = {
                            if (auth != null) {
                                // Recortar espacios en blanco en el correo
                                val trimmedEmail = emailState.trim()

                                // Validación básica para correo valido
                                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                                if (trimmedEmail.isEmpty() || !trimmedEmail.matches(emailPattern.toRegex())) {
                                    errorMessage = "Por favor, ingresa un correo válido."
                                    showErrorDialog = true
                                    return@Button
                                }

                                if (passwordState.isEmpty()) {
                                    errorMessage = "La contraseña no puede estar vacía."
                                    showErrorDialog = true
                                    return@Button
                                }

                                // Iniciar sesión con Firebase
                                isLoading = true
                                auth.signInWithEmailAndPassword(trimmedEmail, passwordState)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            val intent = Intent(context, MenuActivity::class.java)
                                            context.startActivity(intent)
                                        } else {
                                            val exceptionMessage = task.exception?.message ?: "Error desconocido"
                                            errorMessage = translateFirebaseError(exceptionMessage)
                                            showErrorDialog = true
                                        }
                                    }

                            }
                        },
                        modifier = Modifier
                            .padding(top = (0.05f * screenHeight.value).dp)
                            .fillMaxWidth(0.5f)
                            .height((0.05f * screenHeight.value).dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDAE089))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = "Iniciar", color = Color.White, fontSize = (0.03f * screenWidth.value).sp)
                        }
                    }

                    Spacer(modifier = Modifier.height((0.04f * screenHeight.value).dp))

                    // Textos de navegación
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = (0.04f * screenWidth.value).dp)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = (0.03f * screenWidth.value).sp,
                            color = Color(0xFF89BDE0),
                            modifier = Modifier.clickable {
                                val intent = Intent(context, ResetpasActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                        Text(
                            text = "Regístrate",
                            fontSize = (0.03f * screenWidth.value).sp,
                            color = Color(0xFF89E0C4),
                            modifier = Modifier.clickable {
                                val intent = Intent(context, RegisterActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
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
                                .offset(x = (-30.dp)),
                            colorFilter = ColorFilter.tint(Color(0xFF89E0C4))
                        )

                        Image(
                            painter = painterResource(id = R.drawable.bottom_right),
                            contentDescription = "Bottom Right Image",
                            modifier = Modifier
                                .size(150.dp)
                                .offset(x = (30.dp)),
                            colorFilter = ColorFilter.tint(Color(0xFF89E0C4))
                        )
                    }
                }
            }
        }
    )
    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

fun translateFirebaseError(errorMessage: String): String {
    return when {
        errorMessage.contains("supplied auth credential", ignoreCase = true) -> {
            "Las credenciales de autenticación proporcionadas son incorrectas, están mal formadas o han expirado."
        }
        // Agrega más casos para otros mensajes de error de Firebase que desees traducir
        else -> {
            Log.d("LoginActivity", "No se encontró traducción para el mensaje: $errorMessage")
            errorMessage
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    S5sum2Theme {
        LoginScreen(auth = null)
    }
}
