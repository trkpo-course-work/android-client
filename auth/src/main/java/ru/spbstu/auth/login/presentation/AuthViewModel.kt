package ru.spbstu.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spbstu.auth.AuthRouter

class AuthViewModel(private val authRouter: AuthRouter) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LOGIN)
    val authState = _authState.asStateFlow()


    fun onNavigationButtonClicked(): Boolean {
        return when (_authState.value) {
            AuthState.LOGIN -> false
            AuthState.SIGNIN -> {
                _authState.value = AuthState.LOGIN
                true
            }
            AuthState.CONFIRMATION -> {
                _authState.value = AuthState.SIGNIN
                true
            }
            AuthState.RESET_PASSWORD_EMAIL -> {
                _authState.value = AuthState.LOGIN
                true
            }
            AuthState.RESET_PASSWORD_CODE -> {
                _authState.value = AuthState.RESET_PASSWORD_EMAIL
                true
            }
            AuthState.RESET_PASSWORD_NEW_PASSWORD -> {
                _authState.value = AuthState.LOGIN
                true
            }
        }
    }

    fun onForgotPasswordClick() {
        _authState.value = AuthState.RESET_PASSWORD_EMAIL
    }

    fun onMainActionButtonClick() {
        when (_authState.value) {
            AuthState.LOGIN -> {
                authRouter.openMainPage()
            }
            AuthState.SIGNIN -> {
                _authState.value = AuthState.CONFIRMATION
            }
            AuthState.CONFIRMATION -> {

            }
            AuthState.RESET_PASSWORD_EMAIL -> {
                _authState.value = AuthState.RESET_PASSWORD_CODE
            }
            AuthState.RESET_PASSWORD_CODE -> {
                _authState.value = AuthState.RESET_PASSWORD_NEW_PASSWORD
            }
            AuthState.RESET_PASSWORD_NEW_PASSWORD -> {
                _authState.value = AuthState.LOGIN
            }
        }
    }

    fun onSignInClick() {
        _authState.value = AuthState.SIGNIN
    }

    sealed class AuthState {
        object LOGIN : AuthState()
        object SIGNIN : AuthState()
        object CONFIRMATION : AuthState()
        object RESET_PASSWORD_EMAIL: AuthState()
        object RESET_PASSWORD_CODE: AuthState()
        object RESET_PASSWORD_NEW_PASSWORD: AuthState()
    }
}