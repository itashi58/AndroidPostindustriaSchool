package com.example.androidpostindustriaschool.ui.activities.gallery

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class GalleryActivity : AppCompatActivity() {

    private lateinit var createPhotoBtn: ImageButton
    private lateinit var currentPhotoPath: String
    private lateinit var galleryDir:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        createPhotoBtn = findViewById(R.id.fab_create_photo)

        galleryDir = this.getDir("Gallery", Context.MODE_PRIVATE)

        createPhotoBtn.setOnClickListener { dispatchTakePictureIntent() }
        Log.d("file was created", "OK")
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    createToast(R.string.error_creating_photo)
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    Log.d("file was created", "OK")
                }
                Log.d("file was created", "OK")
            }
        }
    }

    private fun createToast(messageResId: Int) {
        val toast = Toast.makeText(
            this,
            getString(messageResId),
            Toast.LENGTH_LONG
        )
        toast.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val photo = File(currentPhotoPath)
            createDialog()
        }
    }
    private fun createDialog(){
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        val et = EditText(this)

        alertDialogBuilder.setView(et)

        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK"
        ) { dialog, id ->

        }


        // create alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        // show it
        alertDialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = galleryDir
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

}