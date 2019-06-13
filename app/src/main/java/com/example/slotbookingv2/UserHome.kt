package com.example.slotbookingv2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_home.*

class UserHome : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseUser
    lateinit var ref: DatabaseReference
    lateinit var employeeList: MutableList<Data>
    lateinit var listview: ListView
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)


        currentUser?.let { user ->
            val rootRef = FirebaseDatabase.getInstance().reference
            val userNameRef = rootRef.child("users").orderByChild("email").equalTo(user.email)
            val eventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (e in dataSnapshot.children) {
                        val employee = e.getValue(Data::class.java)
                        if (employee != null) {
                            val u_type = employee.user_type.toString()
                            if (u_type == "S") startActivity(Intent(this@UserHome, studentHomeActivity::class.java))
                            else startActivity(Intent(this@UserHome, mentorHomeActivity::class.java))

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            userNameRef.addListenerForSingleValueEvent(eventListener)

        }
        employeeList = mutableListOf()
        listview = findViewById(R.id.listview)
        ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    employeeList.clear()
                    for (e in p0.children) {
                        val employee = e.getValue(Data::class.java)

                        employeeList.add(employee!!)
                    }
                    /* val adapter = customAdapterc(this@UserHome, R.layout.listview_custom, employeeList)
                     listview.adapter = adapter*/
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        logoutbtn.setOnClickListener(
            View.OnClickListener {
                FirebaseAuth.getInstance().signOut()
                logout()

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
