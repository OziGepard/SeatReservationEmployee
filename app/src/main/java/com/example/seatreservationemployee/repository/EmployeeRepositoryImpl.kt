package com.example.seatreservationemployee.repository

import com.example.seatreservationemployee.db.CinemaFirebaseDatabase
import com.example.seatreservationemployee.retrofit.DateAPI
import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.Resource
import com.example.seatreservationemployee.utils.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.await
import java.time.LocalDate
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val retrofitDate: DateAPI,
    private val dbFB: CinemaFirebaseDatabase
) : EmployeeRepository {

    override suspend fun employeeAuth(loginText: String, passwordText: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = mAuth.signInWithEmailAndPassword(loginText, passwordText).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun retrofitGetDate(): Resource<DateResponse> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = retrofitDate.getActualDate().await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun updateReservations(actualDate: LocalDate): Resource<QuerySnapshot> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = dbFB.getReservationDaoFB.updateReservations(actualDate).await()
                Resource.Success(result)
            }
        }
    }

    override suspend fun receiveIssues(): Resource<QuerySnapshot> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = dbFB.getReservationDaoFB.receiveIssues().await()
                Resource.Success(result)
            }
        }
    }
}