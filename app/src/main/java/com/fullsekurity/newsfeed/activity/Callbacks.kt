package com.fullsekurity.newsfeed.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.fullsekurity.newsfeed.meanings.MeaningsListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchmeaningsListViewModel() : MeaningsListViewModel?
}