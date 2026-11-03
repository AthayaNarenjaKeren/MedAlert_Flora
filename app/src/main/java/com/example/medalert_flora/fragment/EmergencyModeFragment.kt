package com.example.medalert_flora.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medalert_flora.MainActivity
import com.example.medalert_flora.R
import com.example.medalert_flora.dial
import com.example.medalert_flora.model.DummyData
import com.example.medalert_flora.model.MedicalProfile
import com.example.medalert_flora.openRoute
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Layar Emergency Mode (layar penuh, tanpa toolbar & bottom nav)
class EmergencyModeFragment : Fragment(R.layout.fragment_emergency) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val p = MedicalProfile.load(requireContext())
        val time = SimpleDateFormat("HH:mm", Locale("id")).format(Date())

        view.findViewById<TextView>(R.id.tvTime).text = "SOS dikirim pukul $time"
        view.findViewById<TextView>(R.id.tvLocationSent).text =
            "Lokasi terkirim ke ${DummyData.contacts.size} kontak"

        // Info untuk penolong dari data kartu medis
        view.findViewById<TextView>(R.id.tvBloodBadge).text = p.blood
        view.findViewById<TextView>(R.id.tvInfoName).text = p.name
        view.findViewById<TextView>(R.id.tvInfoAllergy).text =
            if (p.allergies.isEmpty()) "-" else p.allergies.joinToString(", ")
        view.findViewById<TextView>(R.id.tvInfoCondition).text = p.condition.ifBlank { "-" }
        view.findViewById<TextView>(R.id.tvInfoMeds).text = p.meds.substringBefore(" (")
        view.findViewById<TextView>(R.id.tvInfoBpjs).text = "BPJS ${p.bpjsNumber}"

        // Faskes terdekat = data pertama
        val near = DummyData.healthcares[0]
        view.findViewById<TextView>(R.id.tvNearName).text = near.name
        view.findViewById<TextView>(R.id.tvNearDistance).text = near.distance
        view.findViewById<TextView>(R.id.tvNearHours).text = " · ${near.hours}"

        view.findViewById<View>(R.id.btnCall).setOnClickListener { requireContext().dial("112") }
        view.findViewById<View>(R.id.btnAmbulance).setOnClickListener { requireContext().dial("119") }
        view.findViewById<View>(R.id.btnRoute).setOnClickListener { requireContext().openRoute(near.name) }

        // Akhiri darurat dengan konfirmasi
        view.findViewById<View>(R.id.btnEnd).setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Akhiri Darurat")
                .setMessage("Apakah bantuan sudah datang?")
                .setPositiveButton("Ya") { _, _ ->
                    Toast.makeText(requireContext(), "Mode darurat diakhiri", Toast.LENGTH_SHORT).show()
                    (activity as MainActivity).closeOverlay()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }
}
