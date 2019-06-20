package com.example.slotbookingv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.android.synthetic.main.dialog_otp_verification.*
import java.util.concurrent.TimeUnit


class UserSignup : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference
    var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users")

        u_register.setOnClickListener {
            val email = u_r_email.text.toString().trim()
            val password = u_r_pass.text.toString().trim()
            val namef = name.text.toString().trim()
            val number =mobile.text.toString().trim()
            val studentidf = studentid.text.toString().trim()
            val status = ("NB")
            val user_type = ("S")
            //val flag2 = (" ").toString().trim()


            if (namef.isEmpty()) {
                name.error = "Name Required"
                name.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                u_r_email.error = "Email Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (number.isEmpty()) {
                mobile.error = "Number Required"
                mobile.requestFocus()
                return@setOnClickListener
            }
            if (studentidf.isEmpty()) {
                studentid.error = "Student-Id Required"
                studentid.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                u_r_email.error = "Valid Email Required"
                u_r_email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.PHONE.matcher(number).matches() || number.length > 10 || number.length < 9) {
                mobile.error = "Valid Phone Number Required"
                mobile.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6) {
                u_r_pass.error = "6 char password required"
                u_r_pass.requestFocus()
                return@setOnClickListener
            }

            showCreateCategoryDialog(email, password, namef, number, studentidf, status, user_type)



        }

        u_r_login.setOnClickListener {
            startActivity(Intent(this@UserSignup, MainActivity::class.java))
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        name: String,
        number: String,
        dob: String,
        status: String,
        user_type: String
    ) {


        progressbar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE
                if (task.isSuccessful) {
                    addUser(email, password, name, number, dob, status, user_type)
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
        user_type: String
    ) {


        ref = FirebaseDatabase.getInstance().reference
        val userId= (ref.push().key).toString()
        val addUser = Data(userId, email, password, name, number, studentId, status, user_type)


        ref.child("users").child(userId).setValue(addUser)
        Toast.makeText(this,"Registration Successful", Toast.LENGTH_LONG).show()



    }


    fun showCreateCategoryDialog(
        email: String,
        password: String,
        namef: String,
        number: String,
        studentidf: String,
        status: String,
        user_type: String
    ) {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Phone Number Verification")

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val view = layoutInflater.inflate(R.layout.dialog_otp_verification, null)

        val categoryEditText = view.findViewById(R.id.editOTP) as EditText
        val vfbutton = view.findViewById<Button>(R.id.get_otp_btn)
        val vreg = view.findViewById<Button>(R.id.vregister)



        vfbutton.setOnClickListener {
            Log.d("xcv", "clicked")
            var phone = "+91" + mobile.text.toString().trim()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                callbacks
            ) // OnVerificationStateChangedCallbacks
        }


        vreg.setOnClickListener {
            val code = editOTP.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success")
                        registerUser(email, password, namef, number, studentidf, status, user_type)

                        val user = task.result?.user
                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                }
        }



        builder.setView(view);

        builder.show();
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("TAG", "onVerificationCompleted:$credential")

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("TAG", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
            verificationId: String?,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("TAG", "onCodeSent:" + verificationId!!)

            // Save verification ID and resending token so we can use them later
            //storedVerificationId = verificationId
            //resendToken = token

            // ...
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val email = u_r_email.text.toString().trim()
                    val password = u_r_pass.text.toString().trim()
                    val namef = name.text.toString().trim()
                    val number = mobile.text.toString().trim()
                    val studentidf = studentid.text.toString().trim()
                    val status = ("NB")
                    val user_type = ("S")
                    registerUser(email, password, namef, number, studentidf, status, user_type)


                    val user = task.result?.user
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }



}