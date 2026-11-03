package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.adapter.ContactAdapter
import com.example.medalert_flora.dial
import com.example.medalert_flora.model.DummyData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class EmergencyContactFragment : Fragment(R.layout.fragment_emergency_contact) {

    private lateinit var adapter: ContactAdapter
    private lateinit var tvSubtitle: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lv = view.findViewById<ListView>(R.id.lvContacts)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        tvSubtitle = view.findViewById(R.id.tvSubtitle)
        lv.emptyView = view.findViewById(R.id.tvEmpty)

        adapter = ContactAdapter(requireContext(), DummyData.contacts)
        lv.adapter = adapter
        refreshSubtitle()

        // Klik kontak -> buka aplikasi telepon
        lv.setOnItemClickListener { _, _, position, _ ->
            val number = DummyData.contacts[position].substringAfter(" - ", "")
                .filter { it.isDigit() || it == '+' }
            if (number.isEmpty()) {
                Snackbar.make(view, "Nomor belum diisi", Snackbar.LENGTH_SHORT).show()
            } else {
                requireContext().dial(number)
            }
        }

        // Tekan lama -> hapus (bisa dibatalkan lewat Snackbar)
        lv.setOnItemLongClickListener { _, _, position, _ ->
            val item = DummyData.contacts[position]
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Hapus kontak?")
                .setMessage(item)
                .setPositiveButton("Hapus") { _, _ ->
                    DummyData.contacts.removeAt(position)
                    adapter.notifyDataSetChanged()
                    refreshSubtitle()
                    Snackbar.make(view, "Kontak dihapus", Snackbar.LENGTH_LONG)
                        .setAction("Batalkan") {
                            DummyData.contacts.add(position, item)
                            adapter.notifyDataSetChanged()
                            refreshSubtitle()
                        }.show()
                }
                .setNegativeButton("Batal", null)
                .show()
            true
        }

        fab.setOnClickListener { showAddDialog(view) }
    }

    private fun refreshSubtitle() {
        tvSubtitle.text = "${DummyData.contacts.size} kontak akan dihubungi saat SOS"
    }

    private fun showAddDialog(root: View) {
        val v = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val etName = v.findViewById<TextInputEditText>(R.id.etContactName)
        val etPhone = v.findViewById<TextInputEditText>(R.id.etContactPhone)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tambah Kontak Darurat")
            .setView(v)
            .setPositiveButton("Simpan") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                if (name.isEmpty() || phone.isEmpty()) {
                    Snackbar.make(root, "Nama dan nomor HP wajib diisi", Snackbar.LENGTH_SHORT).show()
                } else {
                    DummyData.contacts.add("$name - $phone")
                    adapter.notifyDataSetChanged()
                    refreshSubtitle()
                    Snackbar.make(root, "Kontak ditambahkan", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
