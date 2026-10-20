package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.adapter.HealthcareAdapter
import com.example.medalert_flora.model.DummyData

// ListView dengan CUSTOM ADAPTER (HealthcareAdapter : BaseAdapter)
class NearbyHealthcareFragment : Fragment(R.layout.fragment_nearby_healthcare) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spFilter = view.findViewById<Spinner>(R.id.spFilter)
        val lv = view.findViewById<ListView>(R.id.lvFaskes)

        val adapter = HealthcareAdapter(requireContext(), DummyData.healthcares)
        lv.adapter = adapter

        val filters = listOf("Semua", "Rumah Sakit", "Klinik", "Apotek")
        spFilter.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, filters)
        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                val selected = filters[position]
                val data = if (selected == "Semua") DummyData.healthcares
                else DummyData.healthcares.filter { it.type == selected }
                adapter.updateData(data)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        lv.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position)
            Toast.makeText(requireContext(), "${item.name} - ${item.distance}", Toast.LENGTH_SHORT).show()
        }
    }
}