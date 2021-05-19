package com.example.androidpostindustriaschool.ui.activities.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpostindustriaschool.R
import com.example.androidpostindustriaschool.data.database.DatabaseSQLite
import com.example.androidpostindustriaschool.data.repository.HistoryRepository


class HistoryActivity : AppCompatActivity() {

    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var clearHistoryBtn: Button
    private lateinit var adapter: HistoryRequestAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        clearHistoryBtn = findViewById(R.id.clearHistoryBtn)

        val repository = HistoryRepository(DatabaseSQLite.getDatabase(this).requestHistoryDao())
        val viewModelFactory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        initRecyclerView()

        viewModel.getHistory()

        setObservers()
        setListeners()

    }

    private fun initRecyclerView() {
        requestRecyclerView = findViewById(R.id.recyclerViewHistory)
        adapter = HistoryRequestAdapter()
        requestRecyclerView.adapter = adapter
        requestRecyclerView.layoutManager =
            GridLayoutManager(this, resources.getInteger(R.integer.span_count))
    }

    private fun setObservers() {
        viewModel.requestHistory.observe(this, { history ->
            if (history.isEmpty()) {
                val toast = Toast.makeText(
                    this,
                    getString(R.string.title_no_history),
                    Toast.LENGTH_LONG
                )
                toast.show()
            } else {
                adapter.updateList(history)
            }
        })

        adapter.deleteHistory.observe(this, {
            viewModel.deleteAllHistory()
        })
    }

    private fun setListeners() {
        clearHistoryBtn.setOnClickListener {
            adapter.deleteAll()
        }
    }
}