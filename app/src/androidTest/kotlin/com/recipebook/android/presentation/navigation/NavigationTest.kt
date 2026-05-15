package com.recipebook.android.presentation.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.recipebook.android.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun loginScreen_isDisplayedOnStart() {
        composeRule.onNodeWithText("Вход").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigatesToRegister() {
        composeRule.onNodeWithText("Нет аккаунта? Зарегистрироваться").performClick()
        composeRule.onNodeWithText("Регистрация").assertIsDisplayed()
    }

    @Test
    fun loginScreen_navigatesToForgotPassword() {
        composeRule.onNodeWithText("Забыли пароль?").performClick()
        composeRule.onNodeWithText("Восстановление пароля").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emptyFieldsShowError() {
        composeRule.onNodeWithText("Войти").performClick()
        composeRule.onNodeWithText("Заполните все поля").assertIsDisplayed()
    }

    @Test
    fun registerScreen_emptyFieldsShowError() {
        composeRule.onNodeWithText("Нет аккаунта? Зарегистрироваться").performClick()
        composeRule.onNodeWithText("Зарегистрироваться").performClick()
        composeRule.onNodeWithText("Заполните все поля").assertIsDisplayed()
    }

    @Test
    fun registerScreen_mismatchedPasswordsShowError() {
        composeRule.onNodeWithText("Нет аккаунта? Зарегистрироваться").performClick()
        composeRule.onNodeWithText("Имя").performTextInput("Иван")
        composeRule.onNodeWithText("Email").performTextInput("test@mail.ru")
        composeRule.onAllNodesWithText("Пароль")[0].performTextInput("pass123")
        composeRule.onAllNodesWithText("Пароль")[1].performTextInput("pass456")
        composeRule.onNodeWithText("Зарегистрироваться").performClick()
        composeRule.onNodeWithText("Пароли не совпадают").assertIsDisplayed()
    }
}
