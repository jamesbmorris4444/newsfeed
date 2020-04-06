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


class DigitalFootprintItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    private val OUTER_DENOMINATOR = 8
    private val INNER_DENOMINATOR = 5

    val source: ObservableField<String> = ObservableField("")
    val author: ObservableField<String> = ObservableField("")
    val title: ObservableField<String> = ObservableField("")
    val description: ObservableField<String> = ObservableField("")
    val url: ObservableField<String> = ObservableField("")
    val circularImage: ObservableField<Drawable> = ObservableField()
    val publishedAt: ObservableField<String> = ObservableField("")
    val content: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        author.set(item.author)
        title.set("${item.title}/$OUTER_DENOMINATOR")
        source.set(item.source.name)
        description.set("${item.description}/$INNER_DENOMINATOR")
        url.set(item.url)
        circularImage.set(circularDrawable(item))
        publishedAt.set(item.publishedAt)
        content.set(item.content)
    }

    private fun circularDrawable(item: Meaning): Drawable {
        val outerColorNormal = ContextCompat.getColor(callbacks.fetchActivity(), R.color.blue)
        val innerColorNormal = ContextCompat.getColor(callbacks.fetchActivity(), R.color.darkGreen)
        val outerColorLight = ContextCompat.getColor(callbacks.fetchActivity(), R.color.blueLight)
        val innerColorLight = ContextCompat.getColor(callbacks.fetchActivity(), R.color.darkGreenLight)
        var outerColor = outerColorNormal
        var percentNumeratorOuter = item.title.toInt()
        if (percentNumeratorOuter == 0) {
            outerColor = outerColorLight
            percentNumeratorOuter = OUTER_DENOMINATOR
        }
        var innerColor = innerColorNormal
        var percentNumeratorInner = item.description.toInt()
        if (percentNumeratorInner == 0) {
            innerColor = innerColorLight
            percentNumeratorInner = INNER_DENOMINATOR
        }
        val circularView = CircularView(callbacks.fetchActivity(), percentNumeratorOuter, OUTER_DENOMINATOR, outerColor, outerColorLight, percentNumeratorInner, INNER_DENOMINATOR, innerColor, innerColorLight)
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