package com.example.androidpostindustriaschool.ui.activities.history.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R


class HistoryRequestAdapter() :
    RecyclerView.Adapter<HistoryRequestAdapter.RequestViewHolder>() {

    private val _deleteHistory: MutableLiveData<Boolean> = MutableLiveData()
    val deleteHistory: LiveData<Boolean>
        get() = _deleteHistory

    private var recyclerData = ArrayList<String>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_request_history, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.request.text = recyclerData[position]
    }

    override fun getItemCount() = recyclerData.size

    fun updateList(history: ArrayList<String>) {
        recyclerData.clear()
        recyclerData.addAll(history)
        notifyDataSetChanged()
    }

    fun deleteAll() {
        recyclerData.clear()
        _deleteHistory.postValue(true)
        notifyDataSetChanged()
    }


    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request: TextView = itemView.findViewById(R.id.tv_request)
    }

}