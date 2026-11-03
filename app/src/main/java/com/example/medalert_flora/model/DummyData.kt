package com.example.medalert_flora.model

// Data dummy bertema MedAlert (tanpa database)
object DummyData {

    val contacts = mutableListOf(
        "Ibu - 0812-3456-7890",
        "Ayah - 0813-1111-2222",
        "Kakak - 0821-3333-4444"
    )

    val history = mutableListOf(
        "12 Okt 2026, 19:30 - SOS terkirim ke 3 kontak",
        "03 Okt 2026, 08:15 - SOS dibatalkan",
        "21 Sep 2026, 22:40 - SOS terkirim ke 2 kontak"
    )

    val healthcares = listOf(
        Healthcare("RS Sehat Sentosa", "Rumah Sakit", "1,2 km", "Buka 24 jam"),
        Healthcare("Klinik Medika Utama", "Klinik", "2,0 km", "Buka sampai 21.00"),
        Healthcare("RS Harapan Bunda", "Rumah Sakit", "3,1 km", "Buka 24 jam")
    )

    // hanya nama dari "Nama - Nomor"
    fun contactNames(): List<String> = contacts.map { it.substringBefore(" - ") }
}
