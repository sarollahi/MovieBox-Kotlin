/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.aastudio.sarollahi.moviebox.databinding.ActivityRootBinding
import com.aastudio.sarollahi.moviebox.databinding.ViewAboutBinding
import com.aastudio.sarollahi.moviebox.ui.contact.ContactActivity
import com.aastudio.sarollahi.moviebox.ui.movie.RootMovieFragment
import com.aastudio.sarollahi.moviebox.ui.tv.RootTVFragment
import com.aastudio.sarollahi.moviebox.util.FcmTokenRegistrationService
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class RootActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRootBinding
    private var drawerLayout: DrawerLayout? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

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
                drawerLayout?.closeDrawer(GravityCompat.START)
                startActivity(Intent(this, ContactActivity::class.java))
            }
            R.id.nav_about -> {
                val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .create()
                val view: ViewAboutBinding = ViewAboutBinding.inflate(
                    LayoutInflater.from(this),
                    null,
                    false
                )
                view.appVersion.text =
                    getString(R.string.dialog_app_version_prefix, BuildConfig.VERSION_NAME)
                builder.setView(view.root)
                view.playStoreIcon.setOnClickListener {
                    val appPackageName =
                        packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.market_url, appPackageName))
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.play_url, appPackageName))
                            )
                        )
                    }
                }
                view.txtInfoRate.setOnClickListener {
                    val appPackageName =
                        packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.market_url, appPackageName))
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.play_url, appPackageName))
                            )
                        )
                    }
                }
                builder.setCanceledOnTouchOutside(true)
                builder.show()
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

    override fun onResume() {
        super.onResume()
        sendFcmRegistrationToken()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun sendFcmRegistrationToken() {
        val intent = Intent(this, FcmTokenRegistrationService::class.java)
        startService(intent)
    }
}
