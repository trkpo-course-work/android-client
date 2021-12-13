package ru.spbstu.auth.login.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.auth.R
import ru.spbstu.auth.databinding.*
import ru.spbstu.auth.di.AuthApi
import ru.spbstu.auth.di.AuthComponent
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.extensions.setDebounceClickListener
import javax.inject.Inject

class AuthFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var _loginBinding: LayoutAuthLoginBinding? = null
    private val loginBinding get() = _loginBinding!!

    private var _signinBinding: LayoutAuthSigninBinding? = null
    private val signinBinding get() = _signinBinding!!

    private var _confBinding: LayoutAuthConfirmationBinding? = null
    private val confBinding get() = _confBinding!!

    private var _resetPasswordBinding: LayoutAuthResetPasswordEmailBinding? = null
    private val resetPasswordBinding get() = _resetPasswordBinding!!

    private var _newPasswordBinding: LayoutAuthResetPasswordNewPasswordBinding? = null
    private val newPasswordBinding get() = _newPasswordBinding!!

    private var navigationIconDrawable: Drawable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _loginBinding = LayoutAuthLoginBinding.bind(binding.root)
        _signinBinding = LayoutAuthSigninBinding.bind(binding.root)
        _confBinding = LayoutAuthConfirmationBinding.bind(binding.root)
        _resetPasswordBinding = LayoutAuthResetPasswordEmailBinding.bind(binding.root)
        _newPasswordBinding = LayoutAuthResetPasswordNewPasswordBinding.bind(binding.root)
        navigationIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back_24)
        binding.frgLoginToolbar.setNavigationOnClickListener {
            if (!viewModel.onNavigationButtonClicked()) {
                requireActivity().finish()
            }
        }
        binding.frgLoginMbActionButton.setDebounceClickListener {
            when (viewModel.authState.value) {
                AuthViewModel.AuthState.LOGIN -> {
                    val login = loginBinding.layoutLoginEtLogin.text?.toString()?.trim()
                    val password = loginBinding.layoutLoginEtPassword.text?.toString()?.trim()
                    if (login?.isNotEmpty() == true && password?.isNotEmpty() == true) {
                        viewModel.login(login, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Неверно заполнены поля",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                AuthViewModel.AuthState.SIGNIN -> {
                    val name = signinBinding.layoutSigninEtName.text?.toString()?.trim()
                    val login = signinBinding.layoutSigninEtLogin.text?.toString()?.trim()
                    val email = signinBinding.layoutSigninEtEmail.text?.toString()?.trim()
                    val pass = signinBinding.layoutSigninEtPassword.text?.toString()?.trim()
                    val confPass = signinBinding.layoutSigninEtPasswordConf.text?.toString()?.trim()
                    if (name == null || name.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Имя не может быть пустым",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setDebounceClickListener
                    }
                    if (login == null || login.isEmpty() || login.length < 3) {
                        Toast.makeText(
                            requireContext(),
                            "Логин не может быть пустым и меньше 3х символов",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setDebounceClickListener
                    }
                    if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(requireContext(), "Неверно введён email", Toast.LENGTH_SHORT)
                            .show()
                        return@setDebounceClickListener
                    }
                    if (pass == null || pass.length < AuthViewModel.PASSWORD_MIN_LENGTH || pass != confPass) {
                        Toast.makeText(
                            requireContext(),
                            "Пароли не совпадают или длина пароля меньше ${AuthViewModel.PASSWORD_MIN_LENGTH}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setDebounceClickListener
                    }
                    viewModel.signIn(name, login, email, pass)
                }
                AuthViewModel.AuthState.CONFIRMATION -> {
                    val code = confBinding.layoutConfEtCode.text?.toString()
                    if (code?.isNotEmpty() == true && code.length == AuthViewModel.CODE_LENGTH) {
                        viewModel.confirm(code)
                    }
                }
                AuthViewModel.AuthState.RESET_PASSWORD_EMAIL -> {
                    val email =
                        resetPasswordBinding.layoutResetPasswordEmailEtEmail.text?.toString()
                            ?.trim()
                    if (email?.isNotEmpty() == true && Patterns.EMAIL_ADDRESS.matcher(email)
                            .matches()
                    ) {
                        viewModel.requestReset(email)
                    } else {
                        Toast.makeText(requireContext(), "Неверно введён адрес", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                AuthViewModel.AuthState.RESET_PASSWORD_CODE -> {
                    val code =
                        resetPasswordBinding.layoutResetPasswordEmailEtCode.text?.toString()?.trim()
                    if (code?.isNotEmpty() == true && code.length == AuthViewModel.CODE_LENGTH) {
                        viewModel.checkResetCode(code)
                    }
                }
                AuthViewModel.AuthState.RESET_PASSWORD_NEW_PASSWORD -> {
                    val newPass =
                        newPasswordBinding.layoutNewPasswordEmailEtPassword.text?.toString()?.trim()
                    val confPass =
                        newPasswordBinding.layoutNewPasswordEmailEtConf.text?.toString()?.trim()
                    if (newPass != null && confPass != null && newPass.length >= AuthViewModel.PASSWORD_MIN_LENGTH && newPass == confPass) {
                        viewModel.setNewPassword(newPass)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Пароли не совпадают или длина пароля меньше ${AuthViewModel.PASSWORD_MIN_LENGTH}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!viewModel.onNavigationButtonClicked()) {
                        requireActivity().finish()
                    }
                }
            })
        setSpanForNoAccountQuestion()
        loginBinding.layoutLoginTvNewCode.setDebounceClickListener {
            viewModel.onForgotPasswordClick()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.authState.collect {
                handleAuthState(it)
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.buttonState.collect {
                binding.frgLoginMbActionButton.isEnabled = it
            }
        }

        lifecycleScope.launch {
            viewModel.resetTimer.collect {
                if (it > 0) {
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.text_secondary)
                    )
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.isEnabled = false
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.text =
                        getString(R.string.send_code_again, it)
                } else {
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.primary_light_color)
                    )
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.isEnabled = true
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.text =
                        getString(R.string.send_code_again_no_args)
                    resetPasswordBinding.layoutResetPasswordEmailTvNewCode.setDebounceClickListener {
                        viewModel.sendResetCodeAgain()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.confTimer.collect {
                if (it > 0) {
                    confBinding.layoutConfTvNewCode.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.text_secondary)
                    )
                    confBinding.layoutConfTvNewCode.isEnabled = false
                    confBinding.layoutConfTvNewCode.text =
                        getString(R.string.send_code_again, it)
                } else {
                    confBinding.layoutConfTvNewCode.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.primary_light_color)
                    )
                    confBinding.layoutConfTvNewCode.isEnabled = true
                    confBinding.layoutConfTvNewCode.text =
                        getString(R.string.send_code_again_no_args)
                    confBinding.layoutConfTvNewCode.setDebounceClickListener {
                        viewModel.sendConfCode()
                    }
                }
            }
        }
    }

    private fun handleAuthState(state: AuthViewModel.AuthState) {
        when (state) {
            is AuthViewModel.AuthState.LOGIN -> {
                loginBinding.layoutLoginRlRoot.visibility = View.VISIBLE
                signinBinding.layoutSigninRlRoot.visibility = View.GONE
                confBinding.layoutConfRlRoot.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.GONE
                binding.frgLoginToolbarTitle.text = getString(R.string.authorization)
                binding.frgLoginToolbar.navigationIcon = null
                binding.frgLoginMbActionButton.text = getString(R.string.login_button_text)
                binding.frgLoginTvNoAccount.visibility = View.VISIBLE
            }
            AuthViewModel.AuthState.SIGNIN -> {
                loginBinding.layoutLoginRlRoot.visibility = View.GONE
                signinBinding.layoutSigninRlRoot.visibility = View.VISIBLE
                confBinding.layoutConfRlRoot.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.GONE
                binding.frgLoginToolbarTitle.text = getString(R.string.registration)
                binding.frgLoginToolbar.navigationIcon = navigationIconDrawable
                binding.frgLoginMbActionButton.text = getString(R.string.signin_button_text)
                binding.frgLoginTvNoAccount.visibility = View.GONE
            }
            AuthViewModel.AuthState.CONFIRMATION -> {
                loginBinding.layoutLoginRlRoot.visibility = View.GONE
                signinBinding.layoutSigninRlRoot.visibility = View.GONE
                confBinding.layoutConfRlRoot.visibility = View.VISIBLE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.GONE
                binding.frgLoginToolbarTitle.text = getString(R.string.confirmation)
                binding.frgLoginToolbar.navigationIcon = navigationIconDrawable
                binding.frgLoginMbActionButton.text = getString(R.string.confirmation_button_text)
                binding.frgLoginTvNoAccount.visibility = View.GONE
                confBinding.layoutConfTvNewCode.text =
                    getString(R.string.send_code_again, AuthViewModel.TIMER_START)
            }
            AuthViewModel.AuthState.RESET_PASSWORD_EMAIL -> {
                loginBinding.layoutLoginRlRoot.visibility = View.GONE
                signinBinding.layoutSigninRlRoot.visibility = View.GONE
                confBinding.layoutConfRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.VISIBLE
                resetPasswordBinding.layoutResetPasswordEmailMcCode.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailTvNewCode.visibility = View.GONE
                binding.frgLoginMbActionButton.text = getString(R.string.get_code)
                binding.frgLoginToolbar.navigationIcon = navigationIconDrawable
                binding.frgLoginToolbarTitle.text = getString(R.string.password_recovery)
                binding.frgLoginTvNoAccount.visibility = View.GONE
            }
            AuthViewModel.AuthState.RESET_PASSWORD_CODE -> {
                loginBinding.layoutLoginRlRoot.visibility = View.GONE
                signinBinding.layoutSigninRlRoot.visibility = View.GONE
                confBinding.layoutConfRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.VISIBLE
                resetPasswordBinding.layoutResetPasswordEmailMcCode.visibility = View.VISIBLE
                resetPasswordBinding.layoutResetPasswordEmailTvNewCode.visibility = View.VISIBLE
                binding.frgLoginMbActionButton.text = getString(R.string.change_password)
                binding.frgLoginToolbar.navigationIcon = navigationIconDrawable
                binding.frgLoginToolbarTitle.text = getString(R.string.password_recovery)
                binding.frgLoginTvNoAccount.visibility = View.GONE
                resetPasswordBinding.layoutResetPasswordEmailTvNewCode.text =
                    getString(R.string.send_code_again, AuthViewModel.TIMER_START)
            }
            AuthViewModel.AuthState.RESET_PASSWORD_NEW_PASSWORD -> {
                loginBinding.layoutLoginRlRoot.visibility = View.GONE
                signinBinding.layoutSigninRlRoot.visibility = View.GONE
                confBinding.layoutConfRlRoot.visibility = View.GONE
                newPasswordBinding.layoutNewPasswordRlRoot.visibility = View.VISIBLE
                resetPasswordBinding.layoutResetPasswordEmailRlRoot.visibility = View.GONE
                binding.frgLoginMbActionButton.text = getString(R.string.change_password)
                binding.frgLoginToolbar.navigationIcon = navigationIconDrawable
                binding.frgLoginToolbarTitle.text = getString(R.string.password_recovery)
                binding.frgLoginTvNoAccount.visibility = View.GONE
            }
        }
    }

    private fun setSpanForNoAccountQuestion() {
        val spannable = SpannableString(getString(R.string.no_account_question))
        val span = object : ClickableSpan() {
            override fun onClick(widget: View) {
                viewModel.onSignInClick()
            }
        }
        val start = spannable.lastIndexOf(" ")
        val end = spannable.length
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.frgLoginTvNoAccount.text = spannable
        binding.frgLoginTvNoAccount.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun inject() {
        FeatureUtils.getFeature<AuthComponent>(this, AuthApi::class.java)
            .loginComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AuthFragment()
    }
}