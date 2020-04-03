package com.fullsekurity.newsfeed.digitalfootprint

import androidx.databinding.ObservableField
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewItemViewModel

class DigitalFootprintSectionViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<String>() {

    val header: ObservableField<String> = ObservableField("")

    override fun setItem(item: String) {
        header.set(item)
    }

}