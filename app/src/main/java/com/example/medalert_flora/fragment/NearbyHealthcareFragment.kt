package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.medalert_flora.MainActivity
import com.example.medalert_flora.R
import com.example.medalert_flora.adapter.HealthcareAdapter
import com.example.medalert_flora.model.DummyData
import com.google.android.material.chip.ChipGroup

// ListView dengan CUSTOM ADAPTER + filter chip (layar penuh dari kartu Faskes di Home)
class NearbyHealthcareFragment : Fragment(R.layout.fragment_nearby_healthcare) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lv = view.findViewById<ListView>(R.id.lvFaskes)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)
        val chipFilter = view.findViewById<ChipGroup>(R.id.chipFilter)

        val adapter = HealthcareAdapter(requireContext(), DummyData.healthcares)
        lv.adapter = adapter

        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            (activity as MainActivity).closeOverlay()
        }

        chipFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val type = when (checkedIds.firstOrNull()) {
                R.id.chipRs -> "Rumah Sakit"
                R.id.chipKlinik -> "Klinik"
                R.id.chipApotek -> "Apotek"
                else -> "Semua"
            }
            val data = if (type == "Semua") DummyData.healthcares
            else DummyData.healthcares.filter { it.type == type }
            adapter.updateData(data)
            tvCount.text = "${data.size} fasilitas kesehatan di sekitar kamu"
        }
    }
}
