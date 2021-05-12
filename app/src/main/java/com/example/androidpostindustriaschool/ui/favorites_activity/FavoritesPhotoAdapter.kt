package com.example.androidpostindustriaschool.ui.favorites_activity

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto


// TODO: 5/12/21 Don't pass ViewModel into adapter. Pass a listener instead and handle action in activity / fragment
//  Check this in every adapter
class FavoritesPhotoAdapter(private val viewModel: FavoritesViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // TODO: 5/12/21 Omit using Any as it is not obvious what types this list can contain 
    private var recyclerData = ArrayList<Any>()


    // TODO: 5/12/21 avoid hardcode. Provide a suitable name for 1 and 0
    override fun getItemViewType(position: Int): Int {
        return if (recyclerData[position] is FavoritePhoto) {
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.photo_item_favorites, parent, false)
            PhotoViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.request_item_favorites, parent, false)
            RequestViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO: 5/12/21 move viewBinding into holder.bindView method
        if (this.getItemViewType(position) == 0) {
            holder as PhotoViewHolder
            val photo = recyclerData[position] as FavoritePhoto
            Glide.with(holder.photoImageView.context).load(photo.url)
                .into(holder.photoImageView)
            holder.linkTextView.text = photo.url
            Linkify.addLinks(holder.linkTextView, Linkify.WEB_URLS)

            holder.favoritesDeleteBtn.setOnClickListener {
                deleteItem(position)
            }

        }
        if (this.getItemViewType(position) == 1) {
            holder as RequestViewHolder
            holder.request.text = recyclerData[position].toString()
        }

    }

    override fun getItemCount() = recyclerData.size

    // TODO: 5/12/21 Pass a ready to display list. Don't do any list transformation in adapter
    fun updateList(list: Map<String, List<FavoritePhoto>>) {
        val newData = ArrayList<Any>()
        list.forEach { entry ->
            newData.add(entry.key)
            entry.value.forEach { photo ->
                newData.add(photo)
            }
        }
        recyclerData.clear()
        recyclerData.addAll(newData)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        if (recyclerData[position] is FavoritePhoto) {
            val photo = recyclerData[position] as FavoritePhoto
            recyclerData.removeAt(position)
            viewModel.deleteFromChosenPhoto(photo.id)
            notifyItemRemoved(position)
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linkTextView: TextView = itemView.findViewById(R.id.photoLinkText)
        var photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        var favoritesDeleteBtn: ImageButton = itemView.findViewById(R.id.favoritesDeleteBtn)
    }

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request: TextView = itemView.findViewById(R.id.requestLabel)
    }

}