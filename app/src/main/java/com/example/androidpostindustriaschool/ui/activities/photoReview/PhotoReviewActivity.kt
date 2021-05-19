package com.example.androidpostindustriaschool.ui.activities.photoReview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA

class PhotoReviewActivity : AppCompatActivity() {
    private lateinit var photoView: ImageView
    private lateinit var urlTextView: TextView
    private lateinit var addToFavorites: ImageButton
    private lateinit var viewModel: PhotoReviewModel
    var isPhotoFavorite = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)

        initViews()

        val repository = FavoritePhotosRepository(DatabaseSQLite.getDatabase(this).chosenPhotoDao())
        val viewModelFactory = PhotoReviewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PhotoReviewModel::class.java)


        val url = intent.data.toString()
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

    private fun setObservers(){
        viewModel.inFavorites.observe(this, { isFavorite ->
            isPhotoFavorite = isFavorite
            if (isPhotoFavorite) {
                addToFavorites.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.circle_background_yellow,
                    null
                )
            } else {
                addToFavorites.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.circle_background_white, null)
            }
        })
    }

    private fun setListeners(url:String, request:String){
        addToFavorites.setOnClickListener {
            if (isPhotoFavorite) {
                addToFavorites.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.circle_background_white, null)
                viewModel.deleteFromChosenPhoto(url, request)
                isPhotoFavorite = false
            } else {
                addToFavorites.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.circle_background_yellow,
                    null
                )
                viewModel.insertInChosenPhoto(url, request)
                isPhotoFavorite = true
            }
        }
    }

    private fun initViews(){
        photoView = findViewById(R.id.photoView)
        urlTextView = findViewById(R.id.urlTextView)
        addToFavorites = findViewById(R.id.favoritesDeleteBtn)
    }
}