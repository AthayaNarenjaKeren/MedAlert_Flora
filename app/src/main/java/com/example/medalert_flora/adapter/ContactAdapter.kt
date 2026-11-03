package com.example.medalert_flora.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.medalert_flora.R

// Adapter kontak: data tetap berupa teks "Nama - Nomor", tampilannya dipecah jadi avatar, nama, nomor
class ContactAdapter(
    context: Context,
    private val data: MutableList<String>
) : ArrayAdapter<String>(context, 0, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)

        val raw = data[position]
        val name = raw.substringBefore(" - ")
        val number = if (raw.contains(" - ")) raw.substringAfter(" - ") else ""

        view.findViewById<TextView>(R.id.tvAvatar).text =
            name.trim().firstOrNull()?.uppercase() ?: "?"
        view.findViewById<TextView>(R.id.tvName).text = name
        view.findViewById<TextView>(R.id.tvNumber).text = number.ifBlank { "-" }
        return view
    }
}
