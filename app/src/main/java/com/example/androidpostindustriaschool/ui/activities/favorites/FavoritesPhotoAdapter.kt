package com.example.androidpostindustriaschool.ui.activities.favorites

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.model.FavoritePhoto
import com.example.androidpostindustriaschool.util.Constants.Companion.VIEW_TYPE_PHOTO
import com.example.androidpostindustriaschool.util.Constants.Companion.VIEW_TYPE_REQUEST_CATEGORY


class FavoritesPhotoAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val photoDelete: MutableLiveData<String> = MutableLiveData()
    private var recyclerData = ArrayList<RecyclerItemData>()


    override fun getItemViewType(position: Int): Int {
        return if (recyclerData[position].isPhoto) {
            VIEW_TYPE_PHOTO
        } else {
            VIEW_TYPE_REQUEST_CATEGORY
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PHOTO) {
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
        if (this.getItemViewType(position) == VIEW_TYPE_PHOTO) {
            holder as PhotoViewHolder
            val photo = recyclerData[position].favoritePhoto
            Glide.with(holder.photoImageView.context).load(photo.url)
                .into(holder.photoImageView)
            holder.linkTextView.text = photo.url
            Linkify.addLinks(holder.linkTextView, Linkify.WEB_URLS)

            holder.favoritesDeleteBtn.setOnClickListener {
                deleteItem(position)
            }

        }
        if (this.getItemViewType(position) == VIEW_TYPE_REQUEST_CATEGORY) {
            holder as RequestViewHolder
            holder.request.text = recyclerData[position].request
        }

    }

    override fun getItemCount() = recyclerData.size

    fun updateList(list: Map<String, List<FavoritePhoto>>) {
        val newData = ArrayList<RecyclerItemData>()
        list.forEach { entry ->
            newData.add(RecyclerItemData(entry.key))
            entry.value.forEach { photo ->
                newData.add(RecyclerItemData(photo))
            }
        }
        recyclerData.clear()
        recyclerData.addAll(newData)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        if (recyclerData[position].isPhoto) {
            val photo = recyclerData[position].favoritePhoto
            recyclerData.removeAt(position)
            photoDelete.postValue(photo.id)
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

    class RecyclerItemData(val request: String) {
        lateinit var favoritePhoto: FavoritePhoto
        var isPhoto = false

        constructor(favoritePhoto: FavoritePhoto) : this(favoritePhoto.request) {
            this.favoritePhoto = favoritePhoto
            isPhoto = true
        }

    }
}