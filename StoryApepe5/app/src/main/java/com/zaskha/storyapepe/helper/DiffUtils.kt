package com.zaskha.storyapepe.helper

import com.zaskha.storyapepe.data.DetailStoryItem
import androidx.recyclerview.widget.DiffUtil.*

class DiffUtils(
    private val oldIndex: List<DetailStoryItem>,
    private val newIndex: List<DetailStoryItem>,
) : Callback() {

    override fun areItemsTheSame(
        oldIndexItem: Int,
        newIndexItem: Int,
    ): Boolean {
        return oldIndex[oldIndexItem].id == newIndex[newIndexItem].id
    }

    override fun areContentsTheSame(
        oldIndexItem: Int,
        newIndexItem: Int,
    ): Boolean {
        return oldIndex[oldIndexItem].id == newIndex[newIndexItem].id
    }

    override fun getNewListSize(): Int {
        return newIndex.size
    }

    override fun getOldListSize(): Int {
        return oldIndex.size
    }

}

interface ApiCallbackString {
    fun onResponse(success: Boolean, message: String)
}