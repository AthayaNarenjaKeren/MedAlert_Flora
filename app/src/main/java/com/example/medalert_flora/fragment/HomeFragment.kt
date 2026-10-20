package com.example.medalert_flora.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.model.DummyData
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val tvSos = view.findViewById<TextView>(R.id.tvSos)
        val progress = view.findViewById<ProgressBar>(R.id.progressSos)
        val switchLoc = view.findViewById<SwitchMaterial>(R.id.switchLocation)
        val cbSms = view.findViewById<CheckBox>(R.id.cbSms)

        // Nama pengguna dari SharedPreferences (disimpan saat login)
        val pref = requireContext().getSharedPreferences("medalert_pref", Context.MODE_PRIVATE)
        tvGreeting.text = "Halo, ${pref.getString("username", "Pengguna")}"
        tvStatus.text = "●  Data medis lengkap  ·  ${DummyData.contacts.size} kontak darurat"

        // SOS -> DialogFragment konfirmasi
        tvSos.setOnClickListener {
            SosConfirmDialogFragment().show(childFragmentManager, "sos")
        }

        // Hasil dari dialog (Fragment Result API)
        childFragmentManager.setFragmentResultListener(
            SosConfirmDialogFragment.RESULT_KEY, viewLifecycleOwner
        ) { _, bundle ->
            if (bundle.getBoolean("confirmed")) {
                sendSos(progress, cbSms.isChecked)
            } else {
                Toast.makeText(requireContext(), "SOS dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }

        switchLoc.setOnCheckedChangeListener { _, checked ->
            val msg = if (checked) "Lokasi akan dibagikan" else "Lokasi tidak dibagikan"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    // Simulasi pengiriman SOS memakai ProgressBar
    private fun sendSos(progress: ProgressBar, sms: Boolean) {
        progress.visibility = View.VISIBLE
        progress.progress = 0
        var value = 0
        val step = object : Runnable {
            override fun run() {
                value += 20
                progress.progress = value
                if (value < 100) {
                    handler.postDelayed(this, 300)
                } else {
                    progress.visibility = View.GONE
                    val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")).format(Date())
                    val extra = if (sms) " (+SMS)" else ""
                    DummyData.history.add(0, "$now - SOS terkirim$extra")
                    Toast.makeText(requireContext(), "SOS berhasil dikirim", Toast.LENGTH_LONG).show()
                }
            }
        }
        handler.post(step)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)   // hentikan simulasi jika fragment ditutup
        super.onDestroyView()
    }
}