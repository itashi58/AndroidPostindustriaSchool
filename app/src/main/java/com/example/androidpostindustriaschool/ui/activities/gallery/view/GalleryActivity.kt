package com.example.androidpostindustriaschool.ui.activities.gallery.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.BuildConfig
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.util.Constants
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class GalleryActivity : AppCompatActivity() {

    private lateinit var createPhotoBtn: ImageButton
    private lateinit var currentPhotoPath: String
    private lateinit var photoURI: Uri
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var adapter: GalleryPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        createPhotoBtn = findViewById(R.id.fab_create_photo)
        photoRecyclerView = findViewById(R.id.rv_gallery_photos)

        val permissionStorage = checkForStoragePermission()

        initRecyclerView()
        setListeners()
        setObservers()

        getPhotosFromMemory()
    }


    private fun checkForStoragePermission(): Boolean {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            } else {
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

        if (permission != PackageManager.PERMISSION_GRANTED) {

            return true
        } else {
            return true
        }

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
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    val toast = Toast.makeText(
                        this,
                        getString(R.string.error_creating_photo),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        applicationContext,
                        BuildConfig.APPLICATION_ID + ".fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    Log.d("file was created", "OK")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            createDialog()
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            deleteFile(currentPhotoPath.toUri())
            getPhotosFromMemory()
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            UCrop.getOutput(data)
            getPhotosFromMemory()
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            UCrop.getError(data)
        }
    }

    private fun createDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialogBuilder.setView(R.layout.dialog_ucrop)
        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton(
            "OK"
        ) { _, _ ->
            val url = Uri.fromFile(File(currentPhotoPath))
            UCrop.of(url, url)
                .start(this)
        }

        alertDialogBuilder.setCancelable(true).setNegativeButton(
            "No,Thanks"
        ) { _, _ ->
            getPhotosFromMemory()
        }

        // create alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        // show it
        alertDialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = Calendar.getInstance().time.toString()
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            Log.d("photo_pass", currentPhotoPath.toUri().toString())
        }
    }

    private fun getPhotosFromMemory() {
        val uris = ArrayList<Uri>()

        //getting photo downloaded from Camera

//        val pathFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        if (pathFile != null && pathFile.exists()) {
//            val fileNames = pathFile.list()
//            if (fileNames != null && fileNames.isNotEmpty()) {
//                for (i in fileNames.indices) {
//                    val photoPass = (pathFile.path + "/" + fileNames[i]).toUri()
//                    uris.add(photoPass)
//                }
//                uris.forEach {
//                    Log.d("uri", it.toString())
//                }
//            }
//        }

        // getting photo downloaded from flickr
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            // Get the pictures directory that's inside the app-specific directory on
            // external storage.
            val file = File(this.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "flick")
            if (!file.mkdirs()) {
                Log.d( "dir created", "Directory not created")
            }
            Log.d( "dir created", file.path)

//            val file = File(FLICKER_SAVED_PICTURES_DIR)
//            if (file.exists()) {
//                file.listFiles()?.forEach {
//                    uris.add(it.toUri())
//                    Log.d("Files", "FileName:" + it.name)
//                }
//            }

        } else {
            val pathFileNotQ = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + Constants.SAVED_PHOTO_DIRECTORY_NAME
            )
            if (pathFileNotQ.exists()) {
                val fileNames = pathFileNotQ.list()
                if (fileNames != null && fileNames.isNotEmpty()) {
                    for (i in fileNames.indices) {
                        val photoPass = (pathFileNotQ.path + "/" + fileNames[i]).toUri()
                        uris.add(photoPass)
                    }
                    uris.forEach {
                        Log.d("uri image", it.toString())
                    }

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
