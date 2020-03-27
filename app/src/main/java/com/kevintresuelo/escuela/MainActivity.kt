package com.kevintresuelo.escuela

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.kevintresuelo.escuela.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.amToolbar)

        val navController = this.findNavController(R.id.am_fragment_nav_host)

        drawerLayout = binding.amDrawerLayout
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.dashboardFragment,
            R.id.subjectsFragment,
            R.id.academicTermsListFragment,
            R.id.instructorsFragment,
            R.id.sessionsFragment,
            R.id.gradesFragment
        ), drawerLayout)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.amNavigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.am_fragment_nav_host)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
