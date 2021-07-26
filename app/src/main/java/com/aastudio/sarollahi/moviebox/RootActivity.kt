/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aastudio.sarollahi.moviebox.databinding.ActivityRootBinding
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RootActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRootBinding
    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        startKoin {
            androidContext(applicationContext)
            modules(viewModelsModule)
        }
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val rightNavView: NavigationView = binding.navViewRight
        val actionBar = supportActionBar
        assert(actionBar != null)
        actionBar!!.elevation = 0f
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        navView.itemIconTintList = null
        rightNavView.itemIconTintList = null
        navView.setNavigationItemSelectedListener(this)
        rightNavView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.drawer_right) {
            if (drawerLayout?.isDrawerOpen(GravityCompat.END) == true) {
                drawerLayout?.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout?.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }
}
