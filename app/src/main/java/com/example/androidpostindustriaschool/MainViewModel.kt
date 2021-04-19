package com.example.androidpostindustriaschool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository):ViewModel(){

    val myResponse: MutableLiveData<Data> = MutableLiveData()

    fun getPost(search:String) {
        viewModelScope.launch {
            val response = repository.getAPIService(search)
            myResponse.value = response
        }

    }
}