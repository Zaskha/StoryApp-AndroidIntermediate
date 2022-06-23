package com.zaskha.storyapepe.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaskha.storyapepe.data.UserPreference

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(clazz: Class<T>): T {
        return if (clazz.isAssignableFrom(MainViewModel::class.java)) MainViewModel(pref) as T
        else throw IllegalArgumentException("View Model tidak diketahui, yaitu class: " + clazz.name)
    }

//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//        fun getInstance(): ViewModelFactory {
////            return instance ?: synchronized(this) {
//////                val viewModelFactory = ViewModelFactory(Injection.provideUserRepository())
////                instance = viewModelFactory
////                viewModelFactory
////            }
////        }
//    }
}