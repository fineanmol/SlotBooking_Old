package com.example.slotbookingv2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase





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
            ref.child(slot.generated_by).child(id).child("status").setValue("B")
            Toast.makeText(mCtx, "Booked!" + id, Toast.LENGTH_LONG).show()

        }

        /*deleteBtn.setOnClickListener {
            deleteInfo(employee)
        }*/

        return view
    }

    /*private fun updateInfo(employee: List<slotsData>) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Info")
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.user_update, null)
        val email = view.findViewById<EditText>(R.id.editemail)
        val pass = view.findViewById<EditText>(R.id.editpass)
        val name = view.findViewById<EditText>(R.id.nameupdate)
        val number = view.findViewById<EditText>(R.id.numberupdate)
        val dob = view.findViewById<EditText>(R.id.numberupdate)
        email.setText(employee.email)
        pass.setText(employee.pass)
        name.setText(employee.name)
        dob.setText(employee.dob)
        builder.setView(view)
        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val myDatabase = FirebaseDatabase.getInstance().getReference("users")
                val email1 = email.text.toString().trim()
                val pass2 = pass.text.toString().trim()
                val name= name.text.toString().trim()
                val number =number.text.toString().trim()
                val dob = dob.text.toString().trim()
                if (email1.isEmpty()) {
                    email.error = "Please enter your email"
                    return
                }
                if (pass2.isEmpty()) {
                    pass.error = "Please enter your pass"
                    return
                }
                val employee = Data(employee.id, email1, pass2,name,number,dob)
                myDatabase.child(employee.id).setValue(employee)
                Toast.makeText(mCtx, "Updated :) ", Toast.LENGTH_LONG).show()
            }
        })
        builder.setNegativeButton("cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }
        })
        val alert = builder.create()
        alert.show()
    }*/


    private fun deleteInfo(slotsData: List<slotsData>)
    {

    }



}
