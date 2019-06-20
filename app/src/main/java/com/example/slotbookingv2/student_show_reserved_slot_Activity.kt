package com.example.slotbookingv2

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class student_show_reserved_slot_Activity : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var slotList: MutableList<slotsData>
    lateinit var listview: ListView
    private val currentUser = FirebaseAuth.getInstance().currentUser
    var studentName = ""
    var studentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_show_reserved_slot)
        slotList = mutableListOf()
        listview = this.findViewById(R.id.reserved_slot_view)
        ref = FirebaseDatabase.getInstance().getReference("Slots")

        currentUser?.let { user ->

            val userNameRef = ref.parent?.child("users")?.orderByChild("email")?.equalTo(user.email)
            userNameRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(
                            this@student_show_reserved_slot_Activity,
                            "User Already Registered",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        for (e in dataSnapshot.children) {
                            val employee = e.getValue(Data::class.java)!!
                            studentName = employee.name
                            studentId = employee.studentId


                        }
                        var query = ref.child("Nikhil Nishad").orderByChild("studentId").equalTo(studentId)
                        query.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    slotList.clear()
                                    for (e in p0.children) {
                                        val employee = e.getValue(slotsData::class.java)
                                        var status = employee!!.status
                                        if (status == "B") {
                                            slotList.add(employee)
                                        }
                                    }
                                    val adapter = student_reserved_slot_adapter(
                                        this@student_show_reserved_slot_Activity,
                                        R.layout.s_r_s_adapter_list,
                                        slotList
                                    )
                                    listview.adapter = adapter
                                } else {
                                    Toast.makeText(
                                        this@student_show_reserved_slot_Activity,
                                        "user not found",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

        Toast.makeText(this, studentName, Toast.LENGTH_LONG).show()

    }
}



