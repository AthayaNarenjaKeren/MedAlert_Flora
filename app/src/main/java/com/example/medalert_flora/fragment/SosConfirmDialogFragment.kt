package com.example.medalert_flora.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Layar "Konfirmasi SOS" dibuat sebagai DialogFragment
class SosConfirmDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Konfirmasi SOS")
            .setMessage("Kirim sinyal darurat beserta lokasi ke kontak darurat Anda?")
            .setPositiveButton("Kirim SOS") { _, _ ->
                parentFragmentManager.setFragmentResult(RESULT_KEY, bundleOf("confirmed" to true))
            }
            .setNegativeButton("Batal") { _, _ ->
                parentFragmentManager.setFragmentResult(RESULT_KEY, bundleOf("confirmed" to false))
            }
            .create()
    }

    companion object {
        const val RESULT_KEY = "sos_result"
    }
}