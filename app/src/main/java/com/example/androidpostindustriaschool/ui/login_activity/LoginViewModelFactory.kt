package com.example.androidpostindustriaschool.ui.login_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpostindustriaschool.data.repository.LoginRepository


class LoginViewModelFactory(private val loginRepository: LoginRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(loginRepository) as T
        }
}