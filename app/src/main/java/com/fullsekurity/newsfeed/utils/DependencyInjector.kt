package com.fullsekurity.newsfeed.utils

import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.newsfeed.activity.MainActivity
import com.fullsekurity.newsfeed.digitalfootprint.DigitalFootprintAdapter
import com.fullsekurity.newsfeed.digitalfootprint.DigitalFootprintFragment
import com.fullsekurity.newsfeed.digitalfootprint.DigitalFootprintListViewModel
import com.fullsekurity.newsfeed.meanings.MeaningsAdapter
import com.fullsekurity.newsfeed.meanings.MeaningsFragment
import com.fullsekurity.newsfeed.meanings.MeaningsListViewModel
import com.fullsekurity.newsfeed.modal.StandardModal
import com.fullsekurity.newsfeed.repository.Repository
import com.fullsekurity.newsfeed.ui.UIViewModel
import com.fullsekurity.newsfeed.ui.UIViewModelFactory

import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperInjectorModule::class])
interface MapperDependencyInjector {
    fun inject(viewModel: UIViewModel)
}

@Singleton
@Component(modules = [ViewModelInjectorModule::class])
interface ViewModelDependencyInjector {
    fun inject(fragment: MeaningsFragment)
    fun inject(fragment: DigitalFootprintFragment)
    fun inject(modal: StandardModal)
    fun inject(viewModel: MeaningsListViewModel)
    fun inject(viewModel: DigitalFootprintListViewModel)
    fun inject(activity: MainActivity)
    fun inject(adapter: MeaningsAdapter)
    fun inject(adapter: DigitalFootprintAdapter)
}

@Module
class MapperInjectorModule {
    @Provides
    @Singleton
    fun colorMapperProvider() : ColorMapper {
        val colorMapper = ColorMapper()
        colorMapper.initialize()
        return colorMapper
    }
    @Provides
    @Singleton
    fun textSizeMapperProvider() : TextSizeMapper {
        val textSizeMapper = TextSizeMapper()
        textSizeMapper.initialize()
        return textSizeMapper
    }
    @Provides
    @Singleton
    fun typefaceMapperProvider() : TypefaceMapper {
        val typefaceMapper = TypefaceMapper()
        typefaceMapper.initialize()
        return typefaceMapper
    }
}

@Module
class ViewModelInjectorModule(val activity: MainActivity) {
    @Provides
    @Singleton
    fun uiViewModelProvider() : UIViewModel {
        return ViewModelProvider(activity, UIViewModelFactory(activity.application)).get(UIViewModel::class.java)
    }
    @Provides
    @Singleton
    fun repositoryProvider() : Repository {
        return activity.repository
    }

}