package com.example.slotbookingv2

import android.R.id.message
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class mentorhomev2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val userref = FirebaseDatabase.getInstance().getReference("users")
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorhomev2)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Email your suggestion", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            val intent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "agarwal.anmol2004@gmail.com", null
                )
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements")
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mentorhomev2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_one) {
            startActivity(Intent(this, addSlotActivity::class.java))
            Toast.makeText(this, "Add Slot Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.action_two) {
            FirebaseAuth.getInstance().signOut()
            logout()
            Toast.makeText(this, "You have been logout Successfully", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.action_three) {
            Toast.makeText(this, "Item Three Clicked", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, AppointmentList2::class.java))
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
            R.id.nav_addslot -> {
                // Handle the camera action
                startActivity(Intent(this, addSlotActivity::class.java))
                Toast.makeText(this, "Add Slot Clicked", Toast.LENGTH_LONG).show()
            }
            R.id.show_appointment -> {
                startActivity(Intent(this, AppointmentList2::class.java))
                Toast.makeText(this, "Work in Progress", Toast.LENGTH_LONG).show()
            }

            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_reportbug -> {
                val intent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "agarwal.anmol2004@gmail.com", null
                    )
                )
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements")
                intent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(Intent.createChooser(intent, "Choose an Email client :"))

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        /*  user value display at drawer*/
        /* var namedrawer = findViewById<TextView>(R.id.namedrawer)
         var emaildrawer = findViewById<TextView>(R.id.emaildrawer)
         currentUser?.let { user ->
             // Toast.makeText(mCtx, user.email, Toast.LENGTH_LONG).show()
             val userNameRef = userref.parent?.child("users")?.orderByChild("email")?.equalTo(user.email)
             val eventListener = object : ValueEventListener {
                 override fun onDataChange(dataSnapshot: DataSnapshot) {
                     if (!dataSnapshot.exists()) {
                         //create new user
                         Toast.makeText(this@mentorhomev2, "User details not found", Toast.LENGTH_LONG).show()
                     } else {
                         for (e in dataSnapshot.children) {
                             val employee = e.getValue(Data::class.java)
                             var studentName = employee?.name
                             var studentemail = employee?.email
                             namedrawer.text= studentName!!.trim()
                             emaildrawer.text= studentemail!!.trim()

                         }
                     }
                 }

                 override fun onCancelled(databaseError: DatabaseError) {
                 }
             }
             userNameRef?.addListenerForSingleValueEvent(eventListener)

         }*/
// end of method
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
