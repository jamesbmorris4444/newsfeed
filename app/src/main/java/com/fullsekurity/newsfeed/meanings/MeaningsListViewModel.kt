package com.fullsekurity.newsfeed.meanings

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.newsfeed.R
import com.fullsekurity.newsfeed.activity.Callbacks
import com.fullsekurity.newsfeed.recyclerview.RecyclerViewViewModel
import com.fullsekurity.newsfeed.repository.Repository
import com.fullsekurity.newsfeed.repository.storage.Meaning
import com.fullsekurity.newsfeed.ui.UIViewModel
import com.fullsekurity.newsfeed.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.newsfeed.utils.Utils
import com.fullsekurity.newsfeed.utils.ViewModelInjectorModule
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class MeaningsListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MeaningsListViewModel(callbacks) as T
    }
}

class MeaningsListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    override var adapter: MeaningsAdapter = MeaningsAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)
    val submitVisible: ObservableField<Int> = ObservableField(View.GONE)

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
        val textInputLayout: TextInputLayout = view.findViewById(R.id.edit_text_input_name)
        val textInputEditText: TextInputEditText = view.findViewById(R.id.edit_text_input_name_editText)
        textInputLayout.setHintTextAppearance(uiViewModel.editTextDisplayModifyHintStyle)
        textInputEditText.requestFocus()
        Utils.showKeyboard(textInputEditText)
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        if (key.isEmpty()) {
            submitVisible.set(View.GONE)
        } else {
            submitVisible.set(View.VISIBLE)
        }
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.meanings_hint_text))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    fun onSearchClicked(view: View) {
        val enteredText: String = editTextNameInput.get() ?: ""
        if (enteredText == "s") {
            callbacks.fetchActivity().startPretendLongRunningTask()
        } else if (enteredText == "p") {
            callbacks.fetchActivity().pausePretendLongRunningTask()
        } else if (enteredText == "r") {
            callbacks.fetchActivity().resumePretendLongRunningTask()
        } else {
            Utils.hideKeyboard(view)
            val progressBar = callbacks.fetchActivity().getMainProgressBar()
            progressBar.visibility = View.VISIBLE
            repository.getUrbanDictionaryMeanings(enteredText, this::showMeanings)
        }
    }

    private fun showMeanings(meaningsList: List<Meaning>?) {
        val progressBar = callbacks.fetchActivity().getMainProgressBar()
        progressBar.visibility = View.GONE
        if (meaningsList == null) {
            listIsVisible.set(false)
        } else {
            listIsVisible.set(meaningsList.isNotEmpty())
            adapter.addAll(meaningsList)
        }
    }

}