package com.example.medalert_flora.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class MedicalCardFragment : Fragment(R.layout.fragment_medical_card) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etAge = view.findViewById<TextInputEditText>(R.id.etAge)
        val rgGender = view.findViewById<RadioGroup>(R.id.rgGender)
        val spBlood = view.findViewById<Spinner>(R.id.spBlood)
        val cbDrug = view.findViewById<CheckBox>(R.id.cbAllergyDrug)
        val cbFood = view.findViewById<CheckBox>(R.id.cbAllergyFood)
        val sbHeight = view.findViewById<SeekBar>(R.id.sbHeight)
        val tvHeight = view.findViewById<TextView>(R.id.tvHeight)
        val switchDonor = view.findViewById<SwitchMaterial>(R.id.switchDonor)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val tvSummary = view.findViewById<TextView>(R.id.tvSummary)

        // Spinner golongan darah
        val bloods = listOf("A", "B", "AB", "O")
        spBlood.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bloods)

        // SeekBar tinggi badan: 100 - 200 cm
        sbHeight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvHeight.text = "${100 + progress} cm"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Muat data yang pernah disimpan
        val pref = requireContext().getSharedPreferences("medalert_pref", Context.MODE_PRIVATE)
        etName.setText(pref.getString("card_name", ""))
        etAge.setText(pref.getString("card_age", ""))

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().trim()
            if (name.isEmpty()) { etName.error = "Nama wajib diisi"; return@setOnClickListener }
            if (age.isEmpty()) { etAge.error = "Usia wajib diisi"; return@setOnClickListener }

            val gender = when (rgGender.checkedRadioButtonId) {
                R.id.rbMale -> "Laki-laki"
                R.id.rbFemale -> "Perempuan"
                else -> "-"
            }
            val allergies = mutableListOf<String>()
            if (cbDrug.isChecked) allergies.add("Obat")
            if (cbFood.isChecked) allergies.add("Makanan")

            pref.edit().putString("card_name", name).putString("card_age", age).apply()

            tvSummary.text = "Nama: $name\n" +
                    "Usia: $age tahun\n" +
                    "Jenis kelamin: $gender\n" +
                    "Gol. darah: ${spBlood.selectedItem}\n" +
                    "Alergi: ${if (allergies.isEmpty()) "Tidak ada" else allergies.joinToString()}\n" +
                    "Tinggi: ${tvHeight.text}\n" +
                    "Donor organ: ${if (switchDonor.isChecked) "Ya" else "Tidak"}"
            Toast.makeText(requireContext(), "Kartu medis disimpan", Toast.LENGTH_SHORT).show()
        }
    }
}