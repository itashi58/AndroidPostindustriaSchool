package com.example.androidpostindustriaschool.data.repository


import com.example.androidpostindustriaschool.data.database.daos.RequestHistoryDao
import com.example.androidpostindustriaschool.data.database.model.RequestHistory

class HistoryRepository(private val historyDao: RequestHistoryDao) {


    suspend fun deleteHistory() {
        historyDao.deleteAll()
    }

    suspend fun getAllHistory(): Array<RequestHistory> {
        return historyDao.getAllRequests()
    }
}