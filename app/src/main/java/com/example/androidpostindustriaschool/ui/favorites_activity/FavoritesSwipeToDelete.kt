package com.example.androidpostindustriaschool.ui.favorites_activity

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.data.database.model.ChosenPhoto
import com.example.androidpostindustriaschool.data.database.model.Photo
import com.example.androidpostindustriaschool.ui.main_activity.MainPhotoAdapter
import com.example.androidpostindustriaschool.ui.main_activity.MainViewModel

class FavoritesSwipeToDelete(private var adapter: FavoritesPhotoAdapter, private var viewModel: FavoritesViewModel) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is FavoritesPhotoAdapter.PhotoViewHolder) {
            val position = viewHolder.absoluteAdapterPosition
            val photo = adapter.recyclerData[position] as ChosenPhoto
            viewModel.deleteFromChosenPhoto(photo.id)
            adapter.deleteItem(position)
        }
    }
}