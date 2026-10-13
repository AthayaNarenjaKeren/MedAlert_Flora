package com.example.medalert_flora

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medalert_flora.MainActivity

class LoginActivity : AppCompatActivity() {

    // Akun yang dianggap benar (ganti sesuai keinginanmu)
    private val validUsername = "admin"
    private val validPassword = "12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnlogin: Button = findViewById(R.id.btnlogin)
        val username: EditText = findViewById(R.id.edtusername)
        val password: EditText = findViewById(R.id.edtpassword)

        btnlogin.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString()

            // 1. Cek kolom kosong
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Cek username dan password dengan akun yang benar
            if (user == validUsername && pass == validPassword) {
                // 3. Login berhasil -> simpan isLogin = true di SharedPreferences
                val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
                pref.edit()
                    .putBoolean("isLogin", true)
                    .putString("username", user)
                    .apply()

                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()   // tutup Login supaya tombol Back tidak kembali ke sini
            } else {
                // Login gagal
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
