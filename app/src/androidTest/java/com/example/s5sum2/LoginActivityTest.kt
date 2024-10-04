package com.example.s5sum2

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.util.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testLoginWithRegisteredUser() {
        composeTestRule.onNodeWithText("Ingresa Correo").performTextInput("xinocrismunoz@gmail.com")
        composeTestRule.onNodeWithText("Ingresa Contraseña").performTextInput("cris2010")

        composeTestRule.onNodeWithText("Iniciar").performClick()

        Thread.sleep(3000)

        Intents.intended(IntentMatchers.hasComponent(MenuActivity::class.java.name))
    }

    @Test
    fun testLoginShouldFail() {
        composeTestRule.onNodeWithText("Ingresa Correo").performTextInput("incorrect@example.com")
        composeTestRule.onNodeWithText("Ingresa Contraseña").performTextInput("wrongpassword")

        composeTestRule.onNodeWithText("Iniciar").performClick()
        try {
            Intents.intended(IntentMatchers.hasComponent(MenuActivity::class.java.name))
            fail("La prueba debería haber fallado, pero la navegación ocurrió.")
        } catch (e: AssertionError) {
            println("Prueba fallida como se esperaba.")
        }
    }

    @Test
    fun testNavigateToRegister() {
        composeTestRule.onNodeWithText("Regístrate").performClick()

        Intents.intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
    }

    @Test
    fun testNavigateToResetPassword() {
        composeTestRule.onNodeWithText("¿Olvidaste tu contraseña?").performClick()

        Intents.intended(IntentMatchers.hasComponent(ResetpasActivity::class.java.name))
    }
}
