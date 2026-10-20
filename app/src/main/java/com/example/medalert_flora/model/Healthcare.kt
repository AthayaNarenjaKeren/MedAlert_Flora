package com.example.medalert_flora.model

data class Healthcare(
    val name: String,
    val type: String,      // Rumah Sakit / Klinik / Apotek
    val address: String,
    val distance: String,
    val iconRes: Int
)