package com.example.s5sum2


import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResetpasActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ResetpasActivity>()

    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        Intents.init() // Inicializamos Espresso Intents para rastrear Intents
    }

    @After
    fun tearDown() {
        Intents.release() // Liberamos Intents al finalizar la prueba
    }


    @Test
    fun testResetPasswordScreenLaunch() {
        // Verificar que el campo de texto para ingresar el correo está presente
        composeTestRule.onNodeWithText("Ingresa tu Correo").assertExists()

        // Verificar que el botón "Enviar" está presente
        composeTestRule.onNodeWithText("Enviar").assertExists()
    }

}
