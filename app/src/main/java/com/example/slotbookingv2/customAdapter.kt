package com.example.slotbookingv2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class customAdapterc(val mCtx: Context, val layoutId: Int, val employeeList: List<Data>) :
    ArrayAdapter<Data>(mCtx, layoutId, employeeList) {

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


        val employee = employeeList[position]



        bookbtn.setOnClickListener {

            // status set to be booked here
            val myDatabase = FirebaseDatabase.getInstance().getReference("users")
            val name = name.text.toString().trim()
            val status = "Booked".toString().trim()
            val timeslot = timeslot.text.toString().trim()
            val dateslot = dateslot.text.toString().trim()

            val employee = BookedData(name, status, dateslot, timeslot)
            myDatabase.child("").setValue(employee)
            Toast.makeText(mCtx, "Updated :) ", Toast.LENGTH_LONG).show()
        }
        return view
    }


}

