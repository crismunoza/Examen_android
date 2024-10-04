package com.example.s5sum2

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.s5sum2.ui.theme.S5sum2Theme
import com.google.firebase.storage.FirebaseStorage

class TutorialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            S5sum2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(modifier: Modifier = Modifier) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://s5sum2.appspot.com/tutorial/Ecovisual.mp4")

    storageRef.downloadUrl.addOnSuccessListener { uri ->
        videoUri = uri
    }

    videoUri?.let { uri ->
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI(uri)
                    setOnPreparedListener { it.start() }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    S5sum2Theme {
        VideoPlayer()
    }
}
