package com.example.slotbookingv2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class local_slot_adapter(val mCtx: Context, val layoutId: Int, var local_slotList: MutableList<String>) :
    ArrayAdapter<String>(mCtx, layoutId, local_slotList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutId, null)

        val stime = view.findViewById<TextView>(R.id.stime)
        val etime = view.findViewById<TextView>(R.id.etime)
        val date = view.findViewById<TextView>(R.id.date)
        val delete = view.findViewById<TextView>(R.id.delete)


        val slot = local_slotList[position]
        date.text = slot.split("$").last().toString()
        stime.text = slot.split("$").first().split("-").first().toString()
        etime.text = slot.split("$").first().split("-").last().toString()

        delete.setOnClickListener {
            local_slotList.removeAt(position)
            Log.d("TAG2", position.toString())

            for (x in local_slotList) {
                Log.d("TAG2", x)
            }
            notifyDataSetChanged()

        }
        return view
    }


}

