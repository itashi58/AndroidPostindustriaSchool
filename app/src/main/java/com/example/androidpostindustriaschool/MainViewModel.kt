package com.example.androidpostindustriaschool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.model.PhotoResponse
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    // TODO: 4/22/21  Instead of storing a complete response in VM and converting it in activity to something useful -
    //  you can convert it into presentation model in repository
    val myResponse: MutableLiveData<PhotoResponse> = MutableLiveData()

    fun getPost(search: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAPIService(search)
            myResponse.postValue(response)
        }

    }
}