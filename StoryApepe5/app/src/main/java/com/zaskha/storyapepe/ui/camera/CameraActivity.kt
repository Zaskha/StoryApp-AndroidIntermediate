package com.zaskha.storyapepe.ui.camera

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets.Type.statusBars
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.*
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCapture.OutputFileOptions.Builder
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.ProcessCameraProvider.getInstance
import androidx.core.content.ContextCompat.getMainExecutor
import com.zaskha.storyapepe.helper.file
import com.zaskha.storyapepe.helper.say
import com.zaskha.storyapepe.ui.addstory.AddStoryActivity.Companion.CAMERA_X_RESULT
import com.zaskha.storyapepe.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private var capture: ImageCapture? = null
    private var selector: CameraSelector = DEFAULT_BACK_CAMERA
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener {
            (when (selector) {
                DEFAULT_BACK_CAMERA -> DEFAULT_FRONT_CAMERA
                else -> DEFAULT_BACK_CAMERA
            }).also { cameraSelector -> selector = cameraSelector }
            camera()
        }
        binding.takePhoto.setOnClickListener {
            photo()
        }
    }


    private fun camera() {
        val listenableFuture = getInstance(this)
        listenableFuture.addListener({
            val processCameraProvider: ProcessCameraProvider = listenableFuture.get()
            capture = ImageCapture.Builder().build()

            try {
                processCameraProvider.unbindAll()
                processCameraProvider.bindToLifecycle(
                    this,
                    selector,
                    Preview.Builder()
                        .build()
                        .apply { setSurfaceProvider(binding.cameraView.surfaceProvider) },
                    capture
                )
            } catch (exc: Exception) {
                this.say("fail to open camera.")
            }
        }, getMainExecutor(this))
    }

    private fun photo() {
        val file = application.file()
        val takeImage = capture ?: return

        takeImage.takePicture(Builder(file).build(),
            getMainExecutor(this),
            object : OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    this@CameraActivity.say("Fail to take a picture.")
                }

                override fun onImageSaved(output: OutputFileResults) {
                    val data = Intent()
                    data.putExtra("picture", file)
                    data.putExtra(
                        "isBackCamera",
                        selector == DEFAULT_BACK_CAMERA
                    )
                    setResult(CAMERA_X_RESULT, data)
                    finish()
                }
            })
    }

    public override fun onResume() {
        super.onResume()
        systemUI()
        camera()
    }

    private fun systemUI() {
        @Suppress("DEPRECATION")
        (when {
            SDK_INT < R -> {
                window.setFlags(
                    FLAG_FULLSCREEN,
                    FLAG_FULLSCREEN
                )
            }
            else -> {
                window.insetsController?.hide(statusBars())
            }
        })
        supportActionBar?.hide()
    }
}