package ru.spbstu.diary.user_blog.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.common.domain.Blog
import ru.spbstu.diary.DiaryRouter

class UserBlogViewModel(private val router: DiaryRouter) : ViewModel() {

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _state.value = State(
            listOf(
                Blog(
                    1,
                    "akapolix",
                    "10.10.2010",
                    "LoremIpsum LoremIpsum LoremIpsum LoremIpsum LoremIpsum ",
                    "https://static.remove.bg/remove-bg-web/e88d40fe6b242c5a4872a70c3c93599d93563581/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png",
                    "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
                ),
                Blog(
                    2,
                    "akapolix",
                    "10.10.2010",
                    "LoremIpsum LoremIpsum LoremIpsum LoremIpsum LoremIpsum ",
                    "https://static.remove.bg/remove-bg-web/e88d40fe6b242c5a4872a70c3c93599d93563581/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png",
                    null
                ),
                Blog(
                    3,
                    "akapolix",
                    "10.10.2010",
                    "LoremIpsum LoremIpsum LoremIpsum LoremIpsum LoremIpsum ",
                    "https://static.remove.bg/remove-bg-web/e88d40fe6b242c5a4872a70c3c93599d93563581/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png",
                    "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
                ),
                Blog(
                    4,
                    "akapolix",
                    "10.10.2010",
                    "LoremIpsum LoremIpsum LoremIpsum LoremIpsum LoremIpsum ",
                    "https://static.remove.bg/remove-bg-web/e88d40fe6b242c5a4872a70c3c93599d93563581/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png",
                    "https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"
                ),
            )
        )
    }

    fun openPostFragment(isEdit: Boolean) {
        router.navigateToPostFragment(true, isEdit, null)
    }

    data class State(val blogs: List<Blog>)
}