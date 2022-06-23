package com.zaskha.storyapepe.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponOfStories(

    @field:SerializedName("listStory")
    val listStory: List<DetailStoryItem>,

    @field:SerializedName("error")
    val err: Boolean,

    @field:SerializedName("message")
    val msg: String,
) : Parcelable

@Parcelize
data class DetailStoryItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photo: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val desc: String,
//
//    @field:SerializedName("createdAt")
//    val createdAt: String,
//
//    @field:SerializedName("lon")
//    val lon: Double,
//
//    @field:SerializedName("lat")
//    val lat: Double

    ) : Parcelable
