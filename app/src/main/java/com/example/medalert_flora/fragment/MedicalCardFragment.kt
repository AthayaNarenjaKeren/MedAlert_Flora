package com.example.medalert_flora.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.medalert_flora.R
import com.example.medalert_flora.model.MedicalProfile
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.example.medalert_flora.pill
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout

class MedicalCardFragment : Fragment(R.layout.fragment_medical_card) {

    private lateinit var root: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = view
        bind()

        view.findViewById<Button>(R.id.btnEdit).setOnClickListener { showEditDialog() }

        // Switch "Tampilkan di layar kunci" (disimpan di SharedPreferences)
        val pref = requireContext().getSharedPreferences("medalert_pref", Context.MODE_PRIVATE)
        val switchLock = view.findViewById<SwitchMaterial>(R.id.switchLock)
        switchLock.isChecked = pref.getBoolean("lock_screen", true)
        switchLock.setOnCheckedChangeListener { _, checked ->
            pref.edit().putBoolean("lock_screen", checked).apply()
            val msg = if (checked) "Kartu medis tampil di layar kunci" else "Kartu medis disembunyikan dari layar kunci"
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    // Tampilkan data dari SharedPreferences ke layar
    private fun bind() {
        val p = MedicalProfile.load(requireContext())
        root.findViewById<TextView>(R.id.tvInitials).text = p.initials
        root.findViewById<TextView>(R.id.tvName).text = p.name
        root.findViewById<TextView>(R.id.tvAgeGender).text = "${p.age} tahun · ${p.gender}"
        root.findViewById<TextView>(R.id.tvBlood).text = p.blood
        root.findViewById<TextView>(R.id.tvMeds).text = p.meds
        root.findViewById<TextView>(R.id.tvBpjs).text = "BPJS Kesehatan · ${p.bpjsNumber}"

        fillChips(root.findViewById(R.id.chipAllergy), p.allergies, "#FDE7EA", "#E63946")
        fillChips(root.findViewById(R.id.chipCondition),
            listOf(p.condition).filter { it.isNotBlank() }, "#E8EEF5", "#1B365D")
    }

    private fun fillChips(group: ChipGroup, items: List<String>, bg: String, fg: String) {
        group.removeAllViews()
        for (item in items) group.addView(requireContext().pill(item, bg, fg))
    }


    // Dialog form "Ubah Data"
    private fun showEditDialog() {
        val ctx = requireContext()
        val p = MedicalProfile.load(ctx)
        val v = layoutInflater.inflate(R.layout.dialog_edit_medical, null)

        val tilName = v.findViewById<TextInputLayout>(R.id.tilName)
        val tilAge = v.findViewById<TextInputLayout>(R.id.tilAge)
        val etName = v.findViewById<TextInputEditText>(R.id.etName)
        val etAge = v.findViewById<TextInputEditText>(R.id.etAge)
        val rgGender = v.findViewById<RadioGroup>(R.id.rgGender)
        val spBlood = v.findViewById<Spinner>(R.id.spBlood)
        val cbPenisilin = v.findViewById<CheckBox>(R.id.cbPenisilin)
        val cbKacang = v.findViewById<CheckBox>(R.id.cbKacang)
        val cbSeafood = v.findViewById<CheckBox>(R.id.cbSeafood)
        val tvAllergyCount = v.findViewById<TextView>(R.id.tvAllergyCount)
        val etCondition = v.findViewById<TextInputEditText>(R.id.etCondition)
        val etMeds = v.findViewById<TextInputEditText>(R.id.etMeds)

        val pvInitials = v.findViewById<TextView>(R.id.pvInitials)
        val pvName = v.findViewById<TextView>(R.id.pvName)
        val pvAgeGender = v.findViewById<TextView>(R.id.pvAgeGender)
        val pvBlood = v.findViewById<TextView>(R.id.pvBlood)

        val bloods = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        spBlood.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, bloods)

        // Isi data awal
        etName.setText(p.name)
        etAge.setText(p.age.toString())
        rgGender.check(if (p.gender == "Laki-laki") R.id.rbMale else R.id.rbFemale)
        spBlood.setSelection(bloods.indexOf(p.blood).coerceAtLeast(0))
        cbPenisilin.isChecked = "Penisilin" in p.allergies
        cbKacang.isChecked = "Kacang" in p.allergies
        cbSeafood.isChecked = "Seafood" in p.allergies
        etCondition.setText(p.condition)
        etMeds.setText(p.meds)

        fun gender() = if (rgGender.checkedRadioButtonId == R.id.rbMale) "Laki-laki" else "Perempuan"

        // Pratinjau kartu berubah langsung
        fun updatePreview() {
            val name = etName.text.toString().trim().ifEmpty { "Nama" }
            pvInitials.text = name.split(" ").filter { it.isNotBlank() }.take(2)
                .joinToString("") { it.first().uppercase() }
            pvName.text = name
            val age = etAge.text.toString().ifEmpty { "-" }
            pvAgeGender.text = "$age tahun · ${gender()}"
            pvBlood.text = spBlood.selectedItem.toString()
        }

        fun updateAllergyCount() {
            val n = listOf(cbPenisilin, cbKacang, cbSeafood).count { it.isChecked }
            tvAllergyCount.text = if (n == 0) "Tidak ada alergi dipilih" else "$n alergi dipilih"
        }

        etName.doAfterTextChanged { tilName.error = null; updatePreview() }
        etAge.doAfterTextChanged { tilAge.error = null; updatePreview() }
        rgGender.setOnCheckedChangeListener { _, _ -> updatePreview() }
        spBlood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatePreview()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        for (cb in listOf(cbPenisilin, cbKacang, cbSeafood)) {
            cb.setOnCheckedChangeListener { _, _ -> updateAllergyCount() }
        }
        updatePreview()
        updateAllergyCount()

        // Dialog putih dengan sudut membulat
        val bg = GradientDrawable()
        bg.cornerRadius = 24 * resources.displayMetrics.density
        bg.setColor(Color.WHITE)

        val dialog = MaterialAlertDialogBuilder(ctx, R.style.ThemeOverlay_MedAlert_Dialog)
            .setView(v)
            .setBackground(bg)
            .setPositiveButton("Simpan", null)   // klik diatur di bawah supaya bisa menahan dialog
            .setNegativeButton("Batal", null)
            .create()
        dialog.show()

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(ctx, R.color.medalert_gray))

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull()
            var ok = true
            if (name.isEmpty()) { tilName.error = "Nama wajib diisi"; ok = false }
            if (age == null || age !in 1..120) { tilAge.error = "Masukkan usia 1-120"; ok = false }
            if (!ok) return@setOnClickListener   // dialog tetap terbuka

            val allergies = mutableListOf<String>()
            if (cbPenisilin.isChecked) allergies.add("Penisilin")
            if (cbKacang.isChecked) allergies.add("Kacang")
            if (cbSeafood.isChecked) allergies.add("Seafood")

            val updated = p.copy(
                name = name,
                age = age!!,
                gender = gender(),
                blood = spBlood.selectedItem.toString(),
                allergies = allergies,
                condition = etCondition.text.toString().trim(),
                meds = etMeds.text.toString().trim().ifEmpty { p.meds }
            )
            MedicalProfile.save(ctx, updated)
            bind()
            dialog.dismiss()
            Snackbar.make(root, "Data medis diperbarui", Snackbar.LENGTH_LONG).show()
        }
    }
}
