package com.zaskha.storyapepe.data


import com.zaskha.storyapepe.BuildConfig.*
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory.*

class ApiConfig {
    fun apiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(create())
            .client(Builder().addInterceptor(HttpLoggingInterceptor().setLevel(BODY)).build())
            .build()
            .create(ApiService::class.java)
    }

    companion object {
        fun service(): ApiService {
            return Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(create())
                .client(Builder().addInterceptor(HttpLoggingInterceptor().setLevel(BODY)).build())
                .build()
                .create(ApiService::class.java)
        }
    }
}