package com.example.slotbookingv2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_mentor_slot_list.*

class MentorSlotList : AppCompatActivity() {
    lateinit var sList: MutableList<String>
    lateinit var listview: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_slot_list)
        ListValue()


        val bundle: Bundle? = intent.extras
        var list: String? = bundle?.getString("slotList")
        textView.text = list.toString()
        list = list
        if (list != null) {
            list = list.replace("[", "")
            list = list.replace("]", "")
        }
        if (list != null) {
            list = list.split(",").toString()
        }
        if (list != null) {
            for (e in list) {
                Log.d("TAG", e.toString())
            }
        }

        if (list != null) {
            for (i in list) {
                var listvalue = i
            }
        }
    }

    private fun ListValue() {
        val serializableExtra = intent.getSerializableExtra("slotLists").toString()
        for (i in 1..10) {
            var value = serializableExtra[i].toString()
            var new = 0
        }


    }
}

