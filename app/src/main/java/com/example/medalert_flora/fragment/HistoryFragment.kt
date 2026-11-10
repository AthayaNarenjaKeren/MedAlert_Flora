package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.model.DummyData
import com.google.android.material.chip.ChipGroup

// Riwayat dengan ChipGroup sebagai filter (Semua / Terkirim / Dibatalkan)
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var adapter: ArrayAdapter<String>
    private val shown = mutableListOf<String>()      // data yang sedang ditampilkan
    private var filterKeyword = ""                   // "" = semua

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lv = view.findViewById<ListView>(R.id.lvHistory)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupHistory)
        lv.emptyView = view.findViewById(R.id.tvEmptyHistory)

        adapter = ArrayAdapter(requireContext(), R.layout.item_history, R.id.tvHistory, shown)
        lv.adapter = adapter

        // Deteksi chip yang dipilih
        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            filterKeyword = when (checkedIds.firstOrNull()) {
                R.id.chipHSent -> "terkirim"
                R.id.chipHCancel -> "dibatalkan"
                else -> ""
            }
            applyFilter()
        }
        applyFilter()
    }

    private fun applyFilter() {
        shown.clear()
        shown.addAll(DummyData.history.filter { it.contains(filterKeyword, ignoreCase = true) })
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        applyFilter()
    }
}
