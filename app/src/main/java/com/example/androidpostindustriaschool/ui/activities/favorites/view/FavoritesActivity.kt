package com.example.androidpostindustriaschool.ui.activities.favorites.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.FavoritePhotosRepository
import com.example.androidpostindustriaschool.ui.activities.favorites.view_model.FavoritesViewModel
import com.example.androidpostindustriaschool.ui.activities.favorites.view_model.FavoritesViewModelFactory

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var adapter: FavoritesPhotoAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val repository = FavoritePhotosRepository(DatabaseSQLite.getDatabase(this).chosenPhotoDao())
        val viewModelFactory = FavoritesViewModelFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel::class.java)

        initRecyclerView()

        viewModel.getFavoritesPhoto()

        setObservers()

    }

    private fun initRecyclerView() {
        favoriteRecyclerView = findViewById(R.id.rv_history)
        adapter = FavoritesPhotoAdapter { ItemToDeletePosition: Int ->
            adapter.deleteItem(ItemToDeletePosition)
        }
        favoriteRecyclerView.adapter = adapter
        favoriteRecyclerView.layoutManager =
            GridLayoutManager(this, 1)

        itemTouchHelper = ItemTouchHelper(FavoritesSwipeToDelete())
        itemTouchHelper.attachToRecyclerView(favoriteRecyclerView)
    }

    private fun setObservers() {
        viewModel.favoritePhotos.observe(this, { favoritesMap ->
            adapter.updateList(favoritesMap)
        })

        adapter.photoDelete.observe(this, { deletedPhotoId ->
            viewModel.deleteFromFavoritePhotos(deletedPhotoId)
        })

        viewModel.noFavoritePhotos.observe(this, { isNoFavoritePhotos ->
            if (isNoFavoritePhotos) {
                val toast = Toast.makeText(
                    this,
                    getString(R.string.msg_no_favorite_photos),
                    Toast.LENGTH_LONG
                )
                toast.show()
            }
        })
    }
}