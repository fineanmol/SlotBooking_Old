package com.example.slotbookingv2

class slotsData(
    val begins_At: String,
    val stop_At: String,
    val date: String,
    val generated_by: String,
    val reserved_by: String,
    val studentId:String,
    val studentNumber:String,
    val status:String
) {
    constructor() : this("", "", "", "", "","","","")

}