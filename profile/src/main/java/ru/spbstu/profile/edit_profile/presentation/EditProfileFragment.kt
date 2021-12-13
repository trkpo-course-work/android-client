package ru.spbstu.profile.edit_profile.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.profile.databinding.FragmentEditProfileBinding
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileComponent
import javax.inject.Inject
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.picker.PickPhoto
import ru.spbstu.common.utils.PictureUrlHelper

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: EditProfileViewModel

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper

    private val getContent =
        registerForActivityResult(PickPhoto()) { uri: Uri? ->
            if (uri != null) {
                binding.frgEditProfileIvAvatarStub.visibility = View.GONE
                viewModel.photoUri = uri
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.frgEditProfileToolbar.setNavigationOnClickListener {
            viewModel.onNavigationIconClick()
        }
        binding.frgEditProfileIvAvatar.setDebounceClickListener {
            getContent.launch(null)
        }
        binding.frgEditProfileIbActions.setDebounceClickListener {
            var name = binding.frgEditProfileEtName.text?.toString()?.trim()
            var nameChange = true
            if (name == null || name.isEmpty()) {
                name = viewModel.state.value?.profile?.name ?: return@setDebounceClickListener
                nameChange = false
            }
            var login = binding.frgEditProfileEtLogin.text?.toString()?.trim()
            var loginChange = true
            if (login == null || login.isEmpty()) {
                login = viewModel.state.value?.profile?.login ?: return@setDebounceClickListener
                loginChange = false
            }
            val oldPass = binding.frgEditProfileEtOldPass.text?.toString()?.trim()
            val newPass = binding.frgEditProfileEtNewPass.text?.toString()?.trim()
            val confPass = binding.frgEditProfileEtConfNewPass.text?.toString()?.trim()
            var passChange = true
            if (newPass == null || newPass.isEmpty() || confPass == null || confPass.isEmpty() || newPass != confPass || oldPass == null || oldPass.isEmpty()
            ) {
                passChange = false
            }
            if (!nameChange && !loginChange && !passChange) {
                viewModel.editProfile(
                    viewModel.state.value?.profile?.name ?: return@setDebounceClickListener,
                    viewModel.state.value?.profile?.login ?: return@setDebounceClickListener,
                    null,
                    null,
                    false
                )
                return@setDebounceClickListener
            }
            val toChangeString =
                "Будут изменены: ${if (nameChange) "имя" else ""} ${if (loginChange) "логин" else ""} ${if (passChange) "пароль" else ""}"
            Toast.makeText(requireContext(), toChangeString, Toast.LENGTH_LONG).show()
            viewModel.editProfile(name, login, oldPass, newPass, passChange)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.uriPhotoState.filterNotNull().collect {
                Glide.with(binding.root)
                    .load(it)
                    .centerCrop()
                    .into(binding.frgEditProfileIvAvatar)
            }
        }

        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                /*binding.frgEditProfileEtLogin.setText(
                    it.profile.login,
                    TextView.BufferType.EDITABLE
                )
                binding.frgEditProfileEtName.setText(it.profile.name, TextView.BufferType.EDITABLE)*/
                val pictureId = it.profile.pictureId
                if (pictureId != null) {
                    Glide.with(binding.root)
                        .load(pictureUrlHelper.getPictureUrl(pictureId))
                        .centerCrop()
                        .into(binding.frgEditProfileIvAvatar)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<ProfileComponent>(this, ProfileApi::class.java)
            .editProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }
}