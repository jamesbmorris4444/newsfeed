package com.fullsekurity.newsfeed.digitalfootprint

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.ObservableField
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.utils.CircularView


class DigitalFootprintItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val source: ObservableField<String> = ObservableField("")
    val author: ObservableField<String> = ObservableField("")
    val title: ObservableField<String> = ObservableField("")
    val description: ObservableField<String> = ObservableField("")
    val url: ObservableField<String> = ObservableField("")
    val circularDrawable: ObservableField<Drawable> = ObservableField()
    val publishedAt: ObservableField<String> = ObservableField("")
    val content: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        source.set(item.source.name)
        author.set(item.author)
        title.set(item.title)
        description.set(item.description)
        url.set(item.url)
        circularDrawable.set(circDrawable())
        publishedAt.set(item.publishedAt)
        content.set(item.content)
    }

    private fun circDrawable(): Drawable {
        val circularView = CircularView(callbacks.fetchActivity())
        return BitmapDrawable(callbacks.fetchActivity().resources, createBitmapFromView(circularView))
    }

    fun createBitmapFromView(view: View): Bitmap {
        view.layout(0, 0, 160, 160)
        val bitmap = Bitmap.createBitmap(
            160,
            160, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
//        val background: Drawable = view.getBackground()
//        background?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }

}