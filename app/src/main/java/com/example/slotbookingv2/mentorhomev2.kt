package com.example.slotbookingv2

import android.R.id.message
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.mikepenz.iconics.IconicsColor.Companion.colorRes
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize.Companion.dp
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.example.slotbookingv2.drawerItems.CustomPrimaryDrawerItem
import com.example.slotbookingv2.drawerItems.CustomUrlPrimaryDrawerItem
import com.example.slotbookingv2.drawerItems.OverflowMenuDrawerItem
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile


class mentorhomev2 : AppCompatActivity() {
    val userref = FirebaseDatabase.getInstance().getReference("users")
    val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer

    private lateinit var profile: IProfile<*>
    private lateinit var profile2: IProfile<*>
    private lateinit var profile3: IProfile<*>
    private lateinit var profile4: IProfile<*>
    private lateinit var profile5: IProfile<*>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentorhomev2)


        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.drawer_item_advanced_drawer)

        // Create a few sample profile
        profile = ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(resources.getDrawable(R.drawable.profile))
        profile2 = ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(resources.getDrawable(R.drawable.profile2)).withIdentifier(2)
        profile3 = ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(resources.getDrawable(R.drawable.profile3))
        profile4 = ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(resources.getDrawable(R.drawable.profile4)).withIdentifier(4)
        profile5 = ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(resources.getDrawable(R.drawable.profile5))

        // Create the AccountHeader
        buildHeader(false, savedInstanceState)

        //Create the drawer
        result = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
            .addDrawerItems(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                //here we use a customPrimaryDrawerItem we defined in our sample app
                //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                OverflowMenuDrawerItem().withName(R.string.drawer_item_menu_drawer_item).withDescription(R.string.drawer_item_menu_drawer_item_desc).withMenu(R.menu.fragment_menu).withOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    Toast.makeText(this@mentorhomev2, item.title, Toast.LENGTH_SHORT).show()
                    false
                }).withIcon(GoogleMaterial.Icon.gmd_filter_center_focus),
                CustomPrimaryDrawerItem().withBackgroundRes(R.color.accent).withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withDescription("This is a description").withIcon(FontAwesome.Icon.faw_eye),
                CustomUrlPrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"),
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cart_plus),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_database).withEnabled(false),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withSelectedIconColor(Color.RED).withIconTintingEnabled(true).withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().padding(dp(5)).color(colorRes(R.color.material_drawer_dark_primary_text))).withTag("Bullhorn"),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false)
            ) // add the items we want to use with our Drawer
            .withOnDrawerNavigationListener(object : Drawer.OnDrawerNavigationListener {
                override fun onNavigationClickListener(clickedView: View): Boolean {
                    //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                    //if the back arrow is shown. close the activity
                    this@mentorhomev2.finish()
                    //return true if we have consumed the event
                    return true
                }
            })
            .addStickyDrawerItems(
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(10),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github)
            )
            .withSavedInstance(savedInstanceState)
            .build()
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


    }

    private fun buildHeader(compact: Boolean, savedInstanceState: Bundle?) {
        // Create the AccountHeader
        headerResult = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .withCompactStyle(compact)
            .addProfiles(
                profile,
                profile2,
                profile3,
                profile4,
                profile5,
                //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().padding(dp(5)).color(colorRes(R.color.material_drawer_dark_primary_text))).withIdentifier(PROFILE_SETTING.toLong()),
                ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
            )
            .withTextColor(ContextCompat.getColor(this, R.color.material_drawer_dark_primary_text))
            .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
                    //sample usage of the onProfileChanged listener
                    //if the clicked item has the identifier 1 add a new profile ;)
                    if (profile is IDrawerItem<*> && (profile as IDrawerItem<*>).identifier == PROFILE_SETTING.toLong()) {
                        val newProfile = ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(resources.getDrawable(R.drawable.profile5))

                        val profiles = headerResult.profiles
                        if (profiles != null) {
                            //we know that there are 2 setting elements. set the new profile above them ;)
                            headerResult.addProfile(newProfile, profiles.size - 2)
                        } else {
                            headerResult.addProfiles(newProfile)
                        }
                    }

                    //false if you have not consumed the event and it should close the drawer
                    return false
                }
            })
            .withSavedInstance(savedInstanceState)
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.menu_1 -> {
                //update the profile2 and set a new image.
                profile2.withIcon(IconicsDrawable(this, GoogleMaterial.Icon.gmd_android).backgroundColor(colorRes(R.color.accent)).size(dp(48)).padding(dp(4)))
                headerResult.updateProfileByIdentifier(profile2)
                return true
            }
            R.id.menu_2 -> {
                //show the arrow icon
                result.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                return true
            }
            R.id.menu_3 -> {
                //show the hamburger icon
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                result.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
                return true
            }
            R.id.menu_4 -> {
                //we want to replace our current header with a compact header
                //build the new compact header
                buildHeader(true, null)
                //set the view to the result
                result.header = headerResult.view
                //set the drawer to the header (so it will manage the profile list correctly)
                headerResult.setDrawer(result)
                return true
            }
            R.id.menu_5 -> {
                //we want to replace our current header with a normal header
                //build the new compact header
                buildHeader(false, null)
                //set the view to the result
                result.header = headerResult.view
                //set the drawer to the header (so it will manage the profile list correctly)
                headerResult.setDrawer(result)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        if (::result.isInitialized) {
            outState = result.saveInstanceState(outState)
        }
        //add the values which need to be saved from the accountHeader to the bundle
        if (::headerResult.isInitialized) {
            outState = headerResult.saveInstanceState(outState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val PROFILE_SETTING = 1
    }



}
