package com.example.s5sum2

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MenuActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MenuActivity>()

    @Before
    fun setup() {
        // Inicializar Espresso Intents
        Intents.init()
    }

    @After
    fun tearDown() {
        // Liberar Espresso Intents
        Intents.release()
    }

    @Test
    fun testConversarButtonLaunchesConversationActivity() {
        // Buscar el botón "Conversar" y hacer clic
        composeTestRule.onNodeWithText("Conversar").performClick()

        // Verificar que se lanzó el Intent correcto hacia ConversationActivity
        Intents.intended(IntentMatchers.hasComponent(ConversationActivity::class.java.name))
    }

    @Test
    fun testGeolocalizacionButtonLaunchesMapsActivity() {
        // Buscar el botón "Geolocalización" y hacer clic
        composeTestRule.onNodeWithText("Geolocalización").performClick()

        // Verificar que se lanzó el Intent correcto hacia MapsActivity
        Intents.intended(IntentMatchers.hasComponent(MapsActivity::class.java.name))
    }
}
