package com.example.androidpostindustriaschool.ui.main_activity

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MainSwipeToDelete(
    private var adapter: MainPhotoAdapter,
    // TODO: 5/12/21 Don't pass viewModel here
    private val viewModel: MainViewModel
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
        viewModel.deleteId(adapter.urls[position] + viewModel.lastRequest)
        adapter.deleteItem(position)
    }
}