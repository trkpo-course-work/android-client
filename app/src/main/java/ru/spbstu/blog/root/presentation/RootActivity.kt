package ru.spbstu.blog.root.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.spbstu.blog.R
import ru.spbstu.blog.databinding.ActivityMainBinding
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.blog.root.di.RootApi
import ru.spbstu.blog.root.di.RootComponent
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.events.AuthEvent
import javax.inject.Inject

class RootActivity : AppCompatActivity() {

    @Inject
    lateinit var activityViewModel: RootActivityViewModel

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var api: Api

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navigator.attach(navController, this)
        api.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful) {
                    navigator.openMainPage()
                }
            }, {
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.detach()
        EventBus.getDefault().unregister(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    private fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .rootActivityComponentFactory()
            .create(this)
            .inject(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AuthEvent) {
        navigator.goToLogin()
    }
}