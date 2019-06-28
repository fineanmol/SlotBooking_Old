package com.sibmentor.appointment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class m_show_slot_list_adapter(val mCtx: Context, val layoutId: Int, val slotList: List<slotsData>) :
    ArrayAdapter<slotsData>(mCtx, layoutId, slotList) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val ref = FirebaseDatabase.getInstance().getReference("Slots")
    val userref = FirebaseDatabase.getInstance().getReference("users")
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        val name = view.findViewById<TextView>(R.id.student_Name)
        val date = view.findViewById<TextView>(R.id.dateslot)
        val TimeslotTextView = view.findViewById<TextView>(R.id.textView)

        val slotTiming = view.findViewById<TextView>(R.id.slot_timing)
        val status = view.findViewById<TextView>(R.id.status)
        val delete = view.findViewById<TextView>(R.id.deletebtn)


        val slot = slotList[position]


        date.text = "${slot.date.split("/").first()} - ${slot.date.split("/")[1]}"

        slotTiming.text = slot.begins_At.split("[").last().toString() + ("-").toString() + slot.stop_At
        if (slot.status == "B") {
            status.setTextColor(Color.GREEN)
            name.setTextColor(Color.RED)
            status.text = context.getString(R.string.slot_status)
            name.text = "By: ${slot.reserved_by}"

        }
        if (slot.status != "B") {
            status.setTextColor(Color.LTGRAY)


            status.text = "Not Booked Yet"
        }

        /** Delete Button for Mentor*/
        delete.setOnClickListener {
            deleteInfo(slot)
        }



        return view
    }

    /** Delete Button Functionality for Mentor*/
    private fun deleteInfo(slots: slotsData) {
        var s_id = ""
        /** User Data Updated Function*/
        // val userNameRef = ref.parent?.child("users")?.orderByChild("studentId")?.equalTo(slots.studentId)
        //  userref.child(slots.studentId!!).child("status").setValue("B")
        if (slots.reserved_by != "" && slots.studentId != "") {
            val userNameRef = ref.parent?.child("users")?.orderByChild("studentId")?.equalTo(slots.studentId)
            userNameRef?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (e in p0.children) {
                        val student = e.getValue(Data::class.java)
                        s_id = student?.id.toString()
                    }
                }

            })
            userref.child(s_id).child("status").setValue("NB")
            val myDatabase = FirebaseDatabase.getInstance().getReference("Slots").child("Nikhil Nishad")
            myDatabase.child(slots.sid).removeValue()
            Toast.makeText(mCtx, "Deleted !", Toast.LENGTH_LONG).show()
        }
    }
}


