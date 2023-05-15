package com.example.seatreservationemployee.repository

import com.example.seatreservationemployee.utils.Resource
import com.example.seatreservationemployee.utils.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    val mAuth: FirebaseAuth
) {

    suspend fun employeeAuth(loginText: String, passwordText: String): Resource<AuthResult> {

        return withContext(Dispatchers.IO){
            safeCall {
                val result = mAuth.signInWithEmailAndPassword(loginText, passwordText).await()
                Resource.Success(result)
            }
        }
    }
}