package com.fullsekurity.newsfeed.digitalfootprint

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.fullsekurity.newsfeed.R
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.utils.CircularView
import java.util.*


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
        val outerColorNormal = ContextCompat.getColor(callbacks.fetchActivity(), R.color.blue)
        val innerColorNormal = ContextCompat.getColor(callbacks.fetchActivity(), R.color.darkGreen)
        val outerColorLight = ContextCompat.getColor(callbacks.fetchActivity(), R.color.blueLight)
        val innerColorLight = ContextCompat.getColor(callbacks.fetchActivity(), R.color.darkGreenLight)
        val random = Random()
        var percentNumeratorOuter = random.nextInt(9)
        if (percentNumeratorOuter == 0) {
            percentNumeratorOuter = 8
        }
        val outerColor = if (percentNumeratorOuter == 8) outerColorLight else outerColorNormal
        var percentNumeratorInner = random.nextInt(9)
        if (percentNumeratorInner == 0) {
            percentNumeratorInner = 8
        }
        val innerColor = if (percentNumeratorInner == 8) innerColorLight else innerColorNormal
        val circularView = CircularView(callbacks.fetchActivity(), percentNumeratorOuter, 8, outerColor, outerColorLight, percentNumeratorInner, 8, innerColor, innerColorLight)
        return BitmapDrawable(callbacks.fetchActivity().resources, createBitmapFromView(circularView))
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val boundingBoxWidth = convertDpToPixels(callbacks.fetchActivity().resources.getDimension(R.dimen.circle_bounding_box))
        view.layout(0, 0, boundingBoxWidth, boundingBoxWidth)
        val bitmap = Bitmap.createBitmap(boundingBoxWidth, boundingBoxWidth, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun convertDpToPixels(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, callbacks.fetchActivity().resources.displayMetrics).toInt()
    }

}