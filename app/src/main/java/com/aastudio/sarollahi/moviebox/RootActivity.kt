/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

package com.aastudio.sarollahi.moviebox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.aastudio.sarollahi.common.util.FirebaseUtils.firebaseAuth
import com.aastudio.sarollahi.moviebox.databinding.ActivityRootBinding
import com.aastudio.sarollahi.moviebox.databinding.ViewAboutBinding
import com.aastudio.sarollahi.moviebox.ui.authentication.AuthActivity
import com.aastudio.sarollahi.moviebox.ui.contact.ContactActivity
import com.aastudio.sarollahi.moviebox.ui.person.PersonActivity
import com.aastudio.sarollahi.moviebox.ui.search.SearchActivity
import com.aastudio.sarollahi.moviebox.ui.watchList.WatchListActivity
import com.aastudio.sarollahi.moviebox.util.FcmTokenRegistrationService
import com.aastudio.sarollahi.moviebox.util.createDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class RootActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityRootBinding
    private var drawerLayout: DrawerLayout? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

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

        (binding.navViewRight.menu.findItem(R.id.navMovieSearch).actionView as Button).setOnClickListener {
            searchByName()
        }

        (binding.navViewRight.menu.findItem(R.id.navYear).actionView as EditText).setOnEditorActionListener(
            object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchByName()
                        return true
                    }
                    return false
                }
            })

        (binding.navViewRight.menu.findItem(R.id.navPersonSearch).actionView as Button).setOnClickListener {
            searchPerson()
        }

        (binding.navViewRight.menu.findItem(R.id.navPerson).actionView as EditText).setOnEditorActionListener(
            object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchPerson()
                        return true
                    }
                    return false
                }
            })
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
        when (item.itemId) {
            R.id.nav_auth -> {
                drawerLayout?.closeDrawer(GravityCompat.START)
                startActivity(Intent(this, AuthActivity::class.java))
            }
            R.id.nav_movies -> {
                switchView(R.id.nav_root)
            }
            R.id.nav_tv_shows -> {
                switchView(R.id.nav_root_tv)
            }
            R.id.nav_watch_list -> {
                if (firebaseAuth.currentUser != null) {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, WatchListActivity::class.java))
                } else {
                    createDialog(
                        this,
                        getString(R.string.access_denied),
                        getString(R.string.access_denied_message),
                        {
                            startActivity(Intent(this, AuthActivity::class.java))
                            it.dismiss()
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                        },
                        {
                            it.dismiss()
                        }
                    )
                }
            }
            R.id.nav_download -> {
            }
            R.id.nav_updates -> {
            }
            R.id.nav_br -> {
            }
            R.id.nav_contact -> {
                if (firebaseAuth.currentUser != null) {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, ContactActivity::class.java))
                } else {
                    createDialog(
                        this,
                        getString(R.string.access_denied),
                        getString(R.string.access_denied_message),
                        {
                            startActivity(Intent(this, AuthActivity::class.java))
                            it.dismiss()
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                        },
                        {
                            it.dismiss()
                        }
                    )
                }
            }
            R.id.nav_about -> {
                showAbout()
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

        if (firebaseAuth.currentUser != null) {
            binding.navView.menu.findItem(R.id.nav_auth).title = getString(R.string.sign_out)
            binding.navView.menu.findItem(R.id.nav_auth).setOnMenuItemClickListener {
                firebaseAuth.signOut()
                recreate()
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun sendFcmRegistrationToken() {
        val intent = Intent(this, FcmTokenRegistrationService::class.java)
        startService(intent)
    }

    private fun switchView(id: Int) {
        val navHostFragment = binding.appBarMain.contentMain.navHostFragmentContentMain
        val graphInflater = navHostFragment.findNavController().navInflater
        navGraph = graphInflater.inflate(R.navigation.main_navigation)
        navController = navHostFragment.findNavController()
        navGraph.startDestination = id
        navController.graph = navGraph
        drawerLayout?.closeDrawer(GravityCompat.START)
    }

    private fun showAbout() {
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
            } catch (e: ActivityNotFoundException) {
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

    private fun searchByName() {
        val title =
            (binding.navViewRight.menu.findItem(R.id.navTitle).actionView as EditText).text
        val year =
            (binding.navViewRight.menu.findItem(R.id.navYear).actionView as EditText).text
        if (title.toString().trim() != "") {
            title.replace("\\s".toRegex(), "%20")

            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(SearchActivity.TITLE, title.toString())
            intent.putExtra(SearchActivity.YEAR, year.toString())
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.END)
        }
    }

    private fun searchPerson() {
        val name =
            (binding.navViewRight.menu.findItem(R.id.navPerson).actionView as EditText).text
        if (name.toString().trim() != "") {
            name.replace("\\s".toRegex(), "%20")

            val intent = Intent(this, PersonActivity::class.java)
            intent.putExtra(PersonActivity.NAME, name.toString())
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.END)
        }
    }
}
