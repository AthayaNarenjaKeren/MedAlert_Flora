package com.example.medalert_flora

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.medalert_flora.fragment.EmergencyContactFragment
import com.example.medalert_flora.fragment.EmergencyModeFragment
import com.example.medalert_flora.fragment.HistoryFragment
import com.example.medalert_flora.fragment.HomeFragment
import com.example.medalert_flora.fragment.MedicalCardFragment
import com.example.medalert_flora.fragment.NearbyHealthcareFragment
import com.example.medalert_flora.fragment.SosConfirmFragment
import com.example.medalert_flora.fragment.WebViewFragment
import com.example.medalert_flora.model.DummyData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: View
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var overlayBack: OnBackPressedCallback
    private var overlayBackAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        rootView = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        bottomNav = findViewById(R.id.bottom_nav)

        // Tombol Back saat layar penuh (overlay) tampil
        overlayBack = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                if (overlayBackAllowed) closeOverlay()
                else Toast.makeText(this@MainActivity, "Tekan \"Akhiri Darurat\" untuk keluar", Toast.LENGTH_SHORT).show()
            }
        }
        onBackPressedDispatcher.addCallback(this, overlayBack)

        bottomNav.setOnItemSelectedListener { item ->
            showTab(item.itemId)
            true
        }
        if (savedInstanceState == null) bottomNav.selectedItemId = R.id.nav_home
    }

    // ---------- Tab bottom navigation ----------
    private fun showTab(menuId: Int) {
        when (menuId) {
            R.id.nav_home -> loadFragment(HomeFragment(), "MedAlert")
            R.id.nav_card -> loadFragment(MedicalCardFragment(), "Medical Card")
            R.id.nav_contact -> loadFragment(EmergencyContactFragment(), "Kontak Darurat")
            R.id.nav_history -> loadFragment(HistoryFragment(), "Riwayat Darurat")
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportActionBar?.title = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    // ---------- Layar penuh (tanpa toolbar & bottom nav) ----------
    // topColorRes = warna area status bar supaya menyatu dengan layar
    private fun showOverlay(fragment: Fragment, allowBack: Boolean, topColorRes: Int) {
        overlayBackAllowed = allowBack
        overlayBack.isEnabled = true
        rootView.setBackgroundColor(ContextCompat.getColor(this, topColorRes))
        toolbar.visibility = View.GONE
        bottomNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    fun closeOverlay() {
        overlayBack.isEnabled = false
        rootView.setBackgroundColor(Color.WHITE)
        toolbar.visibility = View.VISIBLE
        bottomNav.visibility = View.VISIBLE
        showTab(bottomNav.selectedItemId)
    }

    fun startSos() = showOverlay(SosConfirmFragment(), true, R.color.medalert_pink_bg)

    fun openFaskes() = showOverlay(NearbyHealthcareFragment(), true, R.color.medalert_bg)

    fun openTips() = showOverlay(WebViewFragment(), true, R.color.medalert_bg)

    fun startEmergency() {
        // catat ke riwayat
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id")).format(Date())
        DummyData.history.add(0, "$now - SOS terkirim ke ${DummyData.contacts.size} kontak")
        showOverlay(EmergencyModeFragment(), false, R.color.medalert_red)
    }

    // ---------- Menu Logout dengan konfirmasi ----------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Keluar")
                .setMessage("Apakah kamu yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    val pref = getSharedPreferences("medalert_pref", MODE_PRIVATE)
                    pref.edit().putBoolean("isLogin", false).apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Tidak", null)
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
