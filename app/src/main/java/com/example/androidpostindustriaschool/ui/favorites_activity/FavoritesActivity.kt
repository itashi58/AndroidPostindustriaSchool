// TODO: 5/12/21 don't use underscore in package names
//  You can remove _activity postfix and add nested view package which will contain activity, adapters, etc.
package com.example.androidpostindustriaschool.ui.favorites_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.FavoritesRepository

class FavoritesActivity : AppCompatActivity() {
    // TODO: 5/12/21 Keep onCreate as simple as possible. Split logic into separate methods. Check this everywhere.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val favoriteRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistory)

        val repository = FavoritesRepository(DatabaseSQLite.getDatabase(this).chosenPhotoDao())
        val viewModelFactory = FavoritesViewModelFactory(repository)
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoritesViewModel::class.java)

        val adapter = FavoritesPhotoAdapter(viewModel)
        favoriteRecyclerView.adapter = adapter
        favoriteRecyclerView.layoutManager =
            GridLayoutManager(this, 1)

        val itemTouchHelper = ItemTouchHelper(FavoritesSwipeToDelete(adapter))
        itemTouchHelper.attachToRecyclerView(favoriteRecyclerView)

        viewModel.getFavoritesPhoto()

        // TODO: 5/12/21 Separate error handling from response handling
        viewModel.favoritePhotos.observe(this, { favoritesMap ->
            if (favoritesMap.isEmpty()) {
                val toast = Toast.makeText(
                    this,
                    getString(R.string.title_no_favorite_photos),
                    Toast.LENGTH_LONG
                )
                toast.show()
            } else {
                adapter.updateList(favoritesMap)
            }
        })


    }
}