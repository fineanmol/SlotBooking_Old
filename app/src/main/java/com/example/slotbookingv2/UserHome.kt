package com.example.slotbookingv2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserHome : AppCompatActivity() {
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
    }
}
