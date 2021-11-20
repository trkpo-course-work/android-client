package ru.spbstu.blog.navgiation

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.spbstu.auth.AuthRouter
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.RootRouter
import ru.spbstu.common.domain.Blog
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.post.presentation.PostFragment
import ru.spbstu.diary.post.presentation.PostFragment.Companion.MODE_KEY
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.search.SearchRouter
import ru.spbstu.wall.WallRouter

class Navigator : RootRouter, AuthRouter, WallRouter, SearchRouter, ProfileRouter, DiaryRouter {

    private var navController: NavController? = null
    private var activity: AppCompatActivity? = null
    private val bottomNavController: NavController? by lazy {
        activity?.findNavController(R.id.bottomNavHost)
    }

    fun attach(navController: NavController, activity: AppCompatActivity) {
        this.navController = navController
        this.activity = activity
    }

    fun detach() {
        navController = null
        activity = null
    }

    override fun navigateToFavorites() {
        bottomNavController?.navigate(R.id.action_profileFragment_to_favoritesFragment)
    }

    override fun navigateToProfileEdit() {
        bottomNavController?.navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    override fun pop() {
        bottomNavController?.popBackStack()
    }

    override fun openMainPage() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }

    override fun openUserProfile() {
        bottomNavController?.navigate(R.id.action_blogFragment_to_userProfileFragment)
    }

    override fun navigateToPostFragment(isBlog: Boolean, isEdit: Boolean, blog: Blog?) {
        bottomNavController?.navigate(
            R.id.action_diaryFragment_to_postFragment,
            bundleOf(MODE_KEY to PostFragment.Mode(isBlog, isEdit, blog))
        )
    }
}