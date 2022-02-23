package ru.spbstu.profile.profile.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.profile.R
import ru.spbstu.profile.databinding.FragmentProfileBinding
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileComponent
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.frgProfileToolbarTitle.text = "Профиль"
        binding.frgProfileIbActions.setOnClickListener {
            it.showContextMenu()
        }
        registerForContextMenu(binding.frgProfileIbActions)
        binding.frgProfileTvFavorites.setDebounceClickListener {
            viewModel.openFavorites()
        }
        viewModel.loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                binding.frgProfileTvName.text = it.profile.name
                binding.frgProfileTvLogin.text = it.profile.login
                val pictureId = it.profile.pictureId
                if (pictureId != null) {
                    val pictureUrl = pictureUrlHelper.getPictureUrl(pictureId)
                    Glide.with(binding.root)
                        .load(pictureUrl)
                        .centerCrop()
                        .into(binding.frgProfileIvAvatar)
                    binding.frgProfileIvAvatarStub.visibility = View.GONE
                } else {
                    Glide.with(binding.root)
                        .load(ContextCompat.getDrawable(requireContext(), R.color.white))
                        .centerCrop()
                        .into(binding.frgProfileIvAvatar)
                    binding.frgProfileIvAvatarStub.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                viewModel.editProfile()
                true
            }
            R.id.exit -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Выход")
                    .setMessage("Вы действительно хотите выйти?")
                    .setPositiveButton("Да") { _, _ ->
                        viewModel.logout()
                    }
                    .setNegativeButton("Нет") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            R.id.delete -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Удаление профиля")
                    .setMessage("Вы действительно хотите удалить ваш профиль?")
                    .setPositiveButton("Да") { _, _ ->
                        viewModel.delete()
                    }
                    .setNegativeButton("Нет") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            else -> false
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<ProfileComponent>(this, ProfileApi::class.java)
            .profileProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}