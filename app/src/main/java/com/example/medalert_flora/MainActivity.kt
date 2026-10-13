package com.example.medalert_flora

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Pasang Toolbar sebagai action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Tampilkan nama pengguna yang disimpan saat login
        val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
        val username = pref.getString("username", "Pengguna")
        findViewById<TextView>(R.id.tvGreeting).text = "Halo, $username"

        findViewById<TextView>(R.id.tvSos).setOnClickListener {
            Toast.makeText(this, "SOS ditekan (demo)", Toast.LENGTH_SHORT).show()
        }
    }

    // Tampilkan menu Logout di toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            // Logout: ubah isLogin jadi false, lalu kembali ke Login
            val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
            pref.edit().putBoolean("isLogin", false).apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}