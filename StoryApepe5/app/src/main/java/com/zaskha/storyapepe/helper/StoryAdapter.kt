package com.zaskha.storyapepe.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.zaskha.storyapepe.R
import com.zaskha.storyapepe.helper.StoryAdapter.ViewHolder
import com.zaskha.storyapepe.data.DetailStoryItem
import com.zaskha.storyapepe.databinding.ListStoryItemBinding
import com.zaskha.storyapepe.databinding.ListStoryItemBinding.*
import com.zaskha.storyapepe.ui.detailStory.DetailStoryActivity

class StoryAdapter : Adapter<ViewHolder>() {

    private val arrayList = ArrayList<DetailStoryItem>()

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        index: Int,
    ) {
        viewHolder.binding(arrayList[index])
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        type: Int,
    ): ViewHolder {
        return this.ViewHolder(inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }


    override fun getItemCount(): Int {
        return this.arrayList.size
    }

    fun storyListItem(item: List<DetailStoryItem>) {
        val result = calculateDiff(DiffUtils(arrayList, newIndex = item))

        with(arrayList) {
            clear()
            addAll(item)
        }
        result.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private var listStoryItemBinding: ListStoryItemBinding) :
        RecyclerView.ViewHolder(listStoryItemBinding.root) {
        fun binding(listStoryItem: DetailStoryItem): Unit =
            with(listStoryItemBinding, fun ListStoryItemBinding.() {
                Glide.with(this.image)
                    .load(listStoryItem.photo)
                    .error(R.drawable.ic_break_image)
                    .placeholder(R.drawable.ic_image_default)
                    .into(image)
                listStoryItem.name.also {
                    name.text = it
                }
                listStoryItem.desc.also {
                    desc.text = it
                }

                image.setOnClickListener { view ->
                    view.context.startActivity(
                        Intent(
                            view.context, DetailStoryActivity::class.java
                        ).putExtra(DetailStoryActivity.EXTRA_DETAIL_STORY, listStoryItem)
                    )
                }
            })
    }

}