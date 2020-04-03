package com.fullsekurity.newsfeed.digitalfootprint

import androidx.databinding.ObservableField
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.newsfeed.repository.storage.Meaning

class DigitalFootprintItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<Meaning>() {

    val source: ObservableField<String> = ObservableField("")
    val author: ObservableField<String> = ObservableField("")
    val title: ObservableField<String> = ObservableField("")
    val description: ObservableField<String> = ObservableField("")
    val url: ObservableField<String> = ObservableField("")
    val urlToImage: ObservableField<String> = ObservableField("")
    val publishedAt: ObservableField<String> = ObservableField("")
    val content: ObservableField<String> = ObservableField("")

    override fun setItem(item: Meaning) {
        source.set(item.source.name)
        author.set(item.author)
        title.set(item.title)
        description.set(item.description)
        url.set(item.url)
        item.urlToImage?.let {
            if (it.isEmpty()) {
                urlToImage.set("https://images.barrons.com/im-150385/social")
            } else {
                urlToImage.set(it)
            }
        } ?: run {
            urlToImage.set("https://images.barrons.com/im-150385/social")
        }
        publishedAt.set(item.publishedAt)
        content.set(item.content)
    }

}