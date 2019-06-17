package com.example.slotbookingv2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_mentor_slot_list.*


class MentorSlotList : AppCompatActivity() {
    lateinit var parts: MutableList<String>
    lateinit var listView: ListView
    var receiver: BroadcastReceiver? = null
    lateinit var qty:String
    lateinit var date:String
    lateinit var stime:String
    lateinit var etime:String
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Slots")
        setContentView(R.layout.activity_mentor_slot_list)
        listView = this.findViewById(R.id.listview)
        val bundle: Bundle? = intent.extras
        var list: String? = bundle?.getString("slotList")
        //textView.text=list.toString()
        val myString = list.toString()
        parts = myString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()

        parts.forEach {
            for (x in parts) {
                Log.d("TAG1", x)
            }
        }

        val adapter = local_slot_adapter(this, R.layout.slot_local_list_view, parts)

        listView.adapter = adapter

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter("custom-message"))

        save.setOnClickListener {
            //Toast.makeText(this,qty,Toast.LENGTH_LONG).show()
            var parts1 = qty.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()

                for (x in parts1) {
                    Log.d("TAG1", x)
                    date= x.split("$").last().toString().trim().replace("]]","")
                    stime= x.split("$").first().split("-").first().toString().trim().replace("[[","")
                    etime= x.split("$").first().split("-").last().toString().trim()
                    addSlot(stime, etime,date)
                    //Toast.makeText(this, stime+"-"+etime+" "+date, Toast.LENGTH_LONG).show()
                }
        }
    }
    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val ItemName = intent.getStringExtra("item")
            qty = intent.getStringExtra("quantity")
            //Toast.makeText(this@MentorSlotList, "$ItemName $qty", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun addSlot(begin: String, end: String, date: String) {
        val reserved_by = ""
        var generated = "Nikhil Nishad"
        var studentId = "234567"
        var studentNumber = "8765345674"
        var status = "NB"
        val sId = (ref.push().key).toString()
        val addSlot = slotsData(sId, begin, end, date, generated, reserved_by, studentId, studentNumber, status)
        ref.child(generated).child(sId).setValue(addSlot)
        Toast.makeText(this, begin+"-"+end+" "+date, Toast.LENGTH_LONG).show()
    }
}
