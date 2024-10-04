@file:Suppress("DEPRECATION")
package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.s5sum2.models.Contacto
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*

class ConversationActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Registrar el ActivityResultLauncher para el reconocimiento de voz
    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
            setContent {
                S5sum2Theme {
                    ConversationScreen(textToSpeech, spokenText, ::startVoiceRecognitionActivity, getUserId(), db)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.getDefault()
            }
        }

        setContent {
            S5sum2Theme {
                ConversationScreen(textToSpeech, "", ::startVoiceRecognitionActivity, getUserId(), db)
            }
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

    // Función para obtener el userId del usuario autenticado para la conversacion
    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    // Hacer pública la función para ser usada en Composables
    fun startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizerLauncher.launch(intent)
    }
}

@Composable
fun ConversationScreen(
    textToSpeech: TextToSpeech,
    spokenText: String = "",
    startVoiceRecognition: () -> Unit,
    userId: String?,
    db: FirebaseFirestore
) {
    var userMessage by remember { mutableStateOf("") }
    val conversation = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var userName by remember { mutableStateOf("Yo") }

    // Obtener el nombre del usuario desde Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val contact = documentSnapshot.toObject<Contacto>()
                    userName = contact?.nombre ?: "Yo"
                }
                .addOnFailureListener {
                    userName = "Yo"
                }
        }
    }

    // Añadir texto hablado a la conversación si no está vacío
    LaunchedEffect(spokenText) {
        if (spokenText.isNotBlank()) {
            conversation.add(Pair("Respuesta: $spokenText", false))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(conversation.size) { index ->
                val (message, isUser) = conversation[index]
                ChatBubble(message = message, isUser = isUser)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = userMessage,
            onValueChange = { userMessage = it },
            label = { Text("Escribe algo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                // Agrega el mensaje a la conversación
                if (userMessage.isNotBlank()) {
                    conversation.add(Pair("$userName: $userMessage", true))
                    textToSpeech.speak(userMessage, TextToSpeech.QUEUE_FLUSH, null, null)
                    userMessage = ""
                }
            }) {
                Text("Hablar")
            }
            Button(onClick = {
                startVoiceRecognition()
            }) {
                Text("Percibir")
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    //variable para los colores de las burbujas  y tamaño
    val backgroundColor = if (isUser) Color(0xFFD1FFB0) else Color(0xFFB0D7FF)
    val alignment = if (isUser) Alignment.CenterStart else Alignment.CenterEnd
    val paddingStart = if (isUser) 16.dp else 64.dp
    val paddingEnd = if (isUser) 64.dp else 16.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd, top = 4.dp, bottom = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = backgroundColor,
            modifier = Modifier
                .padding(4.dp)
                .wrapContentWidth()
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
