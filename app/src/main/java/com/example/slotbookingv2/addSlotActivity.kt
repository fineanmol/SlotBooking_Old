package com.example.slotbookingv2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_slot.*
import java.util.*


class addSlotActivity : AppCompatActivity() {
    private val TAG = "addSlotActivity"
    lateinit var dateButton: Button
    lateinit var timeButton: Button
    lateinit var dateTextView: TextView
    lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slot)

        dateButton = findViewById<Button>(R.id.dateButton)
        timeButton = findViewById<Button>(R.id.timeButton)
        dateTextView = findViewById<TextView>(R.id.dateTextView)
        timeTextView = findViewById<TextView>(R.id.timeTextView)

        dateButton.setOnClickListener(View.OnClickListener { handleDateButton() })
        timeButton.setOnClickListener(View.OnClickListener { handleTimeButton() })

        submit_slot.setOnClickListener {

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
                dateTextView.text = dateText
            }, YEAR, MONTH, DATE
        )

        datePickerDialog.show()


    }

    private fun handleTimeButton() {
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
            timeTextView.text = dateText
        }, HOUR, MINUTE, is24HourFormat)

        timePickerDialog.show()

    }
}

