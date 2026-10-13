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

        // Tunggu 2 detik, lalu cek apakah pengguna sudah login
        Handler(Looper.getMainLooper()).postDelayed({
            val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
            val isLogin = pref.getBoolean("isLogin", false)   // default false

            val tujuan = if (isLogin) {
                MainActivity::class.java      // sudah login -> langsung ke Home
            } else {
                LoginActivity::class.java     // belum login -> ke halaman Login
            }
            startActivity(Intent(this, tujuan))
            finish()   // tutup Splash supaya tombol Back tidak kembali ke sini
        }, 2000)
    }
}
