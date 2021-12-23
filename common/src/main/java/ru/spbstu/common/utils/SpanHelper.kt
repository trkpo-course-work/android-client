package ru.spbstu.common.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import io.noties.markwon.Markwon
import org.commonmark.parser.Parser
import ru.spbstu.common.api.model.SpanType
import ru.spbstu.common.domain.Span

fun getSpannableText(parser: Parser, context: Context, text: String): Spanned {
    return Markwon.create(context).render(parser.parse(text))
}
