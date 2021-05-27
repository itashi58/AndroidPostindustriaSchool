package com.example.androidpostindustriaschool.ui.activities.login.view_model

import androidx.lifecycle.LiveData
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

    private val _loginResponseCode: MutableLiveData<Int> = MutableLiveData()
    val loginResponseCode: LiveData<Int>
        get() = _loginResponseCode

    fun insertUserInfo(login: String, password: String) {
        if (login.isNotEmpty() && password.length >= 6) {
            viewModelScope.launch(Dispatchers.IO) {
                loginRepository.insertInUsers(login, password)
                _loginResponseCode.postValue(LOGIN_SUCCESSFUL)
            }
        } else if (login.isEmpty() && password.length < 6) {
            _loginResponseCode.postValue(INCORRECT_LOG_AND_PASS)
        } else if (login.isEmpty()) {
            _loginResponseCode.postValue(INCORRECT_LOG)
        } else {
            _loginResponseCode.postValue(INCORRECT_PASS)
        }
    }

}