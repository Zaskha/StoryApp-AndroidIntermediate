package com.zaskha.storyapepe.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(

    @field:SerializedName("error")
    val err: Boolean,

    @field:SerializedName("message")
    val msg: String,
)
