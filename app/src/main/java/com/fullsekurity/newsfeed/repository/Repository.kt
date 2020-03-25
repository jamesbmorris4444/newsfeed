package com.fullsekurity.newsfeed.repository

import com.fullsekurity.newsfeed.R
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.activity.MainActivity
import com.fullsekurity.newsfeed.logger.LogUtils
import com.fullsekurity.newsfeed.modal.StandardModal
import com.fullsekurity.newsfeed.repository.network.APIClient
import com.fullsekurity.newsfeed.repository.network.APIInterface
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository(private val callbacks: Callbacks) {

    private val meaningsService: APIInterface = APIClient.client

    fun getUrbanDictionaryMeanings(country: String, showMeanings: (meaningsList: List<Meaning>?) -> Unit) {
        var disposable: Disposable? = null
        disposable = meaningsService.getMeanings(country, Constants.NEWSFEED_API_KEY)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ meaningsResponse ->
                disposable?.dispose()
                showMeanings(meaningsResponse.articles)
            },
            { throwable ->
                disposable?.dispose()
                showMeanings(null)
                getUrbanDictionaryMeaningsFailure(callbacks.fetchActivity(),"getUrbanDictionaryMeanings", throwable)
            })
    }

    private fun getUrbanDictionaryMeaningsFailure(activity: MainActivity, method: String, throwable: Throwable) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), method, throwable)
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            titleText = activity.getString(R.string.std_modal_urban_dictionary_failure_title),
            bodyText = activity.getString(R.string.std_modal_urban_dictionary_failure_body),
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) { }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() { }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

}