package com.example.citorapp.home.detailBooking

object BookingData {
    private val waktuBooking = arrayOf(
        "08:00-08:30",
        "09:00-09:00",
        "09:00-09:30",
        "09:30-10:00",
        "10:00-10:30",
        "10:30-11:00",
        "11:00-11:30",
        "11:30-12:00",
        "12:00-12:30",
        "12:30-13:00",
        "13:00-13:30",
        "13:30-14:00",
        "14:00-14:30",
        "14:30-15:00",
        "15:00-15:30",
        "15:30-16:00",
        )
    val listData: ArrayList<BookingModel>
        get() {
            val list = arrayListOf<BookingModel>()
            for (position in waktuBooking.indices) {
                val booking = BookingModel()
                booking.jamBuka = waktuBooking[position]
                list.add(booking)
            }
            return list
        }
}