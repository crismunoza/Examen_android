package com.example.s5sum2

import android.app.Instrumentation
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testMainScreenButtonsDisplayed() {
        composeTestRule.onNodeWithText("Iniciar Sesión").assertExists()

        composeTestRule.onNodeWithText("Crear Cuenta").assertExists()
    }

    @Test
    fun testLoginButtonAction() {
        Intents.intending(IntentMatchers.hasComponent(LoginActivity::class.java.name)).respondWith(
            Instrumentation.ActivityResult(0, null)
        )

        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @Test
    fun testRegisterButtonAction() {
        Intents.intending(IntentMatchers.hasComponent(RegisterActivity::class.java.name)).respondWith(
            Instrumentation.ActivityResult(0, null)
        )
        composeTestRule.onNodeWithText("Crear Cuenta").performClick()

        Intents.intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
    }
}
