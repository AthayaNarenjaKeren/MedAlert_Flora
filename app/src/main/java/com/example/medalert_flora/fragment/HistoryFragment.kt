package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.model.DummyData

// ListView dengan ArrayAdapter (simple adapter), tampilan itemnya memakai layout sendiri
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lv = view.findViewById<ListView>(R.id.lvHistory)
        lv.emptyView = view.findViewById(R.id.tvEmptyHistory)
        adapter = ArrayAdapter(requireContext(), R.layout.item_history, R.id.tvHistory, DummyData.history)
        lv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
