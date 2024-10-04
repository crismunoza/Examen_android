package com.example.s5sum2

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.s5sum2.ui.theme.S5sum2Theme
import java.util.*

class HablarActivity : ComponentActivity() {
    private val SPEECH_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            S5sum2Theme {
                HablarScreen(onStartListening = { startVoiceRecognitionActivity() })
            }
        }
    }

    private fun startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            results?.let {
                val spokenText = it[0]
                setContent {
                    S5sum2Theme {
                        HablarScreen(onStartListening = { startVoiceRecognitionActivity() }, spokenText = spokenText)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

@Composable
fun HablarScreen(onStartListening: () -> Unit, spokenText: String = "") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { onStartListening() }) {
            Text("Iniciar dictado")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = spokenText, fontSize = 20.sp)
    }
}
