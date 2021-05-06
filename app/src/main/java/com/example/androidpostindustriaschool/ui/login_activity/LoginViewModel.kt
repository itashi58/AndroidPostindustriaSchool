package com.example.androidpostindustriaschool.ui.login_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun insertUserInfo(login:String, password:String){
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.insertInUsers(login, password)
        }
    }

}