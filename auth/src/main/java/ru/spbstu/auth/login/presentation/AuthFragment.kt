package ru.spbstu.auth.login.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
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
            viewModel.onMainActionButtonClick()
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
                confBinding.layoutConfTvNewCode.text = getString(R.string.send_code_again, 50)
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
                resetPasswordBinding.layoutResetPasswordEmailTvNewCode.text = getString(R.string.send_code_again, 50)
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