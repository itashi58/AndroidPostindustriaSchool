package com.example.androidpostindustriaschool.ui.activities.gallery.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GalleryViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GalleryViewModel() as T

    }
}