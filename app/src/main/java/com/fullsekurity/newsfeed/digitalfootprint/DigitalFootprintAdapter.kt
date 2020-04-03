package com.fullsekurity.newsfeed.digitalfootprint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.newsfeed.R
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.databinding.DigitalFootprintListItemBinding
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.ui.UIViewModel
import com.fullsekurity.newsfeed.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.newsfeed.utils.ViewModelInjectorModule
import javax.inject.Inject

class DigitalFootprintAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<Meaning, DigitalFootprintItemViewModel>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalFootPrintViewHolder {
        val digitalFootprintListItemBinding: DigitalFootprintListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.digital_footprint_list_item, parent, false)
        val digitalFootPrintItemViewModel = DigitalFootprintItemViewModel(callbacks)
        digitalFootprintListItemBinding.digitalFootprintItemViewModel = digitalFootPrintItemViewModel
        digitalFootprintListItemBinding.uiViewModel = uiViewModel
        return DigitalFootPrintViewHolder(digitalFootprintListItemBinding.root, digitalFootPrintItemViewModel, digitalFootprintListItemBinding)
    }

    inner class DigitalFootPrintViewHolder internal constructor(itemView: View, viewModel: DigitalFootprintItemViewModel, viewDataBinding: DigitalFootprintListItemBinding) :
        ItemViewHolder<Meaning, DigitalFootprintItemViewModel>(itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<Meaning, DigitalFootprintItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun itemFilterable(item: Meaning, constraint: String): Boolean {
        return true
    }

}