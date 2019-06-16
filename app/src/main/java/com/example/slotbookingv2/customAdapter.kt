package com.example.slotbookingv2

import android.content.Context
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

class customAdapterc(val mCtx: Context, val layoutId: Int, val slotList: List<slotsData>) :
    ArrayAdapter<slotsData>(mCtx, layoutId, slotList) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val ref = FirebaseDatabase.getInstance().getReference("Slots")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        val mentor = view.findViewById<TextView>(R.id.mentor_Name)
        val date = view.findViewById<TextView>(R.id.dateslot)
        val time = view.findViewById<TextView>(R.id.slot_timing)

        val book = view.findViewById<TextView>(R.id.bookbtn)
        //val deleteBtn = view.findViewById<TextView>(R.id.delete)

        val slot = slotList[position]

        mentor.text = slot.generated_by
        date.text = slot.date
        time.text = (slot.begins_At + ("-").toString() + slot.stop_At)

        book.setOnClickListener {
            var id = slot.sid
            currentUser?.let { user ->
                Toast.makeText(mCtx, user.email, Toast.LENGTH_LONG).show()
                val userNameRef = ref.parent?.child("users")?.orderByChild("email")?.equalTo(user.email)
                val eventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            //create new user
                            Toast.makeText(mCtx, "User details not found", Toast.LENGTH_LONG).show()
                        } else {
                            for (e in dataSnapshot.children) {
                                val employee = e.getValue(Data::class.java)
                                var studentId = employee?.studentId
                                var studentName = employee?.name
                                var phone = employee?.number
                                ref.child(slot.generated_by).child(id).child("studentNumber").setValue(phone)
                                ref.child(slot.generated_by).child(id).child("reserved_by").setValue(studentName)
                                ref.child(slot.generated_by).child(id).child("studentId").setValue(studentId)
                                ref.child(slot.generated_by).child(id).child("status").setValue("B")
                                Toast.makeText(mCtx, studentName +" You Booked! an appointment for: "+time.text , Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
                userNameRef?.addListenerForSingleValueEvent(eventListener)

            }


        }
        return view
    }
}

