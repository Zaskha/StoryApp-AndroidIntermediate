package com.zaskha.storyapepe.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaskha.storyapepe.helper.ApiCallbackString
import com.zaskha.storyapepe.data.ApiConfig
import com.zaskha.storyapepe.data.ApiResponse
import com.zaskha.storyapepe.data.UserModel
import okhttp3.MultipartBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {
    private val liveData = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = liveData

    fun uploadFile(
        desc: String,
        user: UserModel,
        imgMultipart: MultipartBody.Part,
        string: ApiCallbackString,
    ) {
        liveData.value = true
        ApiConfig().apiService().addStory(
            "Bearer ${user.token}",
            desc,
            imgMultipart
        )
            .enqueue(
                object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, resp: Response<ApiResponse>) {
                        when {
                            resp.isSuccessful -> {
                                val apiResponse = resp.body()
                                when {
                                    apiResponse != null && !apiResponse.err -> with(string) {
                                        this.onResponse(
                                            resp.body() != null,
                                            COMPLETE
                                        )
                                    }
                                }
                            }
                            else -> {
                                val json = JSONTokener(
                                    (resp.errorBody()
                                        ?: throw NullPointerException("Expression 'resp.errorBody()' must not be null")).toString(),
                                ).nextValue() as JSONObject
                                with(string) {
                                    onResponse(
                                        false,
                                        json.getString("message")
                                    )
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, throwable: Throwable) {
                        false.apply { liveData.value = this }
                        with(string) { this.onResponse(false, throwable.message.toString()) }
                    }
                },
            )

    }

    companion object {
        private const val COMPLETE = "COMPLETE"
    }
}