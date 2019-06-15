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
    var slotList = ArrayList<String>()
    


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

          //  valueSplit(Stime,Etime)

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
            SlotGenerator(Stime, StimeHH, StimeMM, StimeHour, Etime, EtimeHH, EtimeMM, EtimeHour,interval,slotDuration)
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

    /*Test Slot Generation*/

    private fun SlotGenerator(stime: String, stimeHH: Int, stimeMM: Int, stimeHour: String, etime: String, etimeHH: Int, etimeMM: Int, etimeHour: String, interval: String, slotDuration: String) {
        var valuesList = ArrayList<String>()
        var slotDurationValue= interval.toInt() + slotDuration.toInt()
        var slots = stime + ("-").toString()
        var startime=stime
        var starthour= stimeHH
        var startminute= stimeMM
        var startday = stimeHour
        var endtime = etime
        var endhour= etimeHH
        var endminute= etimeMM
        var endday=etimeHour

        for(i in 1..5) {
            if (startminute + slotDurationValue <= 60) {
                var newminutes = startminute + slotDurationValue
                var nextslotvalue =
                    starthour.toString() + (":").toString() + newminutes.toString() + (":").toString() + startday
                slots += nextslotvalue
                slotList.add((slots))
                startime = nextslotvalue
                valuesList = valueSplit(startime, etime)
                starthour = valuesList[0].toInt()
                startminute = valuesList[1].toInt()
                startday = valuesList[2]
                if(starthour>=endhour){
                    break
                }


            }
            if ((stimeMM + slotDurationValue) > 60 && (stimeMM + slotDurationValue < 120) ) {

                    var newhouradd = (stimeMM + slotDurationValue).div(60)
                    var newhour = stimeHH + newhouradd
                    var newminutes = -(60 - (stimeMM + slotDurationValue))
                    var nextslotvalue =
                        newhour.toString() + (":").toString() + newminutes.toString() + (":").toString() + stimeHour
                    slots += nextslotvalue
                    slotList.add((slots))
                    startime = nextslotvalue
                    valuesList = valueSplit(startime, etime)
                    starthour = valuesList[0].toInt()
                    startminute = valuesList[1].toInt()
                    startday = valuesList[2]
                    if(starthour>=endhour){
                        break
                    }

            }
        }
    }
    /*Test Slot Generation ENds*/

    
    /*Values Split Starts*/
    private fun valueSplit(stime: String, etime: String): ArrayList<String> {
        var returnList = ArrayList<String>()
        var timeFlagS = 0
        var timeFlagE = 0
        var StimeHH = stime.split(":").first().toInt()
        var StimeMM = stime.split(":").last().split(" ").first().toInt()
        val StimeHour = stime.split(":").last().split(" ").last()
        if (StimeHour == "PM") {
            StimeHH = StimeHH + 12
            timeFlagS = 1
        }
        var EtimeHH = etime.split(":").first().toInt()
        val EtimeMM = etime.split(":").last().split(" ").first().toInt()
        val EtimeHour = etime.split(":").last().split(" ").last()
        if (EtimeHour == "PM") {
            EtimeHH = EtimeHH + 12
            timeFlagE = 1
        }
        returnList.add(StimeHH.toString())
        returnList.add(StimeMM.toString())
        returnList.add(StimeHour)
        returnList.add(EtimeHH.toString())
        returnList.add(EtimeMM.toString())
        returnList.add(EtimeHour)

        return (returnList)
    }
    /*Values Split End*/

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
