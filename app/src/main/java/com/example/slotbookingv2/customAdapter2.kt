package com.example.slotbookingv2

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class customAdapter2(val mCtx: Context, val layoutId: Int, val slotList: List<slotsData>) :
    ArrayAdapter<slotsData>(mCtx, layoutId, slotList) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val ref = FirebaseDatabase.getInstance().getReference("Slots")
    val userref = FirebaseDatabase.getInstance().getReference("users")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        val name = view.findViewById<TextView>(R.id.mentor_Name)
        val date = view.findViewById<TextView>(R.id.dateslot)
        val month = view.findViewById<TextView>(R.id.monthslot)
        val studentId = view.findViewById<TextView>(R.id.mentorid)
        val slotTiming = view.findViewById<TextView>(R.id.slot_timing)

        //val book = view.findViewById<TextView>(R.id.bookbtn)
        //val deleteBtn = view.findViewById<TextView>(R.id.delete)

        val slot = slotList[position]

        var temp_date = slot.date.split("/").first().toInt()
        var temp_month = slot.date.split("/")[1].toInt()
        var temp_year = slot.date.split("/").last().toInt()

        val calendar1 = Calendar.getInstance()
        calendar1.set(Calendar.YEAR, temp_year)
        calendar1.set(Calendar.MONTH, temp_month)
        calendar1.set(Calendar.DATE, temp_date)
        val dateText = DateFormat.format("EEEE,d MMMM,yyyy", calendar1).toString()
        date.text = dateText.split(" ").first().toString()
        month.text = dateText.split(" ").last().toString()
        name.text = slot.generated_by
        //number.text = slot.studentNumber
        //studentId.text = slot.studentId
        slotTiming.text = (slot.begins_At + ("-").toString() + slot.stop_At)

        return view
    }
}

