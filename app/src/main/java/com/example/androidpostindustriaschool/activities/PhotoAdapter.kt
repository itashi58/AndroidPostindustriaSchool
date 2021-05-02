package com.example.androidpostindustriaschool.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidpostindustriaschool.R
import org.bluecabin.textoo.Textoo


class PhotoAdapter(private val urls: ArrayList<String>) :
        RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PhotoAdapter.PhotoViewHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoViewHolder, position: Int) {
        Glide.with(holder.photoWebView.context).load(urls[position])
                .into(holder.photoWebView)
        holder.linkTextView.text = urls[position]

        //make links with intents leading to webViewActivity in every CardView
        holder.linkTextView = Textoo
                .config(holder.linkTextView)
                .linkifyAll()
                .addLinksHandler { view, url ->
                    val intent = Intent(holder.linkTextView.context, WebViewActivity::class.java).apply {
                        data = Uri.parse(url)
                    }
                    startActivity(holder.linkTextView.context, intent, Bundle())
                    true
                }
                .apply()

    }

    override fun getItemCount() = urls.size

    fun deleteItem(position: Int){
        urls.removeAt(position)
        notifyItemRemoved(position)
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linkTextView: TextView = itemView.findViewById(R.id.photoLinkText)
        var photoWebView: ImageView = itemView.findViewById(R.id.photoWebView)

    }


}