package com.example.androidpostindustriaschool.ui.activities.photo_review.view

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.ui.activities.photo_review.view_model.PhotoReviewModelFactory
import com.example.androidpostindustriaschool.ui.activities.photo_review.view_model.PhotoReviewViewModel
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTERNAL_STORAGE
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA
import com.example.androidpostindustriaschool.util.Constants.Companion.SAVED_PHOTO_DIRECTORY_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

class PhotoReviewActivity : AppCompatActivity() {
    private lateinit var photoView: ImageView
    private lateinit var urlTextView: TextView
    private lateinit var addToFavorites: ImageButton
    private lateinit var viewModel: PhotoReviewViewModel
    private lateinit var downloadImageButton: ImageButton
    private lateinit var image: Bitmap
    private lateinit var url: String
    private lateinit var context: Context
    private var isPhotoFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_review)

        initViews()

        val repository = FavoritePhotosRepository(DatabaseSQLite.getDatabase(this).chosenPhotoDao())
        val viewModelFactory = PhotoReviewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PhotoReviewViewModel::class.java)


        context = this
        url = intent.data.toString()
        val request = intent.extras?.getString(REQUEST_EXTRA) ?: ""
        if (intent.data != null) {
            Glide.with(this).load(url)
                .into(photoView)
            urlTextView.text = request
        }

        viewModel.idInChosenPhoto(url + request)

        setObservers()
        setListeners(url, request)
    }

    private fun setObservers() {
        viewModel.inFavorites.observe(this, { isFavorite ->
            isPhotoFavorite = isFavorite
            if (isPhotoFavorite) {
                addToFavorites.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.button_background_yellow,
                    null
                )
            } else {
                addToFavorites.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_background_white, null)
            }
        })
    }

    private fun setListeners(url: String, request: String) {
        addToFavorites.setOnClickListener {
            if (isPhotoFavorite) {
                addToFavorites.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_background_white, null)
                viewModel.deleteFromChosenPhoto(url, request)
                isPhotoFavorite = false
            } else {
                addToFavorites.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.button_background_yellow,
                    null
                )
                viewModel.insertInChosenPhoto(url, request)
                isPhotoFavorite = true
            }
        }

        downloadImageButton.setOnClickListener {
            createToast(R.string.msg_downloading_photo)

            lifecycleScope.launch(Dispatchers.IO) {
                image = Glide.with(context)
                    .asBitmap()
                    .load(url) // sample image
                    .submit()
                    .get()
                saveImage()
            }
        }
    }

    private fun initViews() {
        photoView = findViewById(R.id.photoView)
        urlTextView = findViewById(R.id.urlTextView)
        addToFavorites = findViewById(R.id.btn_favorites)
        downloadImageButton = findViewById(R.id.ib_download)
    }

    private fun saveImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageSdkQ()
        } else {
            val permission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                val permissionsStorage = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

                ActivityCompat.requestPermissions(
                    this,
                    permissionsStorage,
                    REQUEST_EXTERNAL_STORAGE
                )
            } else {
                saveImageSdkLessQ()
            }
        }
    }

    // for devices API level < 29
    private fun saveImageSdkLessQ() {
        val urlWithoutSlash = url.replace('/', '|') //because '/' is counted as new directory
        val root =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()
        val photosDir = File(root + File.separator + SAVED_PHOTO_DIRECTORY_NAME)
        photosDir.mkdirs()
        val fileName = "Image-$urlWithoutSlash.jpg"
        val imageFile = File(photosDir, fileName)
        if (imageFile.exists()) imageFile.delete()
        imageFile.createNewFile()
        try {
            val out = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // for devices API level >= 29
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageSdkQ() {
        val urlWithoutSlash = url.replace('/', '|') //because '/' is counted as new directory
        val outputStream: OutputStream?
        val contentResolver: ContentResolver = this.contentResolver
        val contentValues = ContentValues()

        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, urlWithoutSlash)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + File.separator + SAVED_PHOTO_DIRECTORY_NAME
        )
        val imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            outputStream = contentResolver.openOutputStream(imageUri!!)!!
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    saveImageSdkLessQ()
                } else {
                    createToast(R.string.error_no_storage_permission)
                }
                return
            }
        }
    }

    private fun createToast(messageResId: Int) {
        val toast = Toast.makeText(
            this,
            getString(messageResId),
            Toast.LENGTH_LONG
        )
        toast.show()
    }

}