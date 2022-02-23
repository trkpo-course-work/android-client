package ru.spbstu.common.extensions

import android.view.View
import ru.spbstu.common.utils.DebounceClickListener
import ru.spbstu.common.utils.DebouncePostHandler

fun View.setDebounceClickListener(
    delay: Long = DebouncePostHandler.DEFAULT_DELAY,
    onClickListener: View.OnClickListener
) {
    setOnClickListener(DebounceClickListener(delay, onClickListener))
}