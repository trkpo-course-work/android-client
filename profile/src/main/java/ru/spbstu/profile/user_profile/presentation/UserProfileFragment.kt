package ru.spbstu.profile.user_profile.presentation

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.profile.R
import ru.spbstu.profile.databinding.FragmentUserProfileBinding
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileComponent
import ru.spbstu.profile.profile.presentation.UserProfileViewModel
import javax.inject.Inject
import kotlin.math.abs

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: UserProfileViewModel

    private val adapter: BlogAdapter = BlogAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        binding.frgUserProfileToolbarTitle.text = "Профиль"
        binding.frgUserProfileRvPosts.adapter = adapter
        binding.frgUserProfileAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val expandedPart = abs((appBarLayout.totalScrollRange - abs(verticalOffset.toDouble())) / appBarLayout.totalScrollRange)
            if (expandedPart > 0.2) {
                binding.frgUserProfileIvAvatar.visibility = View.VISIBLE
                binding.frgUserProfileIvAvatarStub.visibility = View.VISIBLE
                binding.frgUserProfileToolbarTitleExpanded.visibility = View.VISIBLE
                binding.frgUserProfileToolbarTitle.visibility = View.GONE
            } else {
                binding.frgUserProfileIvAvatar.visibility = View.GONE
                binding.frgUserProfileIvAvatarStub.visibility = View.GONE
                binding.frgUserProfileToolbarTitleExpanded.visibility = View.GONE
                binding.frgUserProfileToolbarTitle.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                binding.frgUserProfileToolbarTitle.text = it.profile.name
                binding.frgUserProfileToolbarTitleExpanded.text = it.profile.name
                adapter.bingData(it.profile.posts)
                if (it.profile.isFavorite) {
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
                if (it.profile.avatarUrl != null) {
                    Glide.with(binding.root)
                        .load(it.profile.avatarUrl)
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
    }

    private fun inject() {
        FeatureUtils.getFeature<ProfileComponent>(this, ProfileApi::class.java)
            .userProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}