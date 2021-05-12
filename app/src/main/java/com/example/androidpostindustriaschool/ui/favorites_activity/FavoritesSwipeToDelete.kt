package com.example.androidpostindustriaschool.ui.favorites_activity

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class FavoritesSwipeToDelete(
    // TODO: 5/12/21 do we need to pass an adapter here? Can we access adapter from onSwiped params?
    private var adapter: FavoritesPhotoAdapter,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        adapter.deleteItem(position)
    }
}