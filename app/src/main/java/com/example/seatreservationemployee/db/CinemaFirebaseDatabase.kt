package com.example.seatreservationemployee.db

import com.example.seatreservationemployee.db.firebase.ReservationDaoFB
import com.google.firebase.firestore.FirebaseFirestore

class CinemaFirebaseDatabase(
    dbFBInstance: FirebaseFirestore
) {
    val getReservationDaoFB = ReservationDaoFB(dbFBInstance)
}