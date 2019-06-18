package com.example.slotbookingv2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class AppointmentList2 : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseUser
    lateinit var ref: DatabaseReference
    lateinit var slotList: MutableList<slotsData>
    lateinit var listview: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_list2)
        slotList = mutableListOf()
        listview = this.findViewById(R.id.appointmentList)
        ref = FirebaseDatabase.getInstance().getReference("Slots").child("Nikhil Nishad")
        ref.addValueEventListener(object : ValueEventListener {
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
                    val adapter = customAdapter2(this@AppointmentList2, R.layout.showappointmentvalue, slotList)
                    listview.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }
}



