package com.example.androidpostindustriaschool.ui.activities.gallery.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.example.androidpostindustriaschool.util.Constants.Companion.SAVED_PHOTOS_QUALITY
import com.example.androidpostindustriaschool.util.Constants.Companion.SAVED_PHOTO_DIRECTORY_NAME
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class GalleryActivity : AppCompatActivity() {

    private lateinit var createPhotoBtn: ImageButton
    private lateinit var currentPhotoPath: String
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var adapter: GalleryPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        createPhotoBtn = findViewById(R.id.fab_create_photo)
        photoRecyclerView = findViewById(R.id.rv_gallery_photos)


        initRecyclerView()
        setListeners()
        setObservers()

        getPhotosFromMemory()
    }


    private fun setListeners() {
        createPhotoBtn.setOnClickListener { dispatchTakePictureIntent() }
    }

    private fun setObservers() {
        adapter.deletePhoto.observe(this, { uri ->
            deleteFile(uri)
        })
    }


    private fun initRecyclerView() {
        adapter = GalleryPhotoAdapter()
        photoRecyclerView.adapter = adapter
        photoRecyclerView.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.span_count))

        val itemTouchHelper = ItemTouchHelper(GallerySwipeToDelete())
        itemTouchHelper.attachToRecyclerView(photoRecyclerView)
    }

    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            val toast = Toast.makeText(
                this,
                getString(R.string.error_creating_photo),
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageFile = createImageFile()
            currentPhotoPath = saveToInternalStorage(imageBitmap, imageFile)
            createDialog()
            getPhotosFromMemory()
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            UCrop.getOutput(data)
            getPhotosFromMemory()
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            UCrop.getError(data)
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap, file: File): String {

        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, SAVED_PHOTOS_QUALITY, fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file.absolutePath
    }

    private fun createDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialogBuilder.setView(R.layout.dialog_ucrop)

        alertDialogBuilder.setCancelable(true).setPositiveButton(
            "OK"
        ) { _, _ ->
            val url = Uri.fromFile(File(currentPhotoPath))
            UCrop.of(url, url)
                .start(this)
        }

        alertDialogBuilder.setCancelable(true).setNegativeButton(
            "No,Thanks"
        ) { _, _ -> }


        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = Calendar.getInstance().time.toString()
        val imageFile =
            File(this.getDir(SAVED_PHOTO_DIRECTORY_NAME, MODE_PRIVATE), "JPEG_$timeStamp.jpg")
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    private fun getPhotosFromMemory() {
        val uris = ArrayList<Uri>()

        //getting photo downloaded from Camera
        val files = this.getDir(SAVED_PHOTO_DIRECTORY_NAME, Context.MODE_PRIVATE).list()

        files?.forEach { fileName ->
            uris.add(
                (this.getDir(
                    SAVED_PHOTO_DIRECTORY_NAME,
                    Context.MODE_PRIVATE
                ).path + File.separator + fileName).toUri()
            )
            uris.reverse()
        }

        //getting photo downloaded from Flickr
        val pathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (pathFile != null && pathFile.exists()) {
            val fileNames = pathFile.list()
            if (fileNames != null && fileNames.isNotEmpty()) {
                for (i in fileNames.indices) {
                    val photoPass = (pathFile.path + File.separator + fileNames[i]).toUri()
                    uris.add(photoPass)
                }
            }
        }
        adapter.updateList(uris)
    }


    private fun deleteFile(uri: Uri) {
        val fileToDelete = File(uri.toString())
        if (fileToDelete.exists()) {
            fileToDelete.delete()
        }
    }
}
