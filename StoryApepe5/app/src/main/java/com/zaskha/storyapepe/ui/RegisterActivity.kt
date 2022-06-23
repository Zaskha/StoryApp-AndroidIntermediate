package com.zaskha.storyapepe.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zaskha.storyapepe.R.string.register_failed
import com.zaskha.storyapepe.R.string.register_success
import com.zaskha.storyapepe.data.ApiConfig.Companion.service
import com.zaskha.storyapepe.data.ApiResponse
import com.zaskha.storyapepe.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.etLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.etNameRegister.type = "name"
        binding.etEmailRegister.type = "email"
        binding.etPasswordRegister.type = "password"

        binding.btnRegister.setOnClickListener {
            val name = binding.etNameRegister.text.toString()
            val mail = binding.etEmailRegister.text.toString()
            val pw = binding.etPasswordRegister.text.toString()

            register(name, mail, pw)
        }
    }

    private fun register(name: String, mail: String, pw: String) {
        showProgress(true)

        service().regist(name, mail, pw).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>,
            ) {
                showProgress(false)
                val apiResponse = response.body()
                Log.d(TAG, "onResponse: $apiResponse")
                when {
                    response.isSuccessful && apiResponse?.msg == "User created" -> {
                        Toast.makeText(this@RegisterActivity,
                            getString(register_success),
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                    else -> {
                        Log.e(TAG, "onFailure1: ${response.message()}")
                        Toast.makeText(this@RegisterActivity,
                            getString(register_failed),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                showProgress(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@RegisterActivity,
                    getString(register_failed),
                    Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showProgress(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.register, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val name = ObjectAnimator.ofFloat(binding.etNameRegister, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmailRegister, View.ALPHA, 1f).setDuration(500)
        val pass =
            ObjectAnimator.ofFloat(binding.etPasswordRegister, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val belum = ObjectAnimator.ofFloat(binding.tvBelum, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.etLogin, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(belum, login)
        }

        AnimatorSet().apply {
            playSequentially(
                name,
                email,
                pass,
                button,
                together
            )
            start()
        }
    }

    companion object {
        private const val TAG = "REGISTER_ACTIVITY"
    }

}