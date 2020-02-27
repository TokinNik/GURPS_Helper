package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.testapp.util.Base64RequestHandler
import com.example.testapp.util.DataManager
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class MainActivity : AppCompatActivity() {

    private val dataManager: DataManager by inject()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentAdd(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toothpick.inject(this, Toothpick.openScope("APP"))

        if (dataManager.appSettingsVault.isNightTheme)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.startFragment,
                R.id.skillObserveAllFragment,
                R.id.battleFragment,
                R.id.settingsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Picasso.setSingletonInstance (
            Picasso.Builder(this)
                .addRequestHandler(Base64RequestHandler())
                .build()
        )
    }

    override fun onResume() {
        super.onResume()
        checkIntentAdd(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkIntentAdd(intent: Intent?)
    {
        if (intent?.data != null)
        {
            dataManager.appSettingsVault.fileIntentAdd = intent.data?.path ?: ""
            intent.action = null//todo not normal clear intent
            intent.data = null
            intent.flags = 0
            intent.replaceExtras(Bundle())
        }
    }
}
