package ru.spbstu.diary.diary.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.spbstu.diary.user_blog.presentation.UserBlogFragment
import ru.spbstu.diary.user_diary.presentation.UserDiaryFragment

class DiaryPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> {
            UserBlogFragment.newInstance()
        }
        1 -> {
            UserDiaryFragment.newInstance()
        }
        else -> throw IllegalArgumentException()
    }

}