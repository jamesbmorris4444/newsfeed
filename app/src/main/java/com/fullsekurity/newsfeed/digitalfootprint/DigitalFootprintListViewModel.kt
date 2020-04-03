package com.fullsekurity.newsfeed.digitalfootprint

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewViewModel
import com.fullsekurity.newsfeed.repository.Repository
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.ui.UIViewModel
import com.fullsekurity.newsfeed.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.newsfeed.utils.Utils
import com.fullsekurity.newsfeed.utils.ViewModelInjectorModule
import javax.inject.Inject

class DigitalFootprintListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DigitalFootprintListViewModel(callbacks) as T
    }
}

class DigitalFootprintListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    override var adapter: DigitalFootprintAdapter = DigitalFootprintAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    fun initialize(view: View) {
        Utils.hideKeyboard(view)
        val progressBar = callbacks.fetchActivity().getMainProgressBar()
        progressBar.visibility = View.VISIBLE
        repository.getUrbanDictionaryMeanings("us", this::showDigitalFootprint)
    }

    private fun showDigitalFootprint(meaningsList: List<Meaning>?) {
        val progressBar = callbacks.fetchActivity().getMainProgressBar()
        progressBar.visibility = View.GONE
        if (meaningsList == null) {
            listIsVisible.set(false)
        } else {
            listIsVisible.set(meaningsList.isNotEmpty())
            val sortedMeaningsList: MutableList<Any> = meaningsList.sortedBy { meaning -> meaning.author }.toMutableList()
            val indexList: MutableList<Int> = mutableListOf()
            val headerList: MutableList<String> = mutableListOf()
            for (c in "ABCDEFGHIJKLMNOPQRSTUVWXYZ") {
                val insertionPoint = sortedMeaningsList.binarySearchBy(c.toString()) { meaning -> (meaning as Meaning).author }
                indexList.add(0, -(insertionPoint + 1))
                headerList.add(0, c.toString())
            }
            for (k in indexList.indices) {
                sortedMeaningsList.add(indexList[k], headerList[k])
            }
            adapter.addAll(sortedMeaningsList)
        }
    }

}