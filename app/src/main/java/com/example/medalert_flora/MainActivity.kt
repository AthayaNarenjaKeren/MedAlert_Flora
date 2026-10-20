package com.example.medalert_flora

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.medalert_flora.fragment.EmergencyContactFragment
import com.example.medalert_flora.fragment.HistoryFragment
import com.example.medalert_flora.fragment.HomeFragment
import com.example.medalert_flora.fragment.MedicalCardFragment
import com.example.medalert_flora.fragment.NearbyHealthcareFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

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

        // Bottom navigation -> ganti fragment
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment(), "MedAlert")
                R.id.nav_card -> loadFragment(MedicalCardFragment(), "Medical Card")
                R.id.nav_contact -> loadFragment(EmergencyContactFragment(), "Kontak Darurat")
                R.id.nav_faskes -> loadFragment(NearbyHealthcareFragment(), "Faskes Terdekat")
                R.id.nav_history -> loadFragment(HistoryFragment(), "Riwayat Darurat")
            }
            true
        }

        // Tab awal = Home (hanya saat pertama dibuat)
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportActionBar?.title = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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