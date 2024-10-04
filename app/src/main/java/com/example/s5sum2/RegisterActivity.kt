package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s5sum2.models.Contacto
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        setContent {
            S5sum2Theme {
                RegisterScreen(auth, firestore)
            }
        }
    }
}

@Composable
fun RegisterScreen(auth: FirebaseAuth?, firestore: FirebaseFirestore?) {
    // variables a usar
    val context = LocalContext.current
    var nombre by rememberSaveable { mutableStateOf("") }
    var fechaNacimiento by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

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
                // canva y logo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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

                Text(
                    text = "Regístrate",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Campos de texto para el registro
                CustomTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = "Ingresa Nombre",
                    icon = R.drawable.name,
                    labelTextSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                DateInputField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = "Fecha Nacimiento",
                    icon = R.drawable.btn_5,
                    labelTextSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Ingresa Correo",
                    icon = R.drawable.email,
                    labelTextSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Ingresa Contraseña",
                    icon = R.drawable.password,
                    isPassword = true,
                    labelTextSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Repetir Contraseña",
                    icon = R.drawable.password,
                    isPassword = true,
                    labelTextSize = 18.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val trimmedEmail = email.trim()
                        if (auth != null && firestore != null) {
                            if (password == confirmPassword) {
                                auth.createUserWithEmailAndPassword(trimmedEmail, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val userId = auth.currentUser?.uid
                                            val contacto = Contacto(nombre, fechaNacimiento, trimmedEmail)
                                            // Guardar datos en Firestore
                                            firestore.collection("users").document(userId!!)
                                                .set(contacto)
                                                .addOnSuccessListener {
                                                    dialogTitle = "Cuenta Creada"
                                                    dialogMessage = "¡Tu cuenta ha sido creada con éxito!"
                                                    showDialog = true

                                                    val intent = Intent(context, LoginActivity::class.java)
                                                    context.startActivity(intent)
                                                }
                                                .addOnFailureListener {
                                                    dialogTitle = "Error"
                                                    dialogMessage = "Error al guardar los datos en Firestore."
                                                    showDialog = true
                                                }
                                        } else {
                                            dialogTitle = "Error"
                                            dialogMessage = getFirebaseErrorMessage(task.exception)
                                            showDialog = true
                                        }
                                    }
                            } else {
                                dialogTitle = "Error"
                                dialogMessage = "Las contraseñas no coinciden."
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF89E0C4))
                ) {
                    Text(text = "Regístrate", color = Color.White)
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                            if (dialogMessage == "¡Tu cuenta ha sido creada con éxito!") {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                    if (dialogMessage == "¡Tu cuenta ha sido creada con éxito!") {
                                        val intent = Intent(context, LoginActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        title = { Text(dialogTitle) },
                        text = { Text(dialogMessage) }
                    )
                }
            }
        }
    )
}
// Función para traducir errores de Firebase al español
fun getFirebaseErrorMessage(exception: Exception?): String {
    return when {
        exception?.message?.contains("Password should be at least 6 characters") == true -> {
            "La contraseña debe tener al menos 6 caracteres."
        }
        exception?.message?.contains("The email address is badly formatted") == true -> {
            "La dirección de correo no tiene el formato correcto."
        }
        exception?.message?.contains("email address is already in use") == true -> {
            "La dirección de correo ya está en uso."
        }
        else -> {
            "Error al registrar usuario: ${exception?.message}"
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    isPassword: Boolean = false,
    labelTextSize: TextUnit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = labelTextSize
            )
        },
        leadingIcon = {
            Icon(painter = painterResource(id = icon), contentDescription = null)
        },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = labelTextSize,
            color = Color(0xFF161414)
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.8f)
    )
}

@Composable
fun DateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    labelTextSize: TextUnit,
    icon: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChange(formatAsDate(newValue)) },
        label = {
            Text(
                text = label,
                fontSize = labelTextSize
            )
        },        leadingIcon = {
            Icon(painter = painterResource(id = icon), contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = TextStyle(color = Color(0xFF161414)),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.8f)
    )
}

fun formatAsDate(input: String): String {
    val digits = input.filter { it.isDigit() }
    return when {
        digits.length >= 8 -> "${digits.take(2)}/${digits.drop(2).take(2)}/${digits.drop(4)}"
        else -> digits
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    S5sum2Theme {
        RegisterScreen(auth = null, firestore = null)
    }
}
