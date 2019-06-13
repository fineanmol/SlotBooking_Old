package com.example.slotbookingv2

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHome : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        logoutbtn.setOnClickListener(
            View.OnClickListener {
                FirebaseAuth.getInstance().signOut()
                logout()
                /*var Intent = Intent(this,MainActivity::class.java)
                startActivity(Intent)*/
            }
        )

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.logoutbtn) {

            AlertDialog.Builder(this).apply {
                setTitle("Are you sure?")
                setPositiveButton("Yes") { _, _ ->

                    FirebaseAuth.getInstance().signOut()
                    logout()

                }
                setNegativeButton("Cancel") { _, _ ->
                }
            }.create().show()

        }
        return super.onOptionsItemSelected(item)
    }
}
