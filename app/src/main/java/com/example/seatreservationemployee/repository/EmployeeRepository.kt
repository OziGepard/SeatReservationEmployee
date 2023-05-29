package com.example.seatreservationemployee.repository

import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import java.time.LocalDate

interface EmployeeRepository {

    suspend fun employeeAuth(loginText: String, passwordText: String): Resource<AuthResult>

    suspend fun retrofitGetDate(): Resource<DateResponse>

    suspend fun updateReservations(actualDate: LocalDate): Resource<QuerySnapshot>

    suspend fun receiveIssues(): Resource<QuerySnapshot>
}