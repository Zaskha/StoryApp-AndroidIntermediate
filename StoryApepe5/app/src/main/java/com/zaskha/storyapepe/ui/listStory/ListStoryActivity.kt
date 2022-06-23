package com.zaskha.storyapepe.ui.listStory

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.zaskha.storyapepe.R
import com.zaskha.storyapepe.helper.StoryAdapter
import com.zaskha.storyapepe.data.UserModel
import com.zaskha.storyapepe.databinding.ActivityListStoryBinding
import com.zaskha.storyapepe.ui.addstory.AddStoryActivity
import com.zaskha.storyapepe.ui.listStory.ListStoryActivity.Companion.EXTRA_USER
import com.zaskha.storyapepe.ui.maps.MapsActivity


class ListStoryActivity : AppCompatActivity() {

    private var listStoryBinding: ActivityListStoryBinding? = null
    private val binding get() = listStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var userModel: UserModel
    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStoryBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userModel = intent.getParcelableExtra(EXTRA_USER)!!
        storyAdapter = StoryAdapter()

        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.adapter = storyAdapter
        binding?.rvStory?.setHasFixedSize(false)

        storyAction()
        listStory()
        bar()
        loading()
        dataProperties()


    }


    private fun storyAction() {
        binding?.ivAddStory?.setOnClickListener {
            val activityGo = Intent(this, AddStoryActivity::class.java)
            activityGo.putExtra(AddStoryActivity.EXTRA_USER, userModel)
            startActivity(activityGo)
        }
        binding?.ivMaps?.setOnClickListener {
            val activityGo = Intent(this, MapsActivity::class.java)
//            activityGo.putExtra(MapsActivity.EXTRA_USER, userModel)
            startActivity(activityGo)
        }
    }

    private fun listStory() {
        viewModel.item.observe(this) { list -> storyAdapter.storyListItem(list) }
        viewModel.story(userModel.token)
    }

    private fun bar() {
        viewModel.liveData.observe(this) { event ->
            event.ifNotControlled()?.let { barText ->
                make(
                    findViewById(R.id.rv_story),
                    barText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loading() {
        viewModel.loading.observe(this) {
            binding?.apply {
                when {
                    it -> {
                        progressBar.visibility = VISIBLE
                        rvStory.visibility = INVISIBLE
                    }
                    else -> {
                        progressBar.visibility = GONE
                        rvStory.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun dataProperties() {
        viewModel.availableData.observe(this) {
            binding?.apply {
                if (it) {
                    tvInfoNoStory.visibility = GONE
                } else {
                    tvInfoNoStory.visibility = VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        listStory()
    }

    override fun onDestroy() {
        super.onDestroy()
        listStoryBinding = null
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}