package com.example.slotbookingv2

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.login() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.let { user ->
        val rootRef = FirebaseDatabase.getInstance().reference
        val userNameRef = rootRef.child("users").orderByChild("email").equalTo(user.email)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (e in dataSnapshot.children) {
                    val employee = e.getValue(Data::class.java)
                    if (employee != null) {
                        val u_type = employee.user_type.toString()
                        if (u_type == "S") startActivity(Intent(this@login, studentHomeActivity::class.java))
                        else startActivity(Intent(this@login, mentorHomeActivity::class.java))

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        userNameRef.addListenerForSingleValueEvent(eventListener)

    }
    val intent = Intent(this, UserHome::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}


fun Context.logout() {
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}
