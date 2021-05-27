package com.example.androidpostindustriaschool.ui.activities.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.ui.activities.photo_review.view.PhotoReviewActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA
import org.bluecabin.textoo.Textoo


class MainPhotoAdapter : RecyclerView.Adapter<MainPhotoAdapter.PhotoViewHolder>() {

    private val _deletePhoto: MutableLiveData<String> = MutableLiveData()
    val deletePhoto: LiveData<String>
        get() = _deletePhoto

    var urls = ArrayList<String>()
    var request: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.onBind(urls[position], request)
    }

    override fun getItemCount() = urls.size

    fun updateList(list: List<String>, request: String) {
        urls.clear()
        urls.addAll(list)
        this.request = request
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        urls.removeAt(position)
        notifyItemRemoved(position)
        _deletePhoto.postValue(urls[position])
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linkTextView: TextView = itemView.findViewById(R.id.tv_photo_link)
        var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)

        fun onBind(url: String, request: String){
            Glide.with(photoImageView.context).load(url)
                .into(photoImageView)
            linkTextView.text = url

            //make links with intents leading to PhotoReviewActivity in every CardView
            linkTextView = Textoo
                .config(linkTextView)
                .linkifyAll()
                .addLinksHandler { _, url ->
                    val intent = Intent(linkTextView.context, PhotoReviewActivity::class.java).apply {
                        data = Uri.parse(url)
                        this.putExtra(REQUEST_EXTRA, request)
                    }
                    startActivity(linkTextView.context, intent, Bundle())
                    true
                }
                .apply()

            photoImageView.setOnClickListener {
                val intent = Intent(linkTextView.context, PhotoReviewActivity::class.java).apply {
                    data = Uri.parse(linkTextView.text.toString())
                    this.putExtra(REQUEST_EXTRA, url)
                }
                startActivity(linkTextView.context, intent, Bundle())
            }
        }
    }

}