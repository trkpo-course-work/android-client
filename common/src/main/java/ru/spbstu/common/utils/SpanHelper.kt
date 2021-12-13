package ru.spbstu.common.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import ru.spbstu.common.api.model.SpanType
import ru.spbstu.common.domain.Span

fun getSpannableText(spans: List<Span>?, text: String): Spannable {
    if (spans == null || spans.isEmpty()) return SpannableString(text)
    val spannable = SpannableString(text)
    spans.forEach {
        if (it.end > text.length + 1) return spannable
        when (it.type) {
            SpanType.BOLD -> {
                spannable.setSpan(StyleSpan(Typeface.BOLD), it.start, it.end, 1)
            }
            SpanType.UNDERLINE -> {
                spannable.setSpan(UnderlineSpan(), it.start, it.end, 1)
            }
            SpanType.ITALIC -> {
                spannable.setSpan(StyleSpan(Typeface.ITALIC), it.start, it.end, 1)
            }
        }
    }
    return spannable
}
