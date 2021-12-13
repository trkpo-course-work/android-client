package ru.spbstu.profile.user_profile.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.databinding.EmptyPostsBinding
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.profile.R
import ru.spbstu.profile.databinding.FragmentUserProfileBinding
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileComponent
import javax.inject.Inject
import kotlin.math.abs

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private var _emptyBinding: EmptyPostsBinding? = null
    private val emptyBinding get() = _emptyBinding!!

    @Inject
    lateinit var viewModel: UserProfileViewModel

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper

    private lateinit var adapter: BlogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        adapter = BlogAdapter(pictureUrlHelper)
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        _emptyBinding = EmptyPostsBinding.bind(binding.root)
        viewModel.userId = requireArguments().getLong(ID_KEY)
        binding.frgUserProfileToolbarTitle.text = "Профиль"
        binding.frgUserProfileRvPosts.adapter = adapter
        binding.frgUserProfileRvPosts.layoutManager =
            object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return true
                }
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        emptyBinding.root.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                binding.frgUserProfileToolbarTitle.text = it.profile.name
                binding.frgUserProfileToolbarTitleExpanded.text = it.profile.name
                val pictureId = it.profile.pictureId
                if (pictureId != null) {
                    Glide.with(binding.root)
                        .load(pictureUrlHelper.getPictureUrl(pictureId))
                        .centerCrop()
                        .into(binding.frgUserProfileIvAvatar)
                    binding.frgUserProfileIvAvatarStub.visibility = View.GONE
                } else {
                    Glide.with(binding.root)
                        .load(ContextCompat.getDrawable(requireContext(), R.color.white))
                        .centerCrop()
                        .into(binding.frgUserProfileIvAvatar)
                    binding.frgUserProfileIvAvatarStub.visibility = View.VISIBLE
                }

            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch {
            viewModel.posts.filterNotNull().collect {
                adapter.bingData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.isFavorite.filterNotNull().collect {
                if (it) {
                    binding.frgUserProfileIbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_favorite_30
                        )!!.apply {
                            setTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.color_favorite
                                )
                            )
                        })
                } else {
                    binding.frgUserProfileIbFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_favorite_30
                        )!!.apply {
                            setTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.text_secondary
                                )
                            )
                        })
                }
                binding.frgUserProfileIbFavorite.setDebounceClickListener {
                    viewModel.changeFavoriteState()
                }
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<ProfileComponent>(this, ProfileApi::class.java)
            .userProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        const val ID_KEY = "ru.spbstu.ru.ID_KEY"

        @JvmStatic
        fun newInstance(id: Long) = UserProfileFragment().apply {
            arguments = bundleOf(ID_KEY to id)
        }
    }
}