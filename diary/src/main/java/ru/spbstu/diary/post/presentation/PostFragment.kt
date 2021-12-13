package ru.spbstu.diary.post.presentation

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.spbstu.common.api.model.toSpanType
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.picker.PickPhoto
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.common.utils.getSpannableText
import ru.spbstu.diary.R
import ru.spbstu.diary.databinding.FragmentPostBinding
import ru.spbstu.diary.di.DiaryApi
import ru.spbstu.diary.di.DiaryComponent
import javax.inject.Inject

class PostFragment : Fragment(), ActionMode.Callback {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: PostViewModel

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper

    lateinit var mode: Mode

    private lateinit var removePhotoDrawable: Drawable

    private val getContent =
        registerForActivityResult(PickPhoto()) { uri: Uri? ->
            viewModel.photoUri = uri
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        removePhotoDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_24)!!.apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.gray))
            }
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        mode = requireArguments().getParcelable(MODE_KEY)!!
        viewModel.mode = mode
        binding.frgPostToolbar.setNavigationOnClickListener {
            viewModel.back()
        }
        if (mode.isBlog) {
            if (mode.isEdit) {
                binding.frgPostToolbarTitle.text = "Редактирование поста"
            } else {
                binding.frgPostToolbarTitle.text = "Новый блог"
            }
        } else {
            if (mode.isEdit) {
                binding.frgPostToolbarTitle.text = "Редактирование записи"
            } else {
                binding.frgPostToolbarTitle.text = "Новая запись"
            }
        }
        binding.frgPostMbAddPhoto.setDebounceClickListener {
            getContent.launch(null)
        }
        binding.frgPostIbRemovePhoto.setDebounceClickListener {
            viewModel.photoUri = null
            viewModel.userDeletedPhoto = true
        }
        binding.frgPostIbRemovePhoto.setImageDrawable(removePhotoDrawable)
        binding.frgPostEtPost.customSelectionActionModeCallback = this
        binding.frgPostFinishPost.setDebounceClickListener {
            val postText = binding.frgPostEtPost.text?.toString() ?: ""
            if (postText.isEmpty() && viewModel.photoUri == null) {
                Toast.makeText(requireContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setDebounceClickListener
            }
            viewModel.onFinishClick(postText)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mode.isEdit) {
            val blog = mode.post!!
            if (blog.spans != null && blog.spans!!.isNotEmpty()) {
                viewModel.addSpans(blog.spans!!)
            }
            binding.frgPostEtPost.setText(
                getSpannableText(blog.spans, blog.text),
                TextView.BufferType.EDITABLE
            )
            val pictureId = blog.pictureId
            if (pictureId != null) {
                viewModel.savePhoto(pictureUrlHelper.getPictureUrl(pictureId).toStringUrl(), requireContext())

                binding.frgPostIbRemovePhoto.visibility = View.VISIBLE
                binding.frgPostMbAddPhoto.visibility = View.GONE
            } else {
                binding.frgPostIbRemovePhoto.visibility = View.GONE
                binding.frgPostMbAddPhoto.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.photoState.collect {
                if (it != null) {
                    binding.frgPostMbAddPhoto.visibility = View.GONE
                    binding.frgPostIvImage.visibility = View.VISIBLE
                    binding.frgPostIbRemovePhoto.visibility = View.VISIBLE
                    Glide.with(binding.root)
                        .load(it)
                        .centerCrop()
                        .into(binding.frgPostIvImage)
                } else {
                    binding.frgPostMbAddPhoto.visibility = View.VISIBLE
                    binding.frgPostIvImage.visibility = View.GONE
                    binding.frgPostIbRemovePhoto.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.spans.filterNotNull().collect {
                binding.frgPostEtPost.setText(
                    getSpannableText(
                        it,
                        binding.frgPostEtPost.text?.toString() ?: ""
                    ), TextView.BufferType.EDITABLE
                )
            }
        }

        lifecycleScope.launch {
            viewModel.buttonsState.collect {
                binding.frgPostFinishPost.isEnabled = it
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<DiaryComponent>(this, DiaryApi::class.java)
            .postComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        const val MODE_KEY = "ru.spbstu.ru.MODE_KEY"

        @JvmStatic
        fun newInstance(mode: Mode) = PostFragment().apply {
            arguments = bundleOf(MODE_KEY to mode)
        }
    }

    @Parcelize
    data class Mode(val isBlog: Boolean, val isEdit: Boolean, val post: Blog? = null) : Parcelable

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        requireActivity().menuInflater.inflate(R.menu.text_selection_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        val start = binding.frgPostEtPost.selectionStart
        val end = binding.frgPostEtPost.selectionEnd
        return when (item?.itemId) {
            R.id.bold -> {
                viewModel.addSpan("BOLD".toSpanType(), start, end)
                true
            }
            R.id.italic -> {
                viewModel.addSpan("ITALIC".toSpanType(), start, end)
                true
            }
            R.id.underline -> {
                viewModel.addSpan("UNDERLINE".toSpanType(), start, end)
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
    }
}