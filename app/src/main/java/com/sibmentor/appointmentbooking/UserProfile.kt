package com.sibmentor.appointmentbooking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfile : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        currentUser?.let { user ->
            text_email.text = user.email

            text_phone.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if (user.isEmailVerified) {
                text_not_verified.visibility = View.INVISIBLE
            } else {
                text_not_verified.visibility = View.VISIBLE
            }
        }

        button_save.setOnClickListener {

            val name = edit_text_name.text.toString().trim()

            if (name.isEmpty()) {
                edit_text_name.error = "name required"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            progressbar.visibility = View.VISIBLE

            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        this.toast("Profile Updated")
                    } else {
                        this.toast(task.exception?.message!!)
                    }
                }

        }
        text_not_verified.setOnClickListener {

            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        this.toast("Verification Email Sent")
                    } else {
                        this.toast(it.exception?.message!!)
                    }
                }

        }

        text_phone.setOnClickListener {
            startActivity(Intent(this, UserPhoneVerify::class.java))
        }

        text_email.setOnClickListener {
            startActivity(Intent(this, UserEmailUpdate::class.java))
        }

        text_password.setOnClickListener {
            startActivity(Intent(this, UserPassUpdate::class.java))
        }

    }
}
