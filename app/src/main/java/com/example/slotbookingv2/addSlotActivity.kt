package com.example.slotbookingv2

//import jdk.nashorn.internal.objects.NativeDate.getTime
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
            val sdate = slotDate.text.toString()
            var Stime = slotSTime.text.toString()
            var Etime = slotETime.text.toString()
            val slotDurations = slotDuration.text.toString()
            var interval = setBreak.text.toString()


            if (sdate == ("Select Date*") || sdate.isNullOrEmpty()) {
                slotDate.error = "Choose Date First !!"
                slotDate.requestFocus()
                Toast.makeText(this, "Date is Mandatory to Generate Slots !!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            slotDate.error = null

            if (Stime == "Select Start Time *" || Stime.isNullOrEmpty()) {
                slotSTime.error = "Start Time Required"
                slotSTime.requestFocus()
                Toast.makeText(this, "Start Time Required !!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            slotSTime.error = null
            if (Etime == "Select Slot End Time *" || Etime.isNullOrEmpty()) {
                slotETime.error = "End Time Required"
                slotETime.requestFocus()
                Toast.makeText(this, "End Time Required !!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            slotETime.error = null
            if (slotDurations == "Enter Slot Duration(in m)" || slotDurations.isNullOrEmpty()) {
                slotDuration.error = "End Time Required"
                slotDuration.requestFocus()
                return@setOnClickListener
            }
            if (interval == "" || interval.isNullOrEmpty()) {
                interval = "0"
            }



            var StimeHH = Stime.split(":").first().toString()
            var StimeMM = Stime.split(":").last().split(" ").first().toString()
            var StimeHour = Stime.split(":").last().split(" ").last()
            if (StimeHour == "PM" || StimeHour.toLowerCase() == "pm") {
                // StimeHH = StimeHH + 12
                StimeHour = "PM"
                timeFlagS = 1
            }
            var EtimeHH = Etime.split(":").first().toString()
            val EtimeMM = Etime.split(":").last().split(" ").first().toString()
            var EtimeHour = Etime.split(":").last().split(" ").last()
            if (EtimeHour == "PM" || EtimeHour.toLowerCase() == "pm") {
                // EtimeHH = EtimeHH + 12
                EtimeHour = "PM"
                timeFlagE = 1
            }

            displayTimeSlots(StimeHH, StimeMM, EtimeHH, EtimeMM, EtimeHour, sdate, slotDurations, interval, StimeHour)


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
        interval: String,
        stimeHour: String
    ) {
        val sdateL = sdate.split(" ").last().toString().trim()
        var sdateF = sdate.split(" ").first().toString().trim()
        var sdateM = sdateF.split(",").last()
        var sdateD = sdateL.split(",").first()
        var sdateY = sdateL.split(",").last()

        var dateText = sdateY + "-" + sdateM + "-" + sdateD
        val dateValue = dateText
        val endDateValue = dateText

        var hours = StimeHH
        var minutes = StimeMM
        var ampm = stimeHour

        val amOrPm: String
        if (Integer.parseInt(hours) < 12) {
            amOrPm = "AM"
        } else {
            amOrPm = "PM"
            hours = getHoursValue(Integer.parseInt(hours)).toString()
        }
        val time1 = "$hours:$minutes $ampm"
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
            var intent = Intent(this, addSlotActivity::class.java)
            startActivity(intent)
            ex.printStackTrace()

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menulogout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_logout) {

            logout()

            return true
        }

        if (id == R.id.contactUs) {
            startActivity(Intent(this, AboutDeveloper::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /* override fun onNavigationItemSelected(item: MenuItem): Boolean {
         // Handle navigation view item clicks here.
         when (item.itemId) {
             R.id.nav_addslot -> {
                 // Handle the camera action
                 startActivity(Intent(this, addSlotActivity::class.java))
                 Toast.makeText(this, "Add Slot Clicked", Toast.LENGTH_LONG).show()
             }
             R.id.show_appointment -> {
                 startActivity(Intent(this, AppointmentList2::class.java))
                 Toast.makeText(this, "Work in Progress", Toast.LENGTH_LONG).show()
             }

             R.id.nav_tools -> {

             }
             R.id.nav_share -> {
                 val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                 sharingIntent.type = "text/plain"
                 val shareBody =
                     "Hey \n Slot Booking Application is a fast,simple and secure app that I use to book my slot with Mentor and Manage all the data.\n\n Get it for free at\n App link "
                 sharingIntent.putExtra(
                     android.content.Intent.EXTRA_SUBJECT,
                     "Slot Booking Management : Android Application"
                 )
                 sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                 startActivity(Intent.createChooser(sharingIntent, "Share via"))

             }
             R.id.nav_reportbug -> {
                 val intent = Intent(
                     Intent.ACTION_SENDTO, Uri.fromParts(
                         "mailto", "agarwal.anmol2004@gmail.com", null
                     )
                 )
                 intent.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements")
                 intent.putExtra(Intent.EXTRA_TEXT, android.R.id.message)
                 startActivity(Intent.createChooser(intent, "Choose an Email client :"))

             }
         }
         val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

         drawerLayout.closeDrawer(GravityCompat.START)
         return true
     }*/

    override fun onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        val alertbox = AlertDialog.Builder(this)
            .setMessage("Do you want to leave the page?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { arg0, arg1 ->
                // do something when the button is clicked
                var intent = Intent(this, mentorhomev2::class.java)
                startActivity(intent)

            })
            .setNegativeButton("No", // do something when the button is clicked
                DialogInterface.OnClickListener { arg0, arg1 -> })
            .show()
    }

}