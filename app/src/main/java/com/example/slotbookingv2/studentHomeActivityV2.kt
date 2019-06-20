package com.example.slotbookingv2

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class UserHomeV2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_v2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            val intent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "agarwal.anmol2004@gmail.com", null
                )
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements")
            intent.putExtra(Intent.EXTRA_TEXT, "Hi\n I would like to inform you that")
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_home_v2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_logout) {

            logout()

            return true
        }
        if (id == R.id.action_three) {
            Toast.makeText(this, "Item Three Clicked", Toast.LENGTH_LONG).show()

            return true
        }
        if (id == R.id.contactUs) {
            Toast.makeText(this, "You click contact us", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, AboutDeveloper::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_showslots -> {
                // Handle the camera action
                var intent = Intent(this, student_show_reserved_slot_Activity::class.java)
                startActivity(intent)
            }
            R.id.nav_bookslots -> {
                var intent = Intent(this, UserHome::class.java)
                startActivity(intent)
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_share -> {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody =
                    "Hey \n Slot Booking Application is a fast,simple and secure app that I use to book my slot with Mentor and Manage all the data.\n\n Get it for free at\n App link "
                sharingIntent.putExtra(
                    android.content.Intent.EXTRA_SUBJECT,
                    "Slot Booking Management : Android Application"
                )
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))

            }
            R.id.nav_logout -> {
                logout()
            }
            R.id.nav_ContactUs -> {
                val intent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "agarwal.anmol2004@gmail.com", null
                    )
                )
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements")
                intent.putExtra(Intent.EXTRA_TEXT, android.R.id.message)
                startActivity(Intent.createChooser(intent, "Choose an Email client :"))


            }
            R.id.nav_AboutDeveloper -> {

                startActivity(Intent(this, AboutDeveloper::class.java))
            }

        }
        val drawerLayout: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey()

            //moveTaskToBack(false);

            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    protected fun exitByBackKey() {

        val alertbox = AlertDialog.Builder(this)
            .setMessage("Do you want to exit application?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { arg0, arg1 ->
                // do something when the button is clicked
                finishAffinity()

            })
            .setNegativeButton("No", // do something when the button is clicked
                DialogInterface.OnClickListener { arg0, arg1 -> })
            .show()

    }
}
