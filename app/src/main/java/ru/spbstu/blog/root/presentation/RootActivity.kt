package ru.spbstu.blog.root.presentation

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.spbstu.blog.R
import ru.spbstu.blog.databinding.ActivityMainBinding
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.blog.root.di.RootApi
import ru.spbstu.blog.root.di.RootComponent
import ru.spbstu.common.di.FeatureUtils
import javax.inject.Inject

class RootActivity : AppCompatActivity() {

    @Inject
    lateinit var activityViewModel: RootActivityViewModel

    @Inject
    lateinit var navigator: Navigator

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navigator.attach(navController, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.detach()
    }

    private fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .rootActivityComponentFactory()
            .create(this)
            .inject(this)
    }
}