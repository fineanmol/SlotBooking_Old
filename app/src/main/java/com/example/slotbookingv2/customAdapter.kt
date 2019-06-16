package com.example.slotbookingv2

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class customAdapterc(val mCtx: Context, val layoutId: Int, val employeeList: List<slotsData>) :
    ArrayAdapter<slotsData>(mCtx, layoutId, employeeList) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    lateinit var name: TextView
    lateinit var dateslot: TextView
    lateinit var timeslot: TextView
    lateinit var bookbtn: Button
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        name = view.findViewById<TextView>(R.id.mentor_Name)
        timeslot = view.findViewById<TextView>(R.id.slot_timing)
        dateslot = view.findViewById<TextView>(R.id.dateslot)
        bookbtn = view.findViewById<Button>(R.id.bookbtn)
      //  var newlist= employeeList

        val employee = employeeList[position]
        name.text= employee.generated_by
        timeslot.text = (employee.begins_At +("-").toString()+ employee.stop_At)
        dateslot.text= employee.date.split("/").first().toString()


        currentUser?.let { user ->
            val rootRef = FirebaseDatabase.getInstance().reference
            val userNameRef = rootRef.child("users").orderByChild("email").equalTo(user.email)
            val eventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (e in dataSnapshot.children) {
                        val employee = e.getValue(Data::class.java)
                        if (employee != null) {
                            val status = employee.status.toString()
                            if (status == "NB") {
                                bookbtn.setOnClickListener {

                                    // status set to be booked here
                                    val myDatabase = FirebaseDatabase.getInstance().getReference("Timeslots")
                                    val name = name.text.toString().trim()
                                    val status = "Booked".toString().trim()
                                    val timeslot = timeslot.text.toString().trim()
                                    val dateslot = dateslot.text.toString().trim()


                                    val employee = BookedData(name, status, dateslot, timeslot)
                                   // myDatabase.child("").setValue(employee)  //To save value in database
                                    Toast.makeText(mCtx, "Updated :) ", Toast.LENGTH_LONG).show()
                                }

                            }
                            if (status == "Booked") {
                                bookbtn.isClickable = false
                                bookbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.Black))

                            }

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            userNameRef.addListenerForSingleValueEvent(eventListener)

        }

        return view
    }


}

