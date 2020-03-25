package com.fullsekurity.newsfeed.services

interface ServiceCallbacks {
    fun setServiceProgress(progress: Int)
    fun setProgressMaxValue(maxValue: Int)
}