package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.model.DummyData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

// ListView dengan SIMPLE LIST ITEM (ArrayAdapter + simple_list_item_1)
class EmergencyContactFragment : Fragment(R.layout.fragment_emergency_contact) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lv = view.findViewById<ListView>(R.id.lvContacts)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        lv.emptyView = view.findViewById(R.id.tvEmpty)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, DummyData.contacts)
        lv.adapter = adapter

        // Klik item
        lv.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(requireContext(), "Menghubungi ${DummyData.contacts[position]}", Toast.LENGTH_SHORT).show()
        }

        // Tekan lama -> hapus
        lv.setOnItemLongClickListener { _, _, position, _ ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Hapus kontak?")
                .setMessage(DummyData.contacts[position])
                .setPositiveButton("Hapus") { _, _ ->
                    DummyData.contacts.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("Batal", null)
                .show()
            true
        }

        // Tambah kontak
        fab.setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Nama - Nomor HP"
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Tambah Kontak Darurat")
                .setView(input)
                .setPositiveButton("Simpan") { _, _ ->
                    val text = input.text.toString().trim()
                    if (text.isNotEmpty()) {
                        DummyData.contacts.add(text)
                        adapter.notifyDataSetChanged()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }
}