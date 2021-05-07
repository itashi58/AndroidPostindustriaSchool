package com.example.androidpostindustriaschool.ui.history_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.model.RequestHistory


class HistoryRequestAdapter(private val viewModel: HistoryViewModel) :
    RecyclerView.Adapter<HistoryRequestAdapter.RequestViewHolder>() {

    private var recyclerData = ArrayList<String>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.request_item_history, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.request.text = recyclerData[position]
    }

    override fun getItemCount() = recyclerData.size

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
        viewModel.deleteAllHistory()
        notifyDataSetChanged()
    }


    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var request: TextView = itemView.findViewById(R.id.requestLabel)
    }

}