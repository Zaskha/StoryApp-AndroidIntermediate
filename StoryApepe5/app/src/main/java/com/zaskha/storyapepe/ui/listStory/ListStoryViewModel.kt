package com.zaskha.storyapepe.ui.listStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.zaskha.storyapepe.helper.Event
import com.zaskha.storyapepe.data.ResponOfStories
import com.zaskha.storyapepe.data.ApiConfig
import com.zaskha.storyapepe.data.DetailStoryItem

class ListStoryViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _item = MutableLiveData<List<DetailStoryItem>>()
    val item: LiveData<List<DetailStoryItem>> = _item

    private val _liveData = MutableLiveData<Event<String>>()
    val liveData: LiveData<Event<String>> = _liveData

    private val _availabledata = MutableLiveData<Boolean>()
    val availableData: LiveData<Boolean> = _availabledata

    fun story(token: String) {
        _loading.value = true
        _availabledata.value = true

        with(ApiConfig) {
            service().allStories("Bearer $token").enqueue(object : Callback<ResponOfStories> {
                override fun onResponse(
                    call: Call<ResponOfStories>,
                    response: Response<ResponOfStories>,
                ) {
                    _loading.value = false
                    when {
                        response.isSuccessful -> {
                            val responseBody = response.body()
                            when {
                                responseBody != null -> when {
                                    !responseBody.err -> {
                                        response.body()?.listStory.also {
                                            _item.value = it
                                        }
                                        (responseBody.msg == "Stories fetched successfully").also {
                                            _availabledata.value = it
                                        }
                                    }
                                }
                            }
                        }
                        else -> _liveData.value = Event()
                    }
                }

                override fun onFailure(call: Call<ResponOfStories>, t: Throwable) {
                    false.also {
                        _loading.value = it
                    }
                    _liveData.value = Event()
                }
            })
        }
    }
}