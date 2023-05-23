package com.example.seatreservationemployee.db.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ReservationDaoFB @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val TAG = "ReservationDaoFB"
    private val RESERVATION = "reservations"

    fun updateReservations(actualDate: LocalDate): Task<QuerySnapshot> {
        return db.collectionGroup("movie_reservations")
            .get()
            .addOnCompleteListener {
                it.result.documents.forEach {
                    val reservationPathArray = it.reference.path.split('/')
                    val reservationDate = LocalDate.parse(reservationPathArray[2])
                    if(reservationDate < actualDate)
                        deleteReservation(reservationPathArray)
                }
            }
    }

    private fun deleteReservation(reservationPathArray: List<String>) {
        db.collection(RESERVATION)
            .document(reservationPathArray[1])
            .collection(reservationPathArray[2])
            .document(reservationPathArray[3])
            .collection(reservationPathArray[4])
            .document(reservationPathArray[5])
            .delete()
    }
}