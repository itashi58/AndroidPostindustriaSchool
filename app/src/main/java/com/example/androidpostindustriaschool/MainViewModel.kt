package com.example.androidpostindustriaschool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.dataClases.Data
import com.example.androidpostindustriaschool.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: 4/22/21 Pay attention to code formatting (cmd + option + L on mac, cntrl + alt + L on win (if I'm not mistaking))
class MainViewModel(private val repository: Repository):ViewModel(){

    // TODO: 4/22/21  Instead of storing a complete response in VM and converting it in activity to something useful -
    //  you can convert it into presentation model in repository
    val myResponse: MutableLiveData<Data> = MutableLiveData()

    fun getPost(search:String) {

        // TODO: 4/22/21 You don't specify the context (IO in your case) and that's why you are blocking the main thread.
        //  viewModelScope.launch(Dispatchers.IO)
        viewModelScope.launch {
            val response = repository.getAPIService(search)
            myResponse.postValue(response)
        }

    }
}