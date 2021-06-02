package com.example.androidpostindustriaschool.ui.activities.photo_review.view

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.ui.activities.photo_review.view_model.PhotoReviewModelFactory
import com.example.androidpostindustriaschool.ui.activities.photo_review.view_model.PhotoReviewViewModel
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI


class PhotoReviewActivity : AppCompatActivity() {
    private lateinit var photoView: ImageView
    private lateinit var urlTextView: TextView
    private lateinit var addToFavorites: ImageButton
    private lateinit var viewModel: PhotoReviewViewModel
    private lateinit var downloadImageButton: ImageButton
    private lateinit var image: Bitmap
    private lateinit var url: String
    private lateinit var imageSaveUri: URI
    private var isPhotoFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_review)

        initViews()

        val repository = FavoritePhotosRepository(DatabaseSQLite.getDatabase(this).chosenPhotoDao())
        val viewModelFactory = PhotoReviewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PhotoReviewViewModel::class.java)

        imageSaveUri = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toURI()!!
        url = intent.data.toString()

        val request = intent.extras?.getString(REQUEST_EXTRA) ?: ""
        if (intent.data != null) {
            Glide.with(this).load(url)
                .into(photoView)
            urlTextView.text = request
        }

        viewModel.idInChosenPhoto(url + request)

        setObservers()
        setListeners(request)
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

        viewModel.photoLoaded.observe(this, { isPhotoLoaded ->
            if (isPhotoLoaded) {
                createToast(R.string.msg_photo_loaded)
            } else {
                createToast(R.string.error_while_loading)
            }
        })
    }

    private fun setListeners(request: String) {
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
                    image = Glide.with(this@PhotoReviewActivity)
                        .asBitmap()
                        .load(url)
                        .submit()
                        .get()
                    viewModel.saveImage(url, imageSaveUri, image)
                }
        }
    }

    private fun initViews() {
        photoView = findViewById(R.id.photoView)
        urlTextView = findViewById(R.id.urlTextView)
        addToFavorites = findViewById(R.id.btn_favorites)
        downloadImageButton = findViewById(R.id.ib_download)
    }


    private fun createToast(messageResId: Int) {
        val toast = Toast.makeText(
            this,
            getString(messageResId),
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

}