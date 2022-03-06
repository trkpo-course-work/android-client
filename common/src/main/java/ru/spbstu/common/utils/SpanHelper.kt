package ru.spbstu.common.utils

import android.content.Context
import android.text.Spanned
import com.bumptech.glide.Glide
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import org.commonmark.parser.Parser

fun getSpannableText(parser: Parser, context: Context, text: String): Spanned {
    return Markwon.builder(context).usePlugin(HtmlPlugin.create())
        .usePlugin(GlideImagesPlugin.create(Glide.with(context)))
        .build()
        .render(parser.parse(text.replace("\n", "  \n")))
}
