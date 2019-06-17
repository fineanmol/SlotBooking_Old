package com.example.slotbookingv2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class customAdapter2(val mCtx: Context, val layoutId: Int, val slotList: List<slotsData>) :
    ArrayAdapter<slotsData>(mCtx, layoutId, slotList) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val ref = FirebaseDatabase.getInstance().getReference("Slots")
    val userref = FirebaseDatabase.getInstance().getReference("users")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        val name = view.findViewById<TextView>(R.id.student_Name)
        val date = view.findViewById<TextView>(R.id.dateslot)
        val number = view.findViewById<TextView>(R.id.studentno)
        val studentId = view.findViewById<TextView>(R.id.studentid)
        val slotTiming = view.findViewById<TextView>(R.id.slot_timing)

        //val book = view.findViewById<TextView>(R.id.bookbtn)
        //val deleteBtn = view.findViewById<TextView>(R.id.delete)

        val slot = slotList[position]

        name.text = slot.reserved_by
        date.text = slot.date.split("/").first().toString()
        number.text = slot.studentNumber
        studentId.text = slot.studentId
        slotTiming.text = (slot.begins_At + ("-").toString() + slot.stop_At)

        return view
    }
}

