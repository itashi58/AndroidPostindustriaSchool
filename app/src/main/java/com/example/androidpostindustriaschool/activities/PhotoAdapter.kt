package com.example.androidpostindustriaschool.activities

import android.content.Intent
import android.net.Uri
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
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
        holder.photoWebView.loadUrl(urls[position])
        holder.linkTextView.text = urls[position]
        Linkify.addLinks(holder.linkTextView, Linkify.WEB_URLS)
    }

    override fun getItemCount() = urls.size

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linkTextView: TextView = itemView.findViewById(R.id.photoLinkText)
        var photoWebView: WebView = itemView.findViewById(R.id.photoWebView)

    }

//    //in apiResponseTextView make links with intents leading to webViewActivity
//    private fun linkify() {
//        apiResponseTextView = Textoo
//                .config(apiResponseTextView)
//                .linkifyAll()
//                .addLinksHandler { view, url ->
//                    val intent = Intent(this., WebViewActivity::class.java).apply {
//                        data = Uri.parse(url)
//                    }
//                    startActivity(intent)
//                    true
//                }
//                .apply()
//    }

}