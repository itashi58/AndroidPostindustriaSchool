package com.example.androidpostindustriaschool.ui.activities.main.view

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.ui.activities.photo_review.view.PhotoReviewActivity
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bluecabin.textoo.Textoo
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


class MainPhotoAdapter : RecyclerView.Adapter<MainPhotoAdapter.PhotoViewHolder>() {

    private val _deletePhoto: MutableLiveData<String> = MutableLiveData()
    val deletePhoto: LiveData<String>
        get() = _deletePhoto

    private var urls = ArrayList<String>()
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
        private var linkTextView: TextView = itemView.findViewById(R.id.tv_photo_link)
        private var photoImageView: ImageView = itemView.findViewById(R.id.iv_photo)
        private val holderContext: Context = linkTextView.context

        fun onBind(url: String, request: String) {
            Glide.with(holderContext).load(url)
                .into(photoImageView)
            linkTextView.text = url

            //make links with intents leading to PhotoReviewActivity in every CardView
            linkTextView = Textoo
                .config(linkTextView)
                .linkifyAll()
                .addLinksHandler { _, _ ->
                    val intent =
                        Intent(holderContext, PhotoReviewActivity::class.java).apply {
                            data = Uri.parse(url)
                            this.putExtra(REQUEST_EXTRA, request)
                        }
                    startActivity(holderContext, intent, Bundle())
                    true
                }
                .apply()

            photoImageView.setOnClickListener {
                val intent = Intent(holderContext, PhotoReviewActivity::class.java).apply {
                    data = Uri.parse(linkTextView.text.toString())
                    this.putExtra(REQUEST_EXTRA, url)
                }
                startActivity(holderContext, intent, Bundle())
            }
        }
    }
}