package com.example.medalert_flora

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.viewpager2.widget.ViewPager2
import com.example.medalert_flora.tutorial.Tutorial1Fragment
import com.example.medalert_flora.tutorial.Tutorial2Fragment
import com.example.medalert_flora.tutorial.Tutorial3Fragment
import com.example.medalert_flora.tutorial.TutorialFragmentAdapter
import com.google.android.material.button.MaterialButton
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

// Tutorial Screen: tampil setelah Splash (sekali), atau dibuka lagi lewat ikon Info di toolbar
class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutorial)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val dots = findViewById<DotsIndicator>(R.id.dot_indicator)
        val btnSkip = findViewById<TextView>(R.id.btnSkip)
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val bottomGroup = findViewById<View>(R.id.bottomGroup)
        val fromMenu = intent.getBooleanExtra("from_menu", false)

        // Jaga tombol agar tidak tertutup status bar / navigation bar
        val density = resources.displayMetrics.density
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tutorialRoot)) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            btnSkip.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = bars.top + (8 * density).toInt()
            }
            bottomGroup.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = bars.bottom + (24 * density).toInt()
            }
            insets
        }

        // ViewPager2 + adapter (3 fragment)
        val fragments = listOf(Tutorial1Fragment(), Tutorial2Fragment(), Tutorial3Fragment())
        viewPager.adapter = TutorialFragmentAdapter(this, fragments)

        // DotsIndicator: dipasang SETELAH adapter
        dots.attachTo(viewPager)

        // Ubah tombol sesuai halaman
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val last = position == fragments.size - 1
                btnNext.text = if (last) "Mulai" else "Lanjut"
                btnSkip.visibility = if (last) View.INVISIBLE else View.VISIBLE
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < fragments.size - 1) viewPager.currentItem += 1
            else finishTutorial(fromMenu)
        }
        btnSkip.setOnClickListener { finishTutorial(fromMenu) }
    }

    private fun finishTutorial(fromMenu: Boolean) {
        if (fromMenu) {        // dibuka dari toolbar: cukup kembali
            finish()
            return
        }
        // dibuka setelah Splash: catat sudah dilihat, lanjut ke Login atau Home
        val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
        pref.edit().putBoolean("tutorial_seen", true).apply()
        val isLogin = pref.getBoolean("isLogin", false)
        val target = if (isLogin) MainActivity::class.java else LoginActivity::class.java
        startActivity(Intent(this, target))
        finish()
    }
}
