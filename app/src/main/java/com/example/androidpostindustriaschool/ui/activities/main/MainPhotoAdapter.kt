package com.example.androidpostindustriaschool.ui.activities.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.model.RequestHistory
import com.example.androidpostindustriaschool.ui.activities.photoReview.PhotoReviewActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA
import org.bluecabin.textoo.Textoo


class MainPhotoAdapter : RecyclerView.Adapter<MainPhotoAdapter.PhotoViewHolder>() {

    val deletePhoto: MutableLiveData<String> = MutableLiveData()
    var urls = ArrayList<String>()
    var request: String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        Glide.with(holder.photoImageView.context).load(urls[position])
            .into(holder.photoImageView)
        holder.linkTextView.text = urls[position]

        //make links with intents leading to PhotoReviewActivity in every CardView
        holder.linkTextView = Textoo
            .config(holder.linkTextView)
            .linkifyAll()
            .addLinksHandler { _, url ->
                val intent = Intent(holder.linkTextView.context, PhotoReviewActivity::class.java).apply {
                    data = Uri.parse(url)
                    this.putExtra(REQUEST_EXTRA, request)
                }
                startActivity(holder.linkTextView.context, intent, Bundle())
                true
            }
            .apply()

        holder.photoImageView.setOnClickListener {
            val intent = Intent(holder.linkTextView.context, PhotoReviewActivity::class.java).apply {
                data = Uri.parse(holder.linkTextView.text.toString())
                this.putExtra(REQUEST_EXTRA, request)
            }
            startActivity(holder.linkTextView.context, intent, Bundle())
        }

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
        deletePhoto.postValue(urls[position])
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linkTextView: TextView = itemView.findViewById(R.id.photoLinkText)
        var photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)

    }

}