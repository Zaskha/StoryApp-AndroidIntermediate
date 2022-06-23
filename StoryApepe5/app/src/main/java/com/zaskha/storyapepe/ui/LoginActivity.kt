package com.zaskha.storyapepe.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.zaskha.storyapepe.MainActivity
import com.zaskha.storyapepe.R.string.*
import com.zaskha.storyapepe.data.LoginResponse
import com.zaskha.storyapepe.data.UserModel
import com.zaskha.storyapepe.data.UserPreference.Companion.getInstance
import com.zaskha.storyapepe.databinding.ActivityLoginBinding
import com.zaskha.storyapepe.helper.MainViewModel
import com.zaskha.storyapepe.helper.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.zaskha.storyapepe.data.ApiConfig.Companion as ApiConfig1

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel()

        playAnimation()

        binding.etEmailLogin.type = "email"
        binding.etPasswordLogin.type = "password"

        binding.btnLogIn.setOnClickListener {
            login(
                binding.etEmailLogin.text.toString(),
                binding.etPasswordLogin.text.toString()
            )
        }

        binding.etRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun viewModel() {
        ViewModelProvider(
            this,
            ViewModelFactory(getInstance(dataStore))
        )[MainViewModel::class.java].also { model = it }

        model.data().observe(this) { user ->
            if (user.hasLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun login(mail: String, pw: String) {
        showProgressbar(true)

        ApiConfig1.service().login(email = mail, pass = pw).also {
            it.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    _return: Response<LoginResponse>,
                ) {
                    showProgressbar(false)
                    val loginReturn = _return.body()
                    Log.d(TAG, "onResponse: $loginReturn")
                    when {
                        !_return.isSuccessful || loginReturn?.msg != "success" -> {
                            Log.e(TAG, "onFailure: ${_return.message()}")
                            makeText(this@LoginActivity,
                                getString(login_failed),
                                LENGTH_SHORT).show()
                        }
                        else -> {
                            model.keepUser(UserModel(loginReturn.result.token, true))
                            makeText(this@LoginActivity,
                                getString(login_success),
                                LENGTH_SHORT).show()
                            this@LoginActivity.startActivity(Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            ))

                        }
                    }
                }

                override fun onFailure(
                    call: Call<LoginResponse>,
                    throwable: Throwable,
                ) {
                    showProgressbar(false)
                    Log.e(TAG, "onFailure: ${throwable.message}")
                    makeText(this@LoginActivity, getString(login_failed), LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.login, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.etEmailLogin, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.etPasswordLogin, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnLogIn, View.ALPHA, 1f).setDuration(500)
        val belum = ObjectAnimator.ofFloat(binding.tvBelum, View.ALPHA, 1f).setDuration(500)
        val regis = ObjectAnimator.ofFloat(binding.etRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(belum, regis)
        }

        AnimatorSet().apply {
            playSequentially(email, pass, button, together)
            start()
        }
    }


    private fun showProgressbar(progress: Boolean) {
        if (progress) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}