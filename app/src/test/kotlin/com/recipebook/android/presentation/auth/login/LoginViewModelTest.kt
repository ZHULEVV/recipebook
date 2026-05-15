package com.recipebook.android.presentation.auth.login

import app.cash.turbine.test
import com.recipebook.android.domain.model.User
import com.recipebook.android.domain.usecase.LoginUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.isSuccess)
    }

    @Test
    fun `onEmailChange updates email and clears error`() {
        viewModel.onEmailChange("test@mail.ru")
        assertEquals("test@mail.ru", viewModel.uiState.value.email)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `login with blank fields sets error`() {
        viewModel.login()
        assertEquals("Заполните все поля", viewModel.uiState.value.error)
    }

    @Test
    fun `login success sets isSuccess true`() = runTest {
        val user = User("1", "test@mail.ru", "Test", null)
        coEvery { loginUseCase("test@mail.ru", "pass123") } returns Result.success(user)

        viewModel.uiState.test {
            viewModel.onEmailChange("test@mail.ru")
            viewModel.onPasswordChange("pass123")
            viewModel.login()
            testDispatcher.scheduler.advanceUntilIdle()

            val states = cancelAndConsumeRemainingEvents()
            val finalState = states.filterIsInstance<app.cash.turbine.Event.Item<LoginUiState>>()
                .last().value
            assertTrue(finalState.isSuccess)
            assertFalse(finalState.isLoading)
        }
    }

    @Test
    fun `login failure sets error message`() = runTest {
        coEvery { loginUseCase("bad@mail.ru", "wrong") } returns Result.failure(Exception("Неверный пароль"))

        viewModel.onEmailChange("bad@mail.ru")
        viewModel.onPasswordChange("wrong")
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Неверный пароль", viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSuccess)
    }
}
