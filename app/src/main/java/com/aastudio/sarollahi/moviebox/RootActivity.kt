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
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.aastudio.sarollahi.moviebox.databinding.ActivityRootBinding
import com.aastudio.sarollahi.moviebox.ui.rootMovie.RootMovieFragment
import com.aastudio.sarollahi.moviebox.ui.rootTV.RootTVFragment
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

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
        val fragment: Fragment
        val fragmentManager = supportFragmentManager
        when (item.itemId) {
            R.id.nav_movies -> {
                fragment = RootMovieFragment()
                fragmentManager.commit {
                    this.replace(
                        binding.appBarMain.contentMain.navHostFragmentContentMain.id,
                        fragment
                    )
                }
                drawerLayout?.closeDrawer(GravityCompat.START)
            }
            R.id.nav_tv_shows -> {
                fragment = RootTVFragment()
                fragmentManager.commit {
                    this.replace(
                        binding.appBarMain.contentMain.navHostFragmentContentMain.id,
                        fragment
                    )
                }
                drawerLayout?.closeDrawer(GravityCompat.START)
            }
            R.id.nav_music_videos -> {
            }
            R.id.nav_wish -> {
            }
            R.id.nav_download -> {
            }
            R.id.nav_updates -> {
            }
            R.id.nav_br -> {
            }
            R.id.nav_contact -> {
            }
            R.id.nav_about -> {
            }
        }
        return true
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = binding.drawerLayout
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            drawer.isDrawerOpen(GravityCompat.END) -> {
                drawer.closeDrawer(GravityCompat.END)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}
