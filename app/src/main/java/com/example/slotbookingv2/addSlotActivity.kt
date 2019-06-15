package com.example.slotbookingv2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_slot.*
import java.util.*


class addSlotActivity : AppCompatActivity() {
    private val TAG = "addSlotActivity"
    private lateinit var mAuth: FirebaseAuth
    lateinit var ref: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slot)
        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Slots")
        var timeFlagS = 0
        var timeFlagE = 0

        setDate.setOnClickListener(View.OnClickListener { handleDateButton() })
        setSTime.setOnClickListener(View.OnClickListener { handleSTimeButton() })
        setETime.setOnClickListener(View.OnClickListener { handleETimeButton() })

        generateSlot.setOnClickListener {

            var Stime = slotSTime.text.toString()
            var Etime = slotETime.text.toString()
            val slotDuration = slotDuration.text.toString()
            val interval = setBreak.text.toString()
            val sdate = slotDate.text.toString()

            var StimeHH = Stime.split(":").first().toInt()
            var StimeMM = Stime.split(":").last().split(" ").first().toInt()
            val StimeHour = Stime.split(":").last().split(" ").last()
            if (StimeHour == "PM") {
                StimeHH = StimeHH + 12
                timeFlagS = 1
            }
            var EtimeHH = Etime.split(":").first().toInt()
            val EtimeMM = Etime.split(":").last().split(" ").first().toInt()
            val EtimeHour = Etime.split(":").last().split(" ").last()
            if (EtimeHour == "PM") {
                EtimeHH = EtimeHH + 12
                timeFlagE = 1
            }
            var slots = "Slots are:"
            while (EtimeHH > StimeHH) {
                var startH = StimeHH
                var startM = StimeMM
                //var temp=slotDuration.toInt()+interval.toInt()
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.HOUR_OF_DAY, startH)
                calendar1.set(Calendar.MINUTE, startM)
                calendar1.set(Calendar.AM_PM, timeFlagS)
                val begin = DateFormat.format("h:mm a", calendar1).toString()
                calendar1.add(Calendar.MINUTE, slotDuration.toInt())
                val end = DateFormat.format("h:mm a", calendar1).toString()
                //addSlot(begin,end)
                slots = slots + ":" + begin + "--" + end
                Log.d(TAG, slots)
                calendar1.add(Calendar.MINUTE, interval.toInt())
                StimeHH = calendar1.get(Calendar.HOUR)
                StimeMM = calendar1.get(Calendar.MINUTE)
                timeFlagS = calendar1.get(Calendar.AM_PM)
            }
            timeDiff.text = slots
        }
    }

    private fun handleDateButton() {
        val calendar = Calendar.getInstance()
        val YEAR = calendar.get(Calendar.YEAR)
        val MONTH = calendar.get(Calendar.MONTH)
        val DATE = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, date ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.YEAR, year)
                calendar1.set(Calendar.MONTH, month)
                calendar1.set(Calendar.DATE, date)
                val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString()
                slotDate.text = dateText
                handleSTimeButton()
            }, YEAR, MONTH, DATE
        )

        datePickerDialog.show()


    }

    private fun handleSTimeButton() {
        val calendar = Calendar.getInstance()
        val HOUR = calendar.get(Calendar.HOUR)
        val MINUTE = calendar.get(Calendar.MINUTE)
        val is24HourFormat = DateFormat.is24HourFormat(this)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            Log.i(TAG, "onTimeSet: $hour$minute")
            val calendar1 = Calendar.getInstance()
            calendar1.set(Calendar.HOUR, hour)
            calendar1.set(Calendar.MINUTE, minute)
            val dateText = DateFormat.format("h:mm a", calendar1).toString()
            slotSTime.text = dateText
            handleETimeButton()
        }, HOUR, MINUTE, is24HourFormat)
        timePickerDialog.show()
    }

    private fun handleETimeButton() {
        val calendar = Calendar.getInstance()
        val HOUR = calendar.get(Calendar.HOUR)
        val MINUTE = calendar.get(Calendar.MINUTE)
        val is24HourFormat = DateFormat.is24HourFormat(this)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            Log.i(TAG, "onTimeSet: $hour$minute")
            val calendar1 = Calendar.getInstance()
            calendar1.set(Calendar.HOUR, hour)
            calendar1.set(Calendar.MINUTE, minute)
            val dateText = DateFormat.format("h:mm a", calendar1).toString()
            slotETime.text = dateText
        }, HOUR, MINUTE, is24HourFormat)
        timePickerDialog.show()
    }

    private fun addSlot(begin: String, end: String): Boolean {
        val reserved_by = ""
        val date = slotDate.text.toString()
        var generated = "Nikhil Nishad"
        val sId = (ref.push().key).toString()
        val addSlot = slotsData(begin, end, date, generated, reserved_by)

        ref.child(generated).child(sId).setValue(addSlot)
        Toast.makeText(this, "Slots Added", Toast.LENGTH_LONG).show()

        return true
    }

}
