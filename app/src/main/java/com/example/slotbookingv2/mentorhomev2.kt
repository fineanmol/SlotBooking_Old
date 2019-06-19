package com.example.slotbookingv2

import android.R.id.message
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.content_mentorhomev2.*


class mentorhomev2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val userref = FirebaseDatabase.getInstance().getReference("users")
    val currentUser = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorhomev2)
        /*  user value display at drawer*/
        /* var namedrawer = findViewById<TextView>(R.id.namedrawer)
         var emaildrawer = findViewById<TextView>(R.id.emaildrawer)
         // namedrawer.text = currentUser!!.displayName
         // emaildrawer.text="anmol"
          currentUser?.let { user ->
              // Toast.makeText(mCtx, user.email, Toast.LENGTH_LONG).show()
              val userNameRef = userref.parent?.child("users")?.orderByChild("email")?.equalTo(user.email)
              val eventListener = object : ValueEventListener {
                  override fun onDataChange(dataSnapshot: DataSnapshot) = if (!dataSnapshot.exists()) {
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

                  override fun onCancelled(databaseError: DatabaseError) {
                  }
              }
              userNameRef?.addListenerForSingleValueEvent(eventListener)

          }*/
// end of method

        new_session_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this@mentorhomev2)

            // Set the alert dialog title
            builder.setTitle("New Session Confirmation")

            // Display a message on alert dialog
            builder.setMessage("Are you sure to restart session? \n Now Users can book slots !!")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES") { dialog, which ->
                // Do something when user press the positive button
                val userNameRef = userref.orderByChild("user_type").equalTo("S")
                val eventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            //create new user
                            Toast.makeText(this@mentorhomev2, "Slots are ready for booking", Toast.LENGTH_LONG).show()
                        } else {
                            for (e in dataSnapshot.children) {
                                val employee = e.getValue(Data::class.java)
                                var studentkey = employee?.id
                                userref.child(studentkey!!).child("status").setValue("NB")
                                Toast.makeText(
                                    applicationContext,
                                    "Ok, Things are Ready!!  Generate Slots.",
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
                var addintent = Intent(this, addSlotActivity::class.java)
                startActivity(addintent)
                userNameRef.addListenerForSingleValueEvent(eventListener)
                // Change the app background color
            }


            // Display a negative button on alert dialog
            builder.setNegativeButton("No") { dialog, which ->
                Toast.makeText(applicationContext, "Not excited to Create New Session ?", Toast.LENGTH_SHORT).show()
            }


            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "You cancelled the Prompt", Toast.LENGTH_SHORT).show()
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()
        }


        existing_session_btn.setOnClickListener {
            startActivity(Intent(this@mentorhomev2, mentorShowSlotActivity::class.java))
        }


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


        /*Firebase Messaging*/
        /* FirebaseInstanceId.getInstance().instanceId
             .addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     Log.w(TAG, "getInstanceId failed", task.exception)
                     return@OnCompleteListener
                 }

                 // Get new Instance ID token
                 val token = task.result?.token

                 // Log and toast
                 val msg = getString(R.string.msg_token_fmt, token)
                 Log.d(TAG, msg)
                 Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
             })*/
        /*Firebase Messaging Ends*/

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
        menuInflater.inflate(R.menu.menulogout, menu)
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
