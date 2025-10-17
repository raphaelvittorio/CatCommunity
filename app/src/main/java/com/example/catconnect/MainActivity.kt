package com.example.catconnect

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHost.navController

        // Top-level destinations for AppBarConfiguration
        val topLevelDestinations = setOf(
            R.id.feedFragment,
            R.id.mapFragment,
            R.id.profileFragment,
            R.id.calendarFragment // Added Calendar to top-level
        )
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = "CatPaw"

            if (destination.id in topLevelDestinations) {
                binding.bottomNav.visibility = View.VISIBLE
            } else {
                binding.bottomNav.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showAppBar(show: Boolean) {
        val navHostLayoutParams = binding.navHost.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        if (show) {
            supportActionBar?.show()
            val actionBarSize = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0f)
            navHostLayoutParams.topMargin = actionBarSize.toInt()
        } else {
            supportActionBar?.hide()
            navHostLayoutParams.topMargin = 0
        }
        binding.navHost.layoutParams = navHostLayoutParams
    }
}
