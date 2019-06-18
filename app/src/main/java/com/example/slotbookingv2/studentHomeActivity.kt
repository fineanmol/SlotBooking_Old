package com.example.slotbookingv2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_student_home.*

class studentHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        logoutbtn.setOnClickListener(
            View.OnClickListener {
                FirebaseAuth.getInstance().signOut()
                logout()

            }
        )
        bookslot.setOnClickListener(View.OnClickListener {
            var Intent = Intent(this, UserHome::class.java)
            startActivity(Intent)
        })

        show_showreserved_slot_btn.setOnClickListener {
            //   Toast.makeText(this@studentHomeActivity, "Clicked!", Toast.LENGTH_SHORT).show()
            var Intent = Intent(this, student_show_reserved_slot_Activity::class.java)
            startActivity(Intent)

        }
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