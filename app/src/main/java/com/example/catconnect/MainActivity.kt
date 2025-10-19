package com.example.catconnect

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.catconnect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Ganti R.id.nav_host dengan ID NavHostFragment Anda yang benar
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHost.navController

        // Top-level destinations Anda sudah benar
        val topLevelDestinations = setOf(
            R.id.feedFragment,
            R.id.profileFragment,
            R.id.calendarFragment,
            R.id.findFragment
        )
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        // --- PENYESUAIAN DIMULAI DI SINI ---
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = ""

            // Logika untuk menampilkan/menyembunyikan Toolbar, BottomNav, dan Divider
            when (destination.id) {
                // Jika tujuan adalah halaman yang memiliki toolbar sendiri
                R.id.personalChatFragment, R.id.addPostFragment -> {
                    supportActionBar?.hide() // Sembunyikan toolbar utama
                    binding.bottomNav.visibility = View.GONE // Sembunyikan bottom nav
                    binding.bottomDivider.visibility = View.GONE // Sembunyikan divider
                }
                // Jika tujuan adalah halaman level atas (Top-Level)
                in topLevelDestinations -> {
                    supportActionBar?.show() // Tampilkan toolbar utama
                    binding.bottomNav.visibility = View.VISIBLE // Tampilkan bottom nav
                    binding.bottomDivider.visibility = View.VISIBLE // Tampilkan divider
                }
                // Untuk semua halaman lainnya (halaman detail selain chat)
                else -> {
                    supportActionBar?.show() // Tampilkan toolbar utama
                    binding.bottomNav.visibility = View.GONE // Sembunyikan bottom nav
                    binding.bottomDivider.visibility = View.GONE // Sembunyikan divider
                }
            }

            // Invalidate the options menu to force a redraw
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_feed_toolbar, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Ganti R.id.nav_host dengan ID NavHostFragment Anda yang benar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val chatItem = menu.findItem(R.id.action_chat)
        chatItem?.isVisible = navController.currentDestination?.id == R.id.feedFragment
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Ganti R.id.nav_host dan action ID dengan yang benar
        if (item.itemId == R.id.action_chat) {
            findNavController(R.id.nav_host).navigate(R.id.action_feed_to_chat)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Ganti R.id.nav_host dengan ID NavHostFragment Anda yang benar
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Fungsi showAppBar ini sekarang tidak diperlukan karena sudah ditangani oleh listener,
    // tapi tidak apa-apa jika tetap ada.
    fun showAppBar(show: Boolean) {
        if (show) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }
}
