package ru.spbstu.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spbstu.auth.AuthRouter
import ru.spbstu.auth.domain.LoginError
import ru.spbstu.auth.repository.AuthRepository
import ru.spbstu.common.domain.BlogInResult
import timber.log.Timber

class AuthViewModel(
    private val authRouter: AuthRouter,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.LOGIN)
    val authState = _authState.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _buttonState = MutableStateFlow(true)
    val buttonState = _buttonState.asStateFlow()

    private val _resetTimer = MutableStateFlow(TIMER_START)
    val resetTimer = _resetTimer.asStateFlow()

    private val _confTimer = MutableStateFlow(TIMER_START)
    val confTimer = _confTimer.asStateFlow()

    private val disposable = CompositeDisposable()

    private var login: String? = null
    private var password: String? = null
    private var email: String? = null
    private var resetCode: String? = null

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

    fun login(login: String, password: String) {
        this.login = login
        this.password = password
        _buttonState.value = false
        authRepository.login(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _buttonState.value = true
                        authRouter.openMainPage()
                    }
                    is BlogInResult.Error -> {
                        when (it.error) {
                            LoginError.NOT_CONFIRMED -> {
                                authRepository.requestConfirm(login)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ confirmRequest ->
                                        when (confirmRequest) {
                                            is BlogInResult.Success -> {
                                                _authState.value = AuthState.CONFIRMATION
                                                _buttonState.value = true
                                            }
                                            is BlogInResult.Error -> {
                                                _error.value = "Ошибка входа"
                                                _buttonState.value = true
                                            }
                                        }
                                    }, {
                                        Timber.d(TAG, "login: $it")
                                    })
                                    .addTo(disposable)
                            }
                            else -> {
                                _buttonState.value = true
                                _error.value = "Ошибка входа"
                            }
                        }
                    }
                }
            }, {
                Timber.d(TAG, "login: $it")
            })
    }

    fun confirm(code: String) {
        val login = this.login ?: return
        val password = this.password ?: return
        _buttonState.value = false
        authRepository.confirm(login, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        login(login, password)
                        _buttonState.value = true
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Ошибка подтверждения аккаунта"
                        _buttonState.value = true
                    }
                }
            }, {
                Timber.d(TAG, "confirm: $it")
            })
            .addTo(disposable)
    }

    fun requestReset(email: String) {
        this.email = email
        _buttonState.value = false
        startResetTimer()
        authRepository.requestReset(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _buttonState.value = true
                        _authState.value = AuthState.RESET_PASSWORD_CODE
                    }
                    is BlogInResult.Error -> {
                        _error.value =
                            "Ошибка при сбросе пароля. Проверьте правильность введённого адреса"
                        _buttonState.value = true
                    }
                }
            }, {
                Timber.d(TAG, "requestReset: $it")
            })
            .addTo(disposable)
    }

    fun checkResetCode(code: String) {
        val email = this.email ?: return
        this.resetCode = code
        _buttonState.value = false
        authRepository.checkReset(email, code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _authState.value = AuthState.RESET_PASSWORD_NEW_PASSWORD
                        _buttonState.value = true
                    }
                    is BlogInResult.Error -> {
                        _error.value =
                            "Неверный код"
                        _buttonState.value = true
                    }
                }
            }, {
                Timber.d(TAG, "checkResetCode: $it")
            })
            .addTo(disposable)
    }

    fun setNewPassword(newPass: String) {
        val email = this.email ?: return
        val resetCode = this.resetCode ?: return
        _buttonState.value = false
        authRepository.reset(email, resetCode, newPass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _authState.value = AuthState.LOGIN
                        _buttonState.value = true
                        _error.value = "Пароль успешно изменён"
                    }
                    is BlogInResult.Error -> {
                        _error.value =
                            "Ошибка при изменении пароля"
                        _buttonState.value = true
                    }
                }
            }, {
                Timber.d(TAG, "setNewPassword: $it")
            })
            .addTo(disposable)
    }

    fun signIn(name: String, login: String, email: String, password: String) {
        this.login = login
        this.email = email
        this.password = password
        _buttonState.value = false
        authRepository.signIn(name, login, email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _authState.value = AuthState.CONFIRMATION
                        sendConfCode()
                        _buttonState.value = true
                    }
                    is BlogInResult.Error -> {
                        _error.value =
                            "Пользователь с такой почтой уже существует"
                        _buttonState.value = true
                    }
                }
            }, {
                Timber.d(TAG, "signIn: $it")
            })
            .addTo(disposable)
    }

    fun sendResetCodeAgain() {
        val email = this.email ?: return
        requestReset(email)
    }

    fun sendConfCode() {
        val login = this.login ?: return
        startConfTimer()
        authRepository.requestConfirm(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> Unit
                    is BlogInResult.Error -> {
                        _error.value =
                            "Ошибка при повторном запросе кода"
                    }
                }
            }, {
                Timber.d(TAG, "sendConfCodeAgain: $it")
            })
            .addTo(disposable)
    }

    private fun startResetTimer() {
        _resetTimer.value = TIMER_START
        viewModelScope.launch(Dispatchers.IO) {
            repeat(TIMER_START) {
                _resetTimer.value = _resetTimer.value - 1
                delay(1000)
            }
        }
    }

    private fun startConfTimer() {
        _confTimer.value = TIMER_START
        viewModelScope.launch(Dispatchers.IO) {
            repeat(TIMER_START) {
                _confTimer.value = _confTimer.value - 1
                delay(1000)
            }
        }
    }

    fun onSignInClick() {
        _authState.value = AuthState.SIGNIN
    }

    override fun onCleared() {
        super.onCleared()
        _buttonState.value = true
        _error.value = null
        disposable.dispose()
    }

    sealed class AuthState {
        object LOGIN : AuthState()
        object SIGNIN : AuthState()
        object CONFIRMATION : AuthState()
        object RESET_PASSWORD_EMAIL : AuthState()
        object RESET_PASSWORD_CODE : AuthState()
        object RESET_PASSWORD_NEW_PASSWORD : AuthState()
    }

    companion object {
        private val TAG = AuthViewModel::class.simpleName
        val CODE_LENGTH = 5
        val TIMER_START = 60
        val PASSWORD_MIN_LENGTH = 6
    }
}