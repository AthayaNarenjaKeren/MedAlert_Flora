package com.example.medalert_flora.model

// Data dummy bertema MedAlert (tanpa database, cukup untuk latihan ListView)
object DummyData {

    val contacts = mutableListOf(
        "Mama - 0812-3456-7890",
        "Papa - 0813-1111-2222",
        "Kakak - 0821-3333-4444"
    )

    val history = mutableListOf(
        "12 Okt 2026, 19:30 - SOS terkirim ke 3 kontak",
        "03 Okt 2026, 08:15 - SOS dibatalkan",
        "21 Sep 2026, 22:40 - SOS terkirim ke 2 kontak"
    )

    val healthcares = listOf(
        Healthcare("RSUD Arifin Achmad", "Rumah Sakit", "Jl. Diponegoro, Pekanbaru", "2,1 km", android.R.drawable.ic_menu_myplaces),
        Healthcare("RS Awal Bros", "Rumah Sakit", "Jl. Jend. Sudirman, Pekanbaru", "3,4 km", android.R.drawable.ic_menu_myplaces),
        Healthcare("Klinik Pratama Sehat", "Klinik", "Jl. Soekarno-Hatta, Pekanbaru", "1,2 km", android.R.drawable.ic_menu_agenda),
        Healthcare("Klinik Medika Riau", "Klinik", "Jl. Tuanku Tambusai, Pekanbaru", "2,8 km", android.R.drawable.ic_menu_agenda),
        Healthcare("Apotek K-24", "Apotek", "Jl. Arengka, Pekanbaru", "0,9 km", android.R.drawable.ic_menu_add),
        Healthcare("Apotek Kimia Farma", "Apotek", "Jl. Ahmad Yani, Pekanbaru", "1,7 km", android.R.drawable.ic_menu_add)
    )
}