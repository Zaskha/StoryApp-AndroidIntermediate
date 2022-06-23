package com.zaskha.storyapepe.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val token: String,
    val hasLogin: Boolean,
) : Parcelable