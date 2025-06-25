package com.univalle.dogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.univalle.dogapp.model.UserRequest
import com.univalle.dogapp.model.UserResponse
import com.univalle.dogapp.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.Assert.assertTrue
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.any


class LoginViewModelTest {

 @get:Rule
 val rule = InstantTaskExecutorRule()

 private val testDispatcher = UnconfinedTestDispatcher()

 private lateinit var loginViewModel: LoginViewModel

 @Mock
 lateinit var loginRepository: LoginRepository

 @Before
 fun setUp() {
  Dispatchers.setMain(testDispatcher)
  MockitoAnnotations.openMocks(this)
  loginViewModel = LoginViewModel(loginRepository)
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `registerUser metodo isRegister correctamente`() = runTest {
       // given
       val userRequest = UserRequest("ana@example.com", "123456")
       val expectedResponse = UserResponse(
        email = "ana@example.com",
        isRegister = true,
        message = "Registro exitoso"
       )
        // when
       // Simular callback del repositorio con el resultado esperado
       doAnswer { invocation ->
        val callback = invocation.getArgument<(UserResponse) -> Unit>(1)
        callback(expectedResponse)
        null
       }.`when`(loginRepository).registerUser(eq(userRequest), any())

       loginViewModel.registerUser(userRequest)
       advanceUntilIdle() // Esperar corrutina

       // Then
       assertEquals(expectedResponse, loginViewModel.isRegister.value)
 }

 @Test
 fun `sesion activa vista cuando email no es null`() {
      // given
      var isEnabled = false
      // when
      loginViewModel.sesion("ana@example.com") {
       isEnabled = it
      }
      // Then
      assertTrue(isEnabled)
 }

 @Test
 fun `sesion desactiva vista cuando email es null`() {
       // given
       var isEnabled = true
       // when
       loginViewModel.sesion(null) {
        isEnabled = it
       }
       // Then
       assertFalse(isEnabled)
      }

}
