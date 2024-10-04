package com.example.s5sum2

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import org.junit.Rule
import org.junit.Test

class RegisterActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<RegisterActivity>()

    @Test
    fun testRegisterScreenLaunch() {
        // Verificar que el campo de texto para ingresar el nombre está presente
        composeTestRule.onNodeWithText("Ingresa Nombre").assertExists()

        // Verificar que el campo de texto para ingresar la fecha de nacimiento está presente
        composeTestRule.onNodeWithText("Fecha Nacimiento").assertExists()

        // Verificar que el campo de texto para ingresar el correo está presente
        composeTestRule.onNodeWithText("Ingresa Correo").assertExists()

        // Verificar que el campo de texto para ingresar la contraseña está presente
        composeTestRule.onNodeWithText("Ingresa Contraseña").assertExists()

        // Verificar que el botón "Regístrate" está presente
        composeTestRule.onNode(hasText("Regístrate") and hasClickAction()).assertExists()
    }
}
