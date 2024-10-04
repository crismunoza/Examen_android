@file:Suppress("DEPRECATION")

package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s5sum2.models.Contacto
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.ui.res.painterResource


class MenuActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S5sum2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuScreen(modifier = Modifier.padding(innerPadding), db, auth.currentUser?.uid)
                }
            }
        }
    }
}

@Composable
fun MenuScreen(modifier: Modifier = Modifier, db: FirebaseFirestore?, userId: String?) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("Usuario") }

    // Obtener el nombre del usuario desde Firestore
    LaunchedEffect(userId) {
        if (userId != null && db != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val contact = documentSnapshot.toObject<Contacto>()
                    userName = contact?.nombre ?: "Usuario"
                }
                .addOnFailureListener {
                    userName = "Usuario"
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
    ) {

        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomEnd)
                .background(Color(0xFFDAE089)) // Color naranja
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // canvas y logo
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()) {
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
                        .offset(y = -20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Menú Principal",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64B5F6), // Color del texto
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Hola, $userName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFDAE089),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Conversar
            Button(onClick = {
                val intent = Intent(context, ConversationActivity::class.java)
                context.startActivity(intent)
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)) {
                Text("Conversar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Geolocalización
            Button(onClick = {
                val intent = Intent(context, MapsActivity::class.java)
                context.startActivity(intent)
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)) {
                Text("Geolocalización")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    S5sum2Theme {
        MenuScreen(db = null, userId = "sampleId")
    }
}

