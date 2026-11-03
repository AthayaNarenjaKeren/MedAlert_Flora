package com.example.medalert_flora.model

import android.content.Context

// Data kartu medis, disimpan di SharedPreferences "medalert_pref"
data class MedicalProfile(
    val name: String,
    val age: Int,
    val gender: String,
    val blood: String,
    val allergies: List<String>,
    val condition: String,
    val meds: String,
    val bpjsNumber: String
) {
    val initials: String
        get() = name.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }

    val firstName: String
        get() = name.trim().split(" ").first()

    companion object {
        private const val PREF = "medalert_pref"

        fun load(context: Context): MedicalProfile {
            val p = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            val allergies = p.getString("mp_allergies", "Penisilin,Kacang").orEmpty()
            return MedicalProfile(
                name = p.getString("mp_name", "Rina Amelia").orEmpty(),
                age = p.getInt("mp_age", 21),
                gender = p.getString("mp_gender", "Perempuan").orEmpty(),
                blood = p.getString("mp_blood", "O+").orEmpty(),
                allergies = if (allergies.isBlank()) emptyList() else allergies.split(","),
                condition = p.getString("mp_condition", "Asma").orEmpty(),
                meds = p.getString("mp_meds", "Ventolin inhaler (saat sesak)").orEmpty(),
                bpjsNumber = p.getString("mp_bpjs", "0001234567890").orEmpty()
            )
        }

        fun save(context: Context, m: MedicalProfile) {
            context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
                .putString("mp_name", m.name)
                .putInt("mp_age", m.age)
                .putString("mp_gender", m.gender)
                .putString("mp_blood", m.blood)
                .putString("mp_allergies", m.allergies.joinToString(","))
                .putString("mp_condition", m.condition)
                .putString("mp_meds", m.meds)
                .putString("mp_bpjs", m.bpjsNumber)
                .apply()
        }
    }
}
