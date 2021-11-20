package ru.spbstu.diary

import ru.spbstu.common.domain.Blog

interface DiaryRouter {
    fun navigateToPostFragment(isBlog: Boolean, isEdit: Boolean, blog: Blog?)
    fun pop()
}