package com.zaskha.storyapepe.ui.detailStory

import androidx.lifecycle.ViewModel
import com.zaskha.storyapepe.data.DetailStoryItem

class DetailStoryViewModel : ViewModel() {
    lateinit var item: DetailStoryItem

    fun item(story: DetailStoryItem): DetailStoryItem {
        item = story
        return item
    }

}