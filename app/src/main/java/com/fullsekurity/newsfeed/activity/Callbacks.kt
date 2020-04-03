package com.fullsekurity.newsfeed.activity

import android.view.View
import com.fullsekurity.newsfeed.digitalfootprint.DigitalFootprintListViewModel
import com.fullsekurity.newsfeed.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchMeaningsListViewModel() : MeaningsListViewModel?
    fun fetchDigitalFootprintListViewModel() : DigitalFootprintListViewModel?
}