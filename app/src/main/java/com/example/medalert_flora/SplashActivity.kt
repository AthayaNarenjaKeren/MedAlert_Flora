package com.example.medalert_flora

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tunggu 2 detik, lalu tentukan tujuan
        Handler(Looper.getMainLooper()).postDelayed({
            val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
            val tutorialSeen = pref.getBoolean("tutorial_seen", false)   // default false
            val isLogin = pref.getBoolean("isLogin", false)              // default false

            val tujuan = when {
                !tutorialSeen -> TutorialActivity::class.java   // pertama kali -> Tutorial Screen
                isLogin -> MainActivity::class.java             // sudah login -> Home
                else -> LoginActivity::class.java               // belum login -> Login
            }
            startActivity(Intent(this, tujuan))
            finish()   // tutup Splash supaya tombol Back tidak kembali ke sini
        }, 2000)
    }
}
