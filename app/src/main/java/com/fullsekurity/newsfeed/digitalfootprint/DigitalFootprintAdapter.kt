package com.fullsekurity.newsfeed.digitalfootprint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.newsfeed.R
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.databinding.DigitalFootprintListItemBinding
import com.fullsekurity.newsfeed.databinding.DigitalFootprintSectionItemBinding
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.ui.UIViewModel
import com.fullsekurity.newsfeed.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.newsfeed.utils.ViewModelInjectorModule
import javax.inject.Inject

class DigitalFootprintAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Any, RecyclerViewItemViewModel<Any>>() {

    private var adapterFilter: AdapterFilter? = null

    @Inject
    lateinit var uiViewModel: UIViewModel

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    enum class ViewTypes {
        HEADER,
        NEWS
    }

    override fun getItemViewType(position: Int): Int {
        return when(itemList[position]) {
            is Meaning -> ViewTypes.NEWS.ordinal
            is String -> ViewTypes.HEADER.ordinal
            else -> ViewTypes.NEWS.ordinal
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> {
        when (viewType) {
            ViewTypes.HEADER.ordinal -> {
                val digitalFootprintSectionItemBinding: DigitalFootprintSectionItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.digital_footprint_section_item, parent, false)
                val digitalFootPrintSectionViewModel = DigitalFootprintSectionViewModel(callbacks)
                digitalFootprintSectionItemBinding.digitalFootprintSectionViewModel = digitalFootPrintSectionViewModel
                digitalFootprintSectionItemBinding.uiViewModel = uiViewModel
                return DigitalFootPrintSectionViewHolder(digitalFootprintSectionItemBinding.root, digitalFootPrintSectionViewModel as RecyclerViewItemViewModel<Any>, digitalFootprintSectionItemBinding)
            }
            ViewTypes.NEWS.ordinal -> {
                val digitalFootprintListItemBinding: DigitalFootprintListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.digital_footprint_list_item, parent, false)
                val digitalFootPrintItemViewModel = DigitalFootprintItemViewModel(callbacks)
                digitalFootprintListItemBinding.digitalFootprintItemViewModel = digitalFootPrintItemViewModel
                digitalFootprintListItemBinding.uiViewModel = uiViewModel
                return DigitalFootPrintItemViewHolder(digitalFootprintListItemBinding.root, digitalFootPrintItemViewModel as RecyclerViewItemViewModel<Any>, digitalFootprintListItemBinding)
            }
            else -> {
                val digitalFootprintListItemBinding: DigitalFootprintListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.digital_footprint_list_item, parent, false)
                val digitalFootPrintItemViewModel = DigitalFootprintItemViewModel(callbacks)
                digitalFootprintListItemBinding.digitalFootprintItemViewModel = digitalFootPrintItemViewModel
                digitalFootprintListItemBinding.uiViewModel = uiViewModel
                return DigitalFootPrintItemViewHolder(digitalFootprintListItemBinding.root, digitalFootPrintItemViewModel as RecyclerViewItemViewModel<Any>, digitalFootprintListItemBinding)
            }
        }
    }

    inner class DigitalFootPrintItemViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: DigitalFootprintListItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    inner class DigitalFootPrintSectionViewHolder internal constructor(itemView: View, viewModel: RecyclerViewItemViewModel<Any>, viewDataBinding: DigitalFootprintSectionItemBinding) :
        ItemViewHolder<Any, RecyclerViewItemViewModel<Any>> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Any, RecyclerViewItemViewModel<Any>>, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun itemFilterable(item: Any, constraint: String): Boolean {
        return true
    }

}