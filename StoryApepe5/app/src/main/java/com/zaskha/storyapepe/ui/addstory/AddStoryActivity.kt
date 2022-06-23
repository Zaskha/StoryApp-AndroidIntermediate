package com.zaskha.storyapepe.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zaskha.storyapepe.R
import com.zaskha.storyapepe.ui.camera.CameraActivity
import com.zaskha.storyapepe.helper.*
import com.zaskha.storyapepe.data.UserModel
import com.zaskha.storyapepe.databinding.ActivityAddStoryBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private lateinit var user: UserModel
    private var file: File? = null
    private var bitmap: Bitmap? = null

    private val storyViewModel by viewModels<AddStoryViewModel>()


    override fun onRequestPermissionsResult(
        code: Int,
        permission: Array<String>,
        results: IntArray,
    ) {
        super.onRequestPermissionsResult(code, permission, results)
        when (code) {
            REQUEST_CODE_PERMISSIONS -> {
                when {
                    !permissionAllowed() -> {
                        Toast.makeText(
                            this,
                            getString(R.string.need_permission),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun permissionAllowed() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        getPermission()

        binding.btnCameraX.setOnClickListener { cameraX() }
        binding.btnGallery.setOnClickListener { gallery() }
        binding.btnUpload.setOnClickListener {
            if (!isDescNotEmpty()) {
                binding.etDescription.error = getString(R.string.no_input)
                return@setOnClickListener
            }
            if (!isFileNotNull()) {
                say(getString(R.string.no_added))
                return@setOnClickListener
            }
            image()
        }

        showProgressbar()
    }

    private fun isFileNotNull() = file != null

    private fun getPermission() {
        if (!permissionAllowed()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // cameraX
    private fun cameraX() {
        resultLauncher.launch(Intent(this, CameraActivity::class.java))
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            this.file = it.data?.getSerializableExtra("picture") as File
            bitmap =
                BitmapFactory.decodeFile(this.file?.path).bitmap(
                    it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                )
        }
        binding.ivPreviewImage.setImageBitmap(bitmap)
    }

    private fun gallery() {
        val target = Intent()
        target.action = Intent.ACTION_GET_CONTENT
        target.type = "image/*"
        intentGallery.launch(Intent.createChooser(target, "Choose a Picture"))
    }

    private val intentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val img: Uri = it.data?.data as Uri
            this.file = img.fileUri(this@AddStoryActivity)
            binding.ivPreviewImage.setImageURI(img)
        }
    }

    private fun image() {
        when {
            file != null -> {
                val file = (file as File).minimumImage()
                with(this.storyViewModel) {
                    uploadFile(binding.etDescription.text.toString(),
                        user,
                        MultipartBody.Part.createFormData(
                            "photo",
                            file.name,
                            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        ),
                        object : ApiCallbackString {
                            override fun onResponse(success: Boolean, message: String) {
                                dialog(success, message)
                            }
                        })
                }
            }
            else -> {
                this@AddStoryActivity.say(getString(R.string.no_added))
            }
        }
    }


    private fun isDescNotEmpty() = binding.etDescription.text.toString().trim().isNotEmpty()

    private fun dialog(param: Boolean, message: String) {
        when {
            param -> AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.say))
                setMessage(getString(R.string.upload_complete))
                setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                create()
                show()
            }
            else -> AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.say))
                setMessage(getString(R.string.upload_fail) + message)
                setPositiveButton(getString(R.string.ok)) { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
                create()
                show()
            }
        }
    }

    private fun showProgressbar() = storyViewModel.loading.observe(this) {
        binding.apply {
            when {
                it -> View.VISIBLE.also {
                    progressBar.visibility = it
                }
                else -> View.GONE.also {
                    progressBar.visibility = it
                }
            }
        }
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        const val EXTRA_USER = "user"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}