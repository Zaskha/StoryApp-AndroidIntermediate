package com.zaskha.storyapepe.data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //credential
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun regist(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String,
    ): Call<ApiResponse>

    // add story
    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") header: String,
        @Part("description") des: String,
        @Part file: MultipartBody.Part,
    ): Call<ApiResponse>

    // get all of stories
    @GET("stories")
    fun allStories(
        @Header("Authorization") token: String,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
    ): Call<ResponOfStories>
}