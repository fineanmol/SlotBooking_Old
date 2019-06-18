package com.example.slotbookingv2

//import jdk.nashorn.internal.objects.NativeDate.getTime
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import java.text.ParseException
import java.text.SimpleDateFormat
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
            if (Stime == "Select Start Time *" || Stime.isNullOrEmpty()) {
                slotSTime.error = "Start Time Required"
                slotSTime.requestFocus()
                Toast.makeText(this, "Start Time Required !!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var StimeHH = Stime.split(":").first().toString()
            var StimeMM = Stime.split(":").last().split(" ").first().toString()
            val StimeHour = Stime.split(":").last().split(" ").last()
            if (StimeHour == "PM") {
                StimeHH = StimeHH + 12
                timeFlagS = 1
            }
            var EtimeHH = Etime.split(":").first().toString()
            val EtimeMM = Etime.split(":").last().split(" ").first().toString()
            val EtimeHour = Etime.split(":").last().split(" ").last()
            if (EtimeHour == "PM") {
                // EtimeHH = EtimeHH + 12
                timeFlagE = 1
            }

            displayTimeSlots(StimeHH, StimeMM, EtimeHH, EtimeMM, EtimeHour, sdate, slotDuration, interval)


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
                val dateText = DateFormat.format("EEEE,MM d,yyyy", calendar1).toString()
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

    private fun addSlot(begin: String, end: String, date: String): Boolean {
        val reserved_by = ""
        var generated = "Nikhil Nishad"
        var studentId = ""
        var studentNumber = ""
        var status = "NB"
        val sId = (ref.push().key).toString()

        val addSlot = slotsData(sId, begin, end, date, generated, reserved_by, studentId, studentNumber, status)


        ref.child(generated).child(sId).setValue(addSlot)
        Toast.makeText(this, "Slots Added", Toast.LENGTH_LONG).show()

        return true
    }

    private fun getHoursValue(hours: Int): Int {
        return hours - 12
    }

    private fun displayTimeSlots(
        StimeHH: String,
        StimeMM: String,
        EtimeHH: String,
        EtimeMM: String,
        EtimeHour: String,
        sdate: String,
        slotDuration: String,
        interval: String
    ) {
        var sdateL = sdate.split(" ").last().toString().trim()
        var sdateF = sdate.split(" ").first().toString().trim()
        var sdateM = sdateF.split(",").last()
        var sdateD = sdateL.split(",").first()
        var sdateY = sdateL.split(",").last()

        var dateText = sdateY + "-" + sdateM + "-" + sdateD
        val dateValue = dateText
        val endDateValue = dateText

        var hours = StimeHH
        var minutes = StimeMM


        val amOrPm: String
        if (Integer.parseInt(hours) < 12) {
            amOrPm = "AM"
        } else {
            amOrPm = "PM"
            hours = getHoursValue(Integer.parseInt(hours)).toString()
        }
        val time1 = "$hours:$minutes $amOrPm"
        val time2 = EtimeHH + ":" + EtimeMM + " " + EtimeHour + " "
        val format = "yyyy-MM-dd hh:mm a"

        val sdf = SimpleDateFormat(format)

        try {
            val dateObj1 = sdf.parse("$dateValue $time1")
            val dateObj2 = sdf.parse("$endDateValue $time2")
            Log.d("TAG", "Date Start: $dateObj1")
            Log.d("TAG", "Date End: $dateObj2")
            var dif = dateObj1.time
            while (dif < dateObj2.time) {
                val slot1 = Date(dif)
                dif += slotDuration.toInt() * 60 * 1000
                val slot2 = Date(dif)
                dif += interval.toInt() * 60 * 1000
                val sdf1 = SimpleDateFormat("hh:mm a")
                val sdf2 = SimpleDateFormat("hh:mm a, dd/MM/yy")
                Log.d("TAG", "Hour slot = " + sdf1.format(slot1) + " - " + sdf2.format(slot2))
                val Fdate = sdf2.format(slot2).split(",").last()
                //addSlot(sdf1.format(slot1), sdf2.format(slot2).split(",").first(), Fdate)
                var listvalue = sdf1.format(slot1) + "-" + sdf2.format(slot2).split(",").first() + "$" + Fdate
                slotList.add(listvalue)


            }

            var intent = Intent(this, MentorSlotList::class.java)
            intent.putExtra("slotList", slotList.toString())
            intent.putExtra("slotLists", slotList)
            startActivity(intent)
        } catch (ex: ParseException) {
            ex.printStackTrace()

        }
    }


}