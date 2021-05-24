package com.example.androidpostindustriaschool.ui.activities.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.model.RequestHistory


class HistoryRequestAdapter() :
    RecyclerView.Adapter<HistoryRequestAdapter.RequestViewHolder>() {

    val deleteHistory: MutableLiveData<Boolean> = MutableLiveData()
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

    // TODO: 24.05.2021 data manipulation in adapter
    fun updateList(list: Array<RequestHistory>) {
        val newData = ArrayList<String>()
        list.forEach { entry ->
            newData.add(entry.request)
        }
        recyclerData.clear()
        recyclerData.addAll(newData)
        notifyDataSetChanged()
    }

    fun deleteAll() {
        recyclerData.clear()
        deleteHistory.postValue(true)
        notifyDataSetChanged()
    }


    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request: TextView = itemView.findViewById(R.id.tv_request)
    }

}