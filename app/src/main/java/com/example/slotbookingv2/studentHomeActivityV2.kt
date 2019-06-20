package com.example.slotbookingv2

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.slotbookingv2.drawerItems.CustomPrimaryDrawerItem
import com.example.slotbookingv2.drawerItems.CustomUrlPrimaryDrawerItem
import com.example.slotbookingv2.drawerItems.OverflowMenuDrawerItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile

class UserHomeV2 : AppCompatActivity() {
    private lateinit var headerResult: AccountHeader
    private lateinit var result: Drawer

    private lateinit var profile: IProfile<*>
    private lateinit var profile2: IProfile<*>
    private lateinit var profile3: IProfile<*>
    private lateinit var profile4: IProfile<*>
    private lateinit var profile5: IProfile<*>
    private var Name: String = "Anmol"
    private var Email: String = "test@gmail.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_v2)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
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
        var Names = Name

        // Create a few sample profile
        profile =
            ProfileDrawerItem().withName(Name).withEmail(Email).withIcon(resources.getDrawable(R.drawable.profile))


        // Create the AccountHeader
        buildHeader(false, savedInstanceState)

        //Create the drawer
        result = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
            .addDrawerItems(
                PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withOnDrawerItemClickListener(
                    object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                            startActivity(Intent(this@UserHomeV2, mentorhomev2::class.java))
                            Log.d("TAGDDD", "clicked")
                            return false
                        }
                    }),
                //here we use a customPrimaryDrawerItem we defined in our sample app
                //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                OverflowMenuDrawerItem().withName(R.string.drawer_item_menu_drawer_item).withDescription(R.string.drawer_item_menu_drawer_item_desc).withOnDrawerItemClickListener(
                    object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                            Log.d("TAGDDD", "clicked")
                            return false
                        }
                    }).withMenu(
                    R.menu.fragment_menu
                ).withOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                    if (item.itemId == R.id.newSession) {
                        val builder = AlertDialog.Builder(this@UserHomeV2)

                        // Set the alert dialog title
                        builder.setTitle("New Session Confirmation")

                        // Display a message on alert dialog
                        builder.setMessage("Are you sure to restart session? \n Now Users can book slots !!")

                        // Set a positive button and its click listener on alert dialog
                        builder.setPositiveButton("YES") { dialog, which ->
                            // Do something when user press the positive button
                        }

                        // Display a negative button on alert dialog
                        builder.setNegativeButton("No") { dialog, which ->
                            Toast.makeText(
                                applicationContext,
                                "Not excited to Create New Session ?",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    if (item.itemId == R.id.oldSession) {
                        startActivity(Intent(this@UserHomeV2, mentorShowSlotActivity::class.java))
                    }
                    false
                }).withIcon(GoogleMaterial.Icon.gmd_filter_center_focus),
                CustomPrimaryDrawerItem().withBackgroundRes(R.color.accent).withName("About Developer")
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                            startActivity(Intent(this@UserHomeV2, AboutDeveloper::class.java))
                            return false
                        }
                    }).withIcon(
                        FontAwesome.Icon.faw_gamepad
                    )
                /*.withOnDrawerItemClickListener(
            Drawer.OnDrawerItemClickListener({
                startActivity(Intent(this,AboutDeveloper::class.java))
            })


                )*/,
                PrimaryDrawerItem().withName(R.string.drawer_item_custom).withDescription("Check Appointment Today onwards").withOnDrawerItemClickListener(
                    object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                            Log.d("TAGDDD", "clicked")
                            startActivity(Intent(this@UserHomeV2, mentorShowSlotActivity::class.java))
                            return false
                        }
                    }).withIcon(
                    FontAwesome.Icon.faw_eye
                ),
                CustomUrlPrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(
                    "https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"
                )/*.withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener{
                     fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                         var intent= Intent(this,addSlotActivity::class.java)
                         startActivity(intent)
                         return true
                     }
true
                })*/,
                SectionDrawerItem().withName(R.string.drawer_item_section_header),
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cart_plus),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_database).withEnabled(
                    false
                ),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                SecondaryDrawerItem().withName(R.string.drawer_item_contact).withSelectedIconColor(Color.RED).withIconTintingEnabled(
                    true
                ).withIcon(
                    IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().padding(IconicsSize.dp(5)).color(
                        IconicsColor.colorRes(
                            R.color.material_drawer_dark_primary_text
                        )
                    )
                ).withTag("Bullhorn"),
                SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(
                    false
                )
            ) // add the items we want to use with our Drawer
            .withOnDrawerNavigationListener(object : Drawer.OnDrawerNavigationListener {
                override fun onNavigationClickListener(clickedView: View): Boolean {
                    //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                    //if the back arrow is shown. close the activity
                    this@UserHomeV2.finish()
                    //return true if we have consumed the event
                    return true
                }
            })
            .addStickyDrawerItems(
                SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(
                    10
                ),
                SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github)
            )
            .withSavedInstance(savedInstanceState)
            .build()


    }

    private fun buildHeader(compact: Boolean, savedInstanceState: Bundle?) {
        // Create the AccountHeader
        headerResult = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .withCompactStyle(compact)
            .addProfiles(
                profile,
                ProfileSettingDrawerItem().withName("Logout"),
                ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
            )
            .withTextColor(ContextCompat.getColor(this, R.color.material_drawer_dark_primary_text))
            .withOnAccountHeaderListener(object : AccountHeader.OnAccountHeaderListener {
                override fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean {
                    //sample usage of the onProfileChanged listener
                    //if the clicked item has the identifier 1 add a new profile ;)
                    if (profile is IDrawerItem<*> && (profile as IDrawerItem<*>).identifier == UserHomeV2.PROFILE_SETTING.toLong()) {
                        val newProfile =
                            ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com")
                                .withIcon(resources.getDrawable(R.drawable.profile5))

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
        inflater.inflate(R.menu.menulogout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.contactUs -> {
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
                Toast.makeText(this, "You click contact us", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, AboutDeveloper::class.java))

                return true
            }
            R.id.action_logout -> {
                logout()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
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

    companion object {
        private const val PROFILE_SETTING = 1
    }
}
