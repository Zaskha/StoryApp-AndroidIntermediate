package com.zaskha.storyapepe.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("loginResult")
    val result: LoginResult,

    @field:SerializedName("error")
    val err: Boolean,

    @field:SerializedName("message")
    val msg: String,
)

