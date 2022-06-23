package com.zaskha.storyapepe

import android.animation.AnimatorSet
import android.animation.ObjectAnimator.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.ALPHA
import androidx.appcompat.app.AlertDialog.*
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.zaskha.storyapepe.R.string.*
import com.zaskha.storyapepe.ui.listStory.ListStoryActivity
import com.zaskha.storyapepe.ui.listStory.ListStoryActivity.Companion.EXTRA_USER
import com.zaskha.storyapepe.ui.LoginActivity
import com.zaskha.storyapepe.data.UserModel
import com.zaskha.storyapepe.data.UserPreference.Companion.getInstance
import com.zaskha.storyapepe.databinding.ActivityMainBinding
import com.zaskha.storyapepe.helper.MainViewModel
import com.zaskha.storyapepe.helper.ViewModelFactory

val Context.store: DataStore<Preferences> by preferencesDataStore("settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel
    private lateinit var user: UserModel

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
        model()
        animation()
    }

    private fun listener() {
        binding.btnToListStory.setOnClickListener {
            startActivity(Intent(
                this@MainActivity,
                ListStoryActivity::class.java
            ).putExtra(EXTRA_USER, user),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this)
                    .toBundle())
        }
        binding.btnLogOut.setOnClickListener {
            with(model) { logout() }
            Builder(this).apply {
                setTitle(getString(say))
                setMessage(getString(log_out_success))
                setPositiveButton(getString(ok)) { _, _ ->
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun model() {
        ViewModelProvider(
            this,
            ViewModelFactory(getInstance(store))
        )[MainViewModel::class.java].also { model = it }

        model.data().observe(this) {
            UserModel(
                it.token,
                true
            ).also {
                user = it
            }
        }
    }

    private fun animation() {
        ofFloat(binding.imageView, View.TRANSLATION_X, -38f, 38f).apply {
            duration = 6000
            repeatCount = INFINITE
            repeatMode = REVERSE
        }.start()

        val list = ofFloat(binding.btnToListStory, ALPHA, 1f).setDuration(500)
        val logout = ofFloat(binding.btnLogOut, ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(list, logout)
            startDelay = 500
        }.start()
    }


}