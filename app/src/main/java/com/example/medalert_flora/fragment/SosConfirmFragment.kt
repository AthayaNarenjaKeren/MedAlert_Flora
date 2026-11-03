package com.example.medalert_flora.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.medalert_flora.MainActivity
import com.example.medalert_flora.R
import com.example.medalert_flora.model.DummyData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.example.medalert_flora.pill

// Layar Konfirmasi SOS: hitung mundur 5 detik, lalu otomatis kirim
class SosConfirmFragment : Fragment(R.layout.fragment_sos_confirm) {

    private var timer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val main = activity as MainActivity

        val tvCount = view.findViewById<TextView>(R.id.tvCount)
        val ring = view.findViewById<CircularProgressIndicator>(R.id.ringCountdown)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipRecipients)

        // Penerima = nama dari daftar kontak darurat
        for (name in DummyData.contactNames()) {
            chipGroup.addView(requireContext().pill(name, "#E8EEF5", "#1B365D"))
        }

        // Tutup / Batalkan -> kembali ke Home
        view.findViewById<View>(R.id.btnClose).setOnClickListener { main.closeOverlay() }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener { main.closeOverlay() }

        // Kirim sekarang -> langsung ke Emergency Mode
        view.findViewById<View>(R.id.btnSendNow).setOnClickListener {
            timer?.cancel()
            main.startEmergency()
        }

        // Hitung mundur 5 detik
        timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = ((millisUntilFinished + 999) / 1000).toInt()
                tvCount.text = sec.toString()
                ring.setProgressCompat(sec * 20, true)
            }

            override fun onFinish() {
                main.startEmergency()
            }
        }.start()
    }

    override fun onDestroyView() {
        timer?.cancel()   // hentikan timer jika layar ditutup
        super.onDestroyView()
    }
}
