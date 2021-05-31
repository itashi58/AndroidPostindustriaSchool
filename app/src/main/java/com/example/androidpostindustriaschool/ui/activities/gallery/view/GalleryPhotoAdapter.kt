package com.example.androidpostindustriaschool.ui.activities.gallery.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.androidpostindustriaschool.R


class GalleryPhotoAdapter : RecyclerView.Adapter<GalleryPhotoAdapter.PhotoViewHolder>() {

    private val _deletePhoto: MutableLiveData<Uri> = MutableLiveData()
    val deletePhoto: LiveData<Uri>
        get() = _deletePhoto

    var uris = ArrayList<Uri>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo_gallery, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.onBind(uris[position])
    }

    override fun getItemCount() = uris.size

    fun updateList(list: List<Uri>) {
        uris.clear()
        uris.addAll(list)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val deleteUri = uris[position]
        uris.removeAt(position)
        notifyItemRemoved(position)
        _deletePhoto.postValue(deleteUri)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photoImageView: ImageView = itemView.findViewById(R.id.iv_gallery_photo)

        fun onBind(uri: Uri){
            Glide.with(photoImageView.context).load(uri.toString())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(photoImageView)
        }
    }

}