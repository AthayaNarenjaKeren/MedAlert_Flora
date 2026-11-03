package com.example.medalert_flora

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView

// Buka aplikasi telepon dengan nomor terisi (tidak butuh izin)
fun Context.dial(number: String) {
    try {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Aplikasi telepon tidak ditemukan", Toast.LENGTH_SHORT).show()
    }
}

// Buka aplikasi peta untuk mencari nama faskes
fun Context.openRoute(place: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(place))))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Aplikasi peta tidak ditemukan", Toast.LENGTH_SHORT).show()
    }
}

fun Context.pill(text: String, bgHex: String, fgHex: String): TextView {
    val d = resources.displayMetrics.density
    val tv = TextView(this)
    tv.text = text
    tv.textSize = 13f
    tv.setTextColor(Color.parseColor(fgHex))
    val bg = GradientDrawable()
    bg.cornerRadius = 100 * d
    bg.setColor(Color.parseColor(bgHex))
    tv.background = bg
    tv.setPadding((14 * d).toInt(), (7 * d).toInt(), (14 * d).toInt(), (7 * d).toInt())
    return tv
}