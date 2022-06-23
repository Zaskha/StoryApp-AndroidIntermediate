package com.zaskha.storyapepe.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zaskha.storyapepe.data.UserModel
import com.zaskha.storyapepe.data.UserPreference
import kotlinx.coroutines.launch


class MainViewModel(private val uPrefer: UserPreference) : ViewModel() {

    fun data(): LiveData<UserModel> = this.uPrefer.user().asLiveData()

    fun keepUser(user: UserModel) = this.viewModelScope.launch { uPrefer.keepUser(user) }

    fun logout() = this.viewModelScope.launch { uPrefer.logout() }

}