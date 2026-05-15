package com.recipebook.android.presentation.auth.register

import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.usecase.RegisterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val registerUseCase: RegisterUseCase = mockk()
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(registerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register with empty fields sets error`() {
        viewModel.register()
        assertEquals("Заполните все поля", viewModel.uiState.value.error)
    }

    @Test
    fun `register with mismatched passwords sets error`() {
        viewModel.onDisplayNameChange("Иван")
        viewModel.onEmailChange("ivan@mail.ru")
        viewModel.onPasswordChange("pass123")
        viewModel.onConfirmPasswordChange("pass456")
        viewModel.register()
        assertEquals("Пароли не совпадают", viewModel.uiState.value.error)
    }

    @Test
    fun `register with short password sets error`() {
        viewModel.onDisplayNameChange("Иван")
        viewModel.onEmailChange("ivan@mail.ru")
        viewModel.onPasswordChange("123")
        viewModel.onConfirmPasswordChange("123")
        viewModel.register()
        assertEquals("Пароль должен содержать минимум 6 символов", viewModel.uiState.value.error)
    }

    @Test
    fun `successful register sets isSuccess true`() = runTest {
        val user = User("1", "ivan@mail.ru", "Иван", null)
        coEvery { registerUseCase("ivan@mail.ru", "pass123", "Иван") } returns Result.success(user)

        viewModel.onDisplayNameChange("Иван")
        viewModel.onEmailChange("ivan@mail.ru")
        viewModel.onPasswordChange("pass123")
        viewModel.onConfirmPasswordChange("pass123")
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSuccess)
        assertNull(viewModel.uiState.value.error)
    }
}
