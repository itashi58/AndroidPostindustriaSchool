package com.example.androidpostindustriaschool.ui.activities.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.androidpostindustriaschool.BuildConfig
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class GalleryActivity : AppCompatActivity() {

    private lateinit var createPhotoBtn: ImageButton
    private lateinit var currentPhotoPath: String
    private lateinit var photoURI: Uri
    private lateinit var galleryDir:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        createPhotoBtn = findViewById(R.id.fab_create_photo)

        galleryDir = this.getDir("Gallery", Context.MODE_PRIVATE)

        createPhotoBtn.setOnClickListener { dispatchTakePictureIntent() }
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
                    photoURI = FileProvider.getUriForFile(Objects.requireNonNull(applicationContext),
                        BuildConfig.APPLICATION_ID + ".fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    Log.d("file was created", "OK")
                }
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
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data!=null) {
            val resultUri = UCrop.getOutput(data)
        } else if (resultCode == UCrop.RESULT_ERROR && data!=null) {
            val cropError = UCrop.getError(data)
        }
    }
    private fun createDialog(){
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialogBuilder.setView(R.layout.dialog_ucrop)

        // set dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK"
        ) { dialog, id ->
            val url = Uri.fromFile(File(currentPhotoPath))
            UCrop.of(url, url)
                .start(this);
        }

        alertDialogBuilder.setCancelable(true).setNegativeButton("No,Thanks"
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

}