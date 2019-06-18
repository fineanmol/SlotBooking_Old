package com.example.slotbookingv2

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
    lateinit var slotList: MutableList<slotsData>
    lateinit var listview: ListView
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        slotList = mutableListOf()
        listview = findViewById(R.id.listview)
        ref = FirebaseDatabase.getInstance().getReference("Slots").child("Nikhil Nishad")
        var query = ref.orderByChild("status").equalTo("NB")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    slotList.clear()
                    for (e in p0.children) {
                        val employee = e.getValue(slotsData::class.java)

                        slotList.add(employee!!)
                    }
                    val adapter = customAdapter(this@UserHome, R.layout.listview_custom, slotList)
                    listview.adapter = adapter
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