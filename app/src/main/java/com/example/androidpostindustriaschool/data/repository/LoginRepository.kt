package com.example.androidpostindustriaschool.data.repository

import com.example.androidpostindustriaschool.data.database.daos.LoginDao


class LoginRepository(private val loginDao: LoginDao) {

    suspend fun insertInUsers(login: String, password: String) {
        loginDao.insert(login, password)
    }
}