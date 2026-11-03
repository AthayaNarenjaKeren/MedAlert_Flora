package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.medalert_flora.MainActivity
import com.example.medalert_flora.R
import com.example.medalert_flora.dial
import com.example.medalert_flora.model.DummyData
import com.example.medalert_flora.model.MedicalProfile

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val main = activity as MainActivity
        val profile = MedicalProfile.load(requireContext())

        view.findViewById<TextView>(R.id.tvGreeting).text = "Halo, ${profile.firstName}"
        view.findViewById<TextView>(R.id.tvAvatar).text = profile.initials
        view.findViewById<TextView>(R.id.tvStatus).text =
            "Data medis lengkap  ·  ${DummyData.contacts.size} kontak darurat"

        // SOS -> layar Konfirmasi SOS (hitung mundur)
        view.findViewById<TextView>(R.id.tvSos).setOnClickListener { main.startSos() }

        // Akses cepat
        view.findViewById<View>(R.id.cardCall).setOnClickListener { requireContext().dial("112") }
        view.findViewById<View>(R.id.cardAmbulance).setOnClickListener { requireContext().dial("119") }
        view.findViewById<View>(R.id.cardFaskes).setOnClickListener { main.openFaskes() }
        view.findViewById<View>(R.id.cardTips).setOnClickListener { main.openTips() }
    }
}
