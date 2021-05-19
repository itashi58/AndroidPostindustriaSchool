package com.example.androidpostindustriaschool.ui.activities.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpostindustriaschool.data.repository.LoginRepository
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_LOG
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_LOG_AND_PASS
import com.example.androidpostindustriaschool.util.Constants.Companion.INCORRECT_PASS
import com.example.androidpostindustriaschool.util.Constants.Companion.LOGIN_SUCCESSFUL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val loginResponseCode: MutableLiveData<Int> = MutableLiveData()

    fun insertUserInfo(login: String, password: String) {
        if (login.isNotEmpty() && password.length >= 6) {
            viewModelScope.launch(Dispatchers.IO) {
                loginRepository.insertInUsers(login, password)
                loginResponseCode.postValue(LOGIN_SUCCESSFUL)
            }
        } else if (login.isEmpty() && password.length < 6) {
            loginResponseCode.postValue(INCORRECT_LOG_AND_PASS)
        } else if (login.isEmpty()) {
            loginResponseCode.postValue(INCORRECT_LOG)
        } else {
            loginResponseCode.postValue(INCORRECT_PASS)
        }
    }

}