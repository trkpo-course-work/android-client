package ru.spbstu.common.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import org.commonmark.parser.Parser
import ru.spbstu.common.api.model.SpanType
import ru.spbstu.common.domain.Span

fun getSpannableText(parser: Parser, context: Context, text: String): Spanned {
    return Markwon.builder(context).usePlugin(HtmlPlugin.create()).build().render(parser.parse(text.replace("\n", "  \n")))
}
