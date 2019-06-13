package com.example.slotbookingv2

class BookedData(
    val name: String,
    val status: String,
    val dateslot: String,
    val timeslot: String

) {
    constructor() : this("", "", "", "")

}
