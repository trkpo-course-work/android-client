package ru.spbstu.blog.navgiation

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.spbstu.auth.AuthRouter
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.RootRouter

class Navigator : RootRouter, AuthRouter {

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
}