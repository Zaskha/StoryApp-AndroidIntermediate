package com.zaskha.storyapepe.helper

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.*
import android.graphics.Bitmap.CompressFormat.*
import android.graphics.BitmapFactory.*
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment.*
import android.widget.Toast
import com.zaskha.storyapepe.R.string.*
import java.io.*
import java.io.File.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val stamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun Context.temporaryFile(): File {
    return createTempFile(
        stamp,
        ".jpg",
        getExternalFilesDir(DIRECTORY_PICTURES)
    )
}

fun Bitmap.bitmap(rearCamera: Boolean = false): Bitmap {
    val m = Matrix()
    return when {
        rearCamera -> {
            m.run { postRotate(0f) }
            createBitmap(this, 0, 0, width, height, m, true)
        }
        else -> {
            m.postScale(-1f, 1f, width / 2f, height / 2f)
            m.postRotate(0f)
            createBitmap(this, 0, 0, width, height, m, true)
        }
    }
}

fun File.minimumImage(): File {
    val bm = decodeFile(this.path)
    var minimizeFile = 100
    var flowLength: Int
    do {
        val baos = ByteArrayOutputStream()
        bm.run {
            compress(JPEG, minimizeFile, baos)
        }
        flowLength = baos.toByteArray().size
        minimizeFile -= 5
    } while (flowLength > 1000000)
    bm.compress(JPEG, minimizeFile, FileOutputStream(this))
    return this
}

fun Application.file(): File {
    val directory = externalMediaDirs.firstOrNull()?.run {
        File(this,
            resources.getString(app_name)).apply(File::mkdirs)
    }

    return File(
        when {
            directory == null || !directory.exists() -> {
                filesDir
            }
            else -> {
                directory
            }
        },
        "$stamp.jpg"
    )
}

fun Uri.fileUri(context: Context): File {
    val file = context.temporaryFile()
    val outFlow = FileOutputStream(file)
    val inFlow = context.contentResolver.openInputStream(this) as InputStream
    val byteSize = ByteArray(size = 1024)
    var length: Int
    while (inFlow.read(byteSize).apply {
            length = this
        } > 0) {
        outFlow.write(byteSize, 0, length)
    }
    outFlow.close()
    inFlow.close()

    return file
}

fun Context.say(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

