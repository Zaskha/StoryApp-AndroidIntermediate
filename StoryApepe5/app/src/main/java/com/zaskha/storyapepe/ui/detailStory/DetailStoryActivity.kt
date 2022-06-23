package com.zaskha.storyapepe.ui.detailStory

import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.zaskha.storyapepe.R
import com.zaskha.storyapepe.data.DetailStoryItem
import com.zaskha.storyapepe.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var story: DetailStoryItem
    private val storyViewModel: DetailStoryViewModel by viewModels()

    @RequiresApi(Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        story = intent.getParcelableExtra(EXTRA_DETAIL_STORY)!!
        storyViewModel.item(story)
        toolbar()
        resultView()
    }

    private fun toolbar() {
        setSupportActionBar(binding.toolbarDetailStory)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun resultView() {
        with(binding) {
            tvDescriptionStory.text = storyViewModel.item.desc
            tvName.text = storyViewModel.item.name
            Glide.with(ivStoryImage)
                .load(storyViewModel.item.photo) // URL Avatar
                .placeholder(R.drawable.ic_image_default)
                .error(R.drawable.ic_break_image)
                .into(ivStoryImage)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DETAIL_STORY = "story"
    }
}