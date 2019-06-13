package com.example.slotbookingv2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_signup.*

class UserSignup : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users")

        u_register.setOnClickListener {
            val email = u_r_email.text.toString().trim()
            val password = u_r_pass.text.toString().trim()
            val name= numberupdate.text.toString().trim()
            val number =mobile.text.toString().trim()
            val studentid = studentid.text.toString().trim()
            val status = (" ").toString().trim()
            val flag1 = (" ").toString().trim()
            val flag2 = (" ").toString().trim()

            if (email.isEmpty()) {
                u_r_email.error = "Email Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                u_r_email.error = "Name Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                u_r_email.error = "Number Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (studentid.isEmpty()) {
                u_r_email.error = "Student-Id Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                u_r_email.error = "Valid Email Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                u_r_pass.error = "6 char password required"
                u_r_pass.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password, name, number, studentid, status, flag1, flag2)

        }

        u_r_login.setOnClickListener {
            startActivity(Intent(this@UserSignup, UserHome::class.java))
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        name: String,
        number: String,
        dob: String,
        status: String,
        flag1: String,
        flag2: String
    ) {
        progressbar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE
                if (task.isSuccessful) {
                    addUser(email, password, name, number, dob, status, flag1, flag2)
                    login()
                } else {
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }

    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            login()
        }
    }

    private fun addUser(
        email: String,
        password: String,
        name: String,
        number: String,
        studentId: String,
        status: String,
        flag1: String,
        flag2: String
    ) {


        ref = FirebaseDatabase.getInstance().reference
        val userId= (ref.push().key).toString()
        val addUser = Data(userId, email, password, name, number, studentId, status, flag1, flag2)


        ref.child("users").child(userId).setValue(addUser)
        Toast.makeText(this,"Registration Successful", Toast.LENGTH_LONG).show()



    }

}
